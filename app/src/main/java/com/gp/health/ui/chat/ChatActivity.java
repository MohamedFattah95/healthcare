package com.gp.health.ui.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gp.health.BuildConfig;
import com.gp.health.R;
import com.gp.health.data.models.MessageModel;
import com.gp.health.databinding.ActivityChatBinding;
import com.gp.health.di.component.ActivityComponent;
import com.gp.health.ui.base.BaseActivity;
import com.gp.health.ui.main.MainActivity;
import com.gp.health.ui.member_profile.MemberProfileActivity;
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.DateUtility;
import com.gp.health.utils.ErrorHandlingUtils;
import com.gp.health.utils.ImageUtils;
import com.gp.health.utils.LanguageHelper;
import com.gp.health.utils.LocationUtils;
import com.gp.health.utils.MediaUtils;
import com.gp.health.utils.PermissionsUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChatActivity extends BaseActivity<ChatViewModel> implements ChatNavigator {

    private static final String TAG = "ChatActivity";
    public static final int REQUEST_LOCATION_CODE = 999;
    public static final int REQUEST_MIC_CODE = 888;

    private final int PERMISSION_ALL = 100;
    private final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private DatabaseReference rootRef;
    private StorageReference storageReference;
    private ChatAdapter mChatAdapter;
    private ChatImagesAdapter mChatImgsAdapter;
    private int senderId;
    private int receiverId;
    private String roomId;
    private MediaRecorder mRecorder;
    private String mFileRecord = null;
    private String lang;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationManager manager;
    private final static long UPDATE_INTERVAL = 2 * 1000;  /* 2 secs */
    private final static long FASTEST_INTERVAL = 1000; /* 1 sec */
    private double lat = 0, lng = 0;


    private float dX;
    private float dXNew = 500f; //500.0 is almost half
    private float dXBase;


    ActivityChatBinding binding;

    String badWords = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mViewModel.setNavigator(this);

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);

        subscribeViewModel();

        setUp();
    }

    private void setUp() {
        setUpOnViewClicked();
        CommonUtils.setupRatingBar(binding.toolbar.ratingBarDelegate);
        lang = LanguageHelper.getLanguage(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        rootRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("Media");
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        senderId = mViewModel.getDataManager().getCurrentUserId();
        receiverId = getIntent().getIntExtra("receiver_id", -1);

        //roomId will be " smallerIdNumber, biggerIdNumber "
        if (senderId < receiverId) {
            roomId = senderId + "," + receiverId;
        } else {
            roomId = receiverId + "," + senderId;
        }

        setupMsgsRecycler();
        setupImgsRecycler();
        retrieveAllMessages();
        showAllButVoice();
        //Request Permissions
        if (!PermissionsUtils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        mFileRecord = this.getCacheDir().getAbsolutePath() + "/record_voice.mp3";

        showLoading();
        mViewModel.getUserInfo(receiverId);
        mViewModel.getBadWords();


    }

    @SuppressLint("LogNotTimber")
    private void subscribeViewModel() {

        mViewModel.getChatNotificationLiveData().observe(this, response -> {
            hideLoading();
            Log.e(TAG, "ChatViewModel: " + "Notification Sent");
        });

        mViewModel.getBadWordsLiveData().observe(this, response -> {
            hideLoading();
            badWords = response.getData();
        });

        mViewModel.getUserInfoLiveData().observe(this, response -> {
            hideLoading();
            bindMemberData(response.getData().getName(),
                    response.getData().getImage() != null ? response.getData().getImage() : "",
                    response.getData().getRate(),
                    response.getData().getRateCount());
        });

    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ChatActivity.class);
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void openOrderDetailsActivity() {
//        startActivity(OrderDetailsActivity.newIntent(ChatActivity.this));
    }

    @Override
    public void userIsBlocked(String message) {
        hideLoading();
        showErrorMessage(message);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        showErrorMessage(message);
    }

    @SuppressLint("SetTextI18n")
    private void bindMemberData(String name, String image, float rate, int rateCount) {
        mChatAdapter.mReceiverImage = image;
        mChatAdapter.mReceiverName = name;
        mChatAdapter.mSenderImage = mViewModel.getDataManager().getUserObject().getImage();
        mChatAdapter.notifyDataSetChanged();

        Glide.with(this)
                .load(image)
                .error(R.drawable.ic_user_holder)
                .placeholder(R.drawable.ic_user_holder)
                .into(binding.toolbar.delegateImage);

        binding.toolbar.delegateName.setText(name);
        binding.toolbar.ratingBarDelegate.setRating(rate);

        binding.toolbar.tvRateCount.setText("(" + rateCount + ")");

    }

    @SuppressLint("ClickableViewAccessibility")
    void setUpOnViewClicked() {

        binding.sendBtn.setOnClickListener(v -> {

            if (badWords != null && !binding.messageEditText.getText().toString().isEmpty()) {

                String[] msg = binding.messageEditText.getText().toString().trim()
                        .replaceAll("أ", "ا")
                        .replaceAll("إ", "ا")
                        .replaceAll("آ", "ا")
                        .replaceAll("ى", "ي")
                        .replaceAll("ة", "ه")
                        .split(" ");

                for (int i = 0; i < msg.length; i++) {

                    if (badWords.toLowerCase().contains(msg[i].toLowerCase().trim())) {
                        showErrorMessage(getString(R.string.msg_contain_undesired_words) + " \"" + msg[i].trim() + "\"");
                        break;
                    }

                    if (i == msg.length - 1) {
                        sendTextMessage();
                        sendImgsMessage();
                    }

                }


            } else {
                sendTextMessage();
                sendImgsMessage();
            }
        });

        binding.toolbar.backButton.setOnClickListener(v -> {
            if (isLastActivity(this)) {
                startActivity(getIntentWithClearHistory(MainActivity.class));
            }
            finish();
        });

        binding.toolbar.llMember.setOnClickListener(v -> {
            startActivity(MemberProfileActivity.newIntent(this).putExtra("userId", receiverId));
        });

        binding.imagesBtn.setOnClickListener(v ->
                ImagePicker.Companion.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(720, 720)
                        .start(1));

        binding.locationLinkBtn.setOnClickListener(v -> {
            if (!PermissionsUtils.hasPermissions(this, PERMISSIONS))
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showMessage(getString(R.string.open_gps));
                return;
            }
            if (LocationUtils.checkLocationPermission(this, REQUEST_LOCATION_CODE))
                shareCurrentLocation();
        });

        binding.recordButton.setOnTouchListener((v, event) -> {
            if (!MediaUtils.checkMicPermission(this, REQUEST_MIC_CODE)) {
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dXBase = v.getX();
                    dX = v.getX() - event.getRawX();
                    showVoiceOnly();
                    showMessage(getString(R.string.recording));
                    startRecording();
                    return true;
                case MotionEvent.ACTION_UP:
                    showAllButVoice();
                    v.animate().x(dXBase).setDuration(0).start(); //reset
                    if (lang.equals("ar") && dXNew >= 500f)
                        stopRecordingAndUpload();
                    else if (lang.equals("en") && dXNew <= 500f)
                        stopRecordingAndUpload();
                    else
                        cancelRecording();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    dXNew = event.getRawX() + dX;
                    //showMessage(dXNew + "");
                    v.animate().x(dXNew).setDuration(0).start(); //drag in X-Axis
                    return true;
            }
            return true;
        });

    }

    @Override
    public void onBackPressed() {
        if (isLastActivity(this)) {
            startActivity(getIntentWithClearHistory(MainActivity.class));
        }
        super.onBackPressed();
    }


    @SuppressLint("MissingPermission")
    private void shareCurrentLocation() {
        // Create the location request to start receiving updates
        showLoading();
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationCallback = new LocationCallback() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onLocationResult(@NotNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.e(TAG, "Lat: " + location.getLatitude());
                Log.e(TAG, "Lng: " + location.getLongitude());
                lat = location.getLatitude();
                lng = location.getLongitude();
                mFusedLocationClient.removeLocationUpdates(locationCallback);

                String locationMsg = lat + "," + lng;
                DatabaseReference roomRef = rootRef.child("Chat").child(roomId);
                String messageKey = roomRef.push().getKey();
                MessageModel message = new MessageModel("location", receiverId, senderId, locationMsg, DateUtility.getCurrentTimeStamp(), false);
                if (messageKey != null && lat != 0 && lng != 0) {
                    roomRef.child(messageKey).setValue(message);
                    hideLoading();
                    lat = 0;
                    lng = 0;

                    mViewModel.sendChatNotification(senderId, receiverId);
                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }


    @SuppressLint("LogNotTimber")
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileRecord);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Log.e(TAG, "startRecording: Failed");
        }
    }


    private void cancelRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            showMessage(getString(R.string.canceled));
        } catch (RuntimeException stopException) {
            stopException.printStackTrace();
        }
    }

    private void stopRecordingAndUpload() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            uploadVoiceRecord();
        } catch (RuntimeException stopException) {
            stopException.printStackTrace();
        }
    }

    private void uploadVoiceRecord() {
        showLoading();
        StorageReference mFileRef = storageReference.child(roomId).child(System.currentTimeMillis() + "_record.mp3");
        Uri uri = Uri.fromFile(new File(mFileRecord));
        mFileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> mFileRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
            DatabaseReference roomRef = rootRef.child("Chat").child(roomId);
            String messageKey = roomRef.push().getKey();
            MessageModel message = new MessageModel("audio", receiverId, senderId, uri1.toString(), DateUtility.getCurrentTimeStamp(), false);
            if (messageKey != null) {
                hideLoading();
                roomRef.child(messageKey).setValue(message);

                mViewModel.sendChatNotification(senderId, receiverId);
            }
        }));
    }

    private void retrieveAllMessages() {
        rootRef.child("Chat").child(roomId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //showProgress();
                if (dataSnapshot.exists()) {
                    MessageModel message = dataSnapshot.getValue(MessageModel.class);
                    mChatAdapter.messageList.add(message); //add new reply at the beginning of the list
                    binding.recyclerMessages.scrollToPosition(mChatAdapter.messageList.size() - 1);
                    mChatAdapter.notifyDataSetChanged();
                    hideLoading();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendTextMessage() {
        String messageText = binding.messageEditText.getText().toString();
        if (!TextUtils.isEmpty(messageText)) {
            DatabaseReference roomRef = rootRef.child("Chat").child(roomId);
            String messageKey = roomRef.push().getKey();
            MessageModel message = new MessageModel("text", receiverId, senderId, messageText, DateUtility.getCurrentTimeStamp(), false);
            if (messageKey != null) {
                roomRef.child(messageKey).setValue(message);
                binding.messageEditText.setText("");

                mViewModel.sendChatNotification(senderId, receiverId);
            }
        }
    }


    private void sendImgsMessage() {
        if (!mChatImgsAdapter.imagesList.isEmpty()) {
            for (Uri img : mChatImgsAdapter.imagesList) {
                showLoading();
                StorageReference mFileRef = storageReference.child(roomId).child(System.currentTimeMillis()
                        + "." + getFileExtension(img));
                //the nxt 3 lines are for reducing the image file size before upload.
                File imageFile = new File(ImageUtils.getRealPathFromURI(this, img));
                File reducedSizeImg = ImageUtils.reduceImageSize(imageFile);
                Uri finalImg = Uri.fromFile(reducedSizeImg);

                mFileRef.putFile(finalImg).addOnSuccessListener(taskSnapshot -> mFileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    DatabaseReference roomRef = rootRef.child("Chat").child(roomId);
                    String messageKey = roomRef.push().getKey();
                    MessageModel message = new MessageModel("image", receiverId, senderId, uri.toString(), DateUtility.getCurrentTimeStamp(), false);
                    if (messageKey != null) {
                        hideLoading();
                        roomRef.child(messageKey).setValue(message);
                        mChatImgsAdapter.imagesList.clear();
                        mChatImgsAdapter.notifyDataSetChanged();

                        mViewModel.sendChatNotification(senderId, receiverId);
                    }
                }));
            }
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                mChatImgsAdapter.imagesList.add(data.getData());
                mChatImgsAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setupMsgsRecycler() {
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(this);
        mChatLayoutManager.setStackFromEnd(true);
        binding.recyclerMessages.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(new ArrayList<>(), String.valueOf(senderId), this);
        binding.recyclerMessages.setAdapter(mChatAdapter);
    }


    private void setupImgsRecycler() {
        binding.recyclerImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mChatImgsAdapter = new ChatImagesAdapter(new ArrayList<>(), this);
        binding.recyclerImages.setAdapter(mChatImgsAdapter);
    }

    private void showVoiceOnly() {
        binding.messageEditText.setVisibility(View.GONE);
        binding.sendBtn.setVisibility(View.GONE);
        binding.locationLinkBtn.setVisibility(View.GONE);
        binding.imagesBtn.setVisibility(View.GONE);
        binding.slideToCancelText.setVisibility(View.VISIBLE);
        binding.arrowCancel.setVisibility(View.VISIBLE);
    }

    private void showAllButVoice() {
        binding.messageEditText.setVisibility(View.VISIBLE);
        binding.sendBtn.setVisibility(View.VISIBLE);
        binding.locationLinkBtn.setVisibility(View.VISIBLE);
        binding.imagesBtn.setVisibility(View.VISIBLE);
        binding.slideToCancelText.setVisibility(View.GONE);
        binding.arrowCancel.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareCurrentLocation();
            } else {
                try {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // now, user has denied permission (but not permanently!)
                        //                getUserLocation();
                        showMessage(R.string.location_is_required);
                    } else {

                        // now, user has denied permission permanently!

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "You have previously declined this permission.\n" +
                                "You must approve this permission in \"Permissions\" in the app settings on your device.", Snackbar.LENGTH_LONG)
                                .setAction(R.string.settings_menu,
                                        view -> startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID))));
                        snackbar.setText(getString(R.string.location_is_required) + "\n" + getString(R.string.go_to_app_settings));
                        View view = snackbar.getView();
                        TextView tv = view.findViewById(R.id.snackbar_text);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                        snackbar.show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == REQUEST_MIC_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                try {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.RECORD_AUDIO)) {
                        // now, user has denied permission (but not permanently!)
                        showMessage(R.string.mic_is_required);
                    } else {

                        // now, user has denied permission permanently!

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "You have previously declined this permission.\n" +
                                "You must approve this permission in \"Permissions\" in the app settings on your device.", Snackbar.LENGTH_LONG)
                                .setAction(R.string.settings_menu,
                                        view -> startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID))));
                        snackbar.setText(getString(R.string.mic_is_required) + "\n" + getString(R.string.go_to_app_settings));
                        View view = snackbar.getView();
                        TextView tv = view.findViewById(R.id.snackbar_text);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                        snackbar.show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
