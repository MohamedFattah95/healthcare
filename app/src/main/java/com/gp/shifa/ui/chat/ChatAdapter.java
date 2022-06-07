package com.gp.shifa.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.gp.shifa.R;
import com.gp.shifa.data.models.MessageModel;
import com.gp.shifa.data.models.TimeModel;
import com.gp.shifa.ui.base.BaseViewHolder;
import com.gp.shifa.utils.DateUtility;
import com.gp.shifa.utils.ImageUtils;
import com.rygelouv.audiosensei.player.AudioSenseiPlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_TEXT = 1;
    public static final int VIEW_TYPE_IMAGE = 2;
    public static final int VIEW_TYPE_VOICE = 3;
    public static final int VIEW_TYPE_LOCATION = 4;

    public ArrayList<MessageModel> messageList;
    public String mReceiverImage = "";
    public String mReceiverName = "";
    public String mSenderImage = "";
    private final Context context;
    private final String currentUserId;

    public ChatAdapter(ArrayList<MessageModel> messageList, String currentUserId, Context context) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList != null && messageList.size() > 0) {
            switch (messageList.get(position).getType()) {
                case "text":
                    return VIEW_TYPE_TEXT;
                case "image":
                    return VIEW_TYPE_IMAGE;
                case "audio":
                    return VIEW_TYPE_VOICE;
                case "location":
                    return VIEW_TYPE_LOCATION;
                default:
                    return 0;
            }
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (messageList != null && messageList.size() > 0)
            return messageList.size();
        else
            return 1; //Empty Item
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TEXT:
                return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text, parent, false));
            case VIEW_TYPE_IMAGE:
                return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_image, parent, false));
            case VIEW_TYPE_VOICE:
                return new VoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_voice, parent, false));
            case VIEW_TYPE_LOCATION:
                return new LocationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_location, parent, false));
            default:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_empty, parent, false));
        }
    }


    private void goToMaps(String latLng) {
        String uri = "http://maps.google.com/maps?q=loc:" + latLng;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    private String getAudioDuration(String url) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(url, new HashMap<>());

        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMilliSec = Long.parseLong(time);
        long duration = timeInMilliSec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);

        if (String.valueOf(minutes).length() == 1 && String.valueOf(seconds).length() == 1)
            return "0" + minutes + ":" + "0" + seconds;
        else if (String.valueOf(minutes).length() == 1 && String.valueOf(seconds).length() != 1)
            return "0" + minutes + ":" + seconds;
        else if (String.valueOf(minutes).length() != 1 && String.valueOf(seconds).length() == 1)
            return minutes + ":" + "0" + seconds;
        else
            return minutes + ":" + seconds;
    }

    static class EmptyViewHolder extends BaseViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(int position) {

        }
    }

    @SuppressLint("NonConstantResourceId")
    class TextViewHolder extends BaseViewHolder {

        @BindView(R.id.receiverMessage)
        TextView receiverText;
        @BindView(R.id.senderMessage)
        TextView senderText;
        @BindView(R.id.layoutMsgSender)
        LinearLayout layoutMsgSender;
        @BindView(R.id.layoutMsgReceiver)
        LinearLayout layoutMsgReceiver;
        @BindView(R.id.receiverImage)
        CircleImageView receiverImage;
        @BindView(R.id.senderImage)
        CircleImageView senderImage;
        @BindView(R.id.tvDateSender)
        TextView tvDateSender;
        @BindView(R.id.tvDateReceiver)
        TextView tvDateReceiver;
        @BindView(R.id.ivCar)
        ImageView ivCar;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(itemView.getContext())
                    .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setDownsampleEnabled(true)
                    .build();
            Fresco.initialize(itemView.getContext(), config);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBind(int position) {
            MessageModel message = messageList.get(position);
            TimeModel timeModel = DateUtility.getTimeOnly(message.getTime(), itemView.getContext());
            if (message.getSenderId() == Integer.parseInt(currentUserId)) {
                layoutMsgSender.setVisibility(View.VISIBLE);
                layoutMsgReceiver.setVisibility(View.GONE);
                senderText.setText(message.getMessage());
                tvDateSender.setText(DateUtility.getDateOnly(message.getTime()) + " - " + timeModel.getTime() + " " + timeModel.getAmOrpm()
                        + " . " + itemView.getResources().getString(R.string.you));
                Glide.with(itemView)
                        .load(mSenderImage)
                        .error(R.drawable.ic_user_holder)
                        .placeholder(R.drawable.ic_user_holder)
                        .into(senderImage);
            } else {
                layoutMsgReceiver.setVisibility(View.VISIBLE);
                layoutMsgSender.setVisibility(View.GONE);
                receiverText.setText(message.getMessage());
                tvDateReceiver.setText(mReceiverName + " . " + DateUtility.getDateOnly(message.getTime()) + " - " + timeModel.getTime() + " " + timeModel.getAmOrpm());
                if (mReceiverImage.length() == 0) {
                    Glide.with(itemView)
                            .load(mReceiverImage)
                            .error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(receiverImage);

                } else {
                    Glide.with(itemView)
                            .load(mReceiverImage)
                            .error(R.drawable.ic_user_holder)
                            .placeholder(R.drawable.ic_user_holder)
                            .into(receiverImage);
                }
            }

            if (mReceiverImage.length() == 0) {
                ivCar.setVisibility(View.GONE);
            } else {
                ivCar.setVisibility(View.VISIBLE);
            }

            List<String> image = new ArrayList<>();
            if (mReceiverImage.length() != 0) {
                image.add(mReceiverImage);
            }
            receiverImage.setOnClickListener(v -> {
                if (mReceiverImage.length() != 0) {
                    ImageUtils.showFrescoImage(itemView.getContext(), image, 0);
                }
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    class ImageViewHolder extends BaseViewHolder {

        @BindView(R.id.senderMsgImage)
        ImageView senderMsgImage;
        @BindView(R.id.receiverMsgImage)
        ImageView receiverMsgImage;
        @BindView(R.id.layoutImgSender)
        LinearLayout layoutImgSender;
        @BindView(R.id.layoutImgReceiver)
        LinearLayout layoutImgReceiver;
        @BindView(R.id.receiverImage)
        CircleImageView receiverImage;
        @BindView(R.id.senderImage)
        CircleImageView senderImage;
        @BindView(R.id.tvDateSender)
        TextView tvDateSender;
        @BindView(R.id.tvDateReceiver)
        TextView tvDateReceiver;
        @BindView(R.id.ivCar)
        ImageView ivCar;

        public ImageViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(itemView.getContext())
                    .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setDownsampleEnabled(true)
                    .build();
            Fresco.initialize(itemView.getContext(), config);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBind(int position) {
            MessageModel message = messageList.get(position);
            TimeModel timeModel = DateUtility.getTimeOnly(message.getTime(), itemView.getContext());
            if (message.getSenderId() == Integer.parseInt(currentUserId)) {
                layoutImgSender.setVisibility(View.VISIBLE);
                layoutImgReceiver.setVisibility(View.GONE);
                ImageUtils.loadChatImage(message.getMessage(), senderMsgImage, context);
                tvDateSender.setText(DateUtility.getDateOnly(message.getTime()) + " - " + timeModel.getTime() + " " + timeModel.getAmOrpm()
                        + " . " + itemView.getResources().getString(R.string.you));
                Glide.with(itemView)
                        .load(mSenderImage)
                        .error(R.drawable.ic_user_holder)
                        .placeholder(R.drawable.ic_user_holder)
                        .into(senderImage);
            } else {
                layoutImgReceiver.setVisibility(View.VISIBLE);
                layoutImgSender.setVisibility(View.GONE);
                ImageUtils.loadChatImage(message.getMessage(), receiverMsgImage, context);
                tvDateReceiver.setText(mReceiverName + " . " + DateUtility.getDateOnly(message.getTime()) + " - " + timeModel.getTime() + " " + timeModel.getAmOrpm());
                if (mReceiverImage.length() == 0) {
                    Glide.with(itemView)
                            .load(mReceiverImage)
                            .error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(receiverImage);

                } else {
                    Glide.with(itemView)
                            .load(mReceiverImage)
                            .error(R.drawable.ic_user_holder)
                            .placeholder(R.drawable.ic_user_holder)
                            .into(receiverImage);
                }
            }

            senderMsgImage.setOnClickListener(view -> {

                List<String> images = new ArrayList<>();
                images.add(message.getMessage());

                if (images != null && !images.isEmpty()) {
                    ImageUtils.showFrescoImage(itemView.getContext(), images, 0);
                }

            });
            receiverMsgImage.setOnClickListener(view -> {

                List<String> images = new ArrayList<>();
                images.add(message.getMessage());

                if (images != null && !images.isEmpty()) {
                    ImageUtils.showFrescoImage(itemView.getContext(), images, 0);
                }

            });

            if (mReceiverImage.length() == 0) {
                ivCar.setVisibility(View.GONE);
            } else {
                ivCar.setVisibility(View.VISIBLE);
            }

            List<String> image = new ArrayList<>();
            if (mReceiverImage.length() != 0) {
                image.add(mReceiverImage);
            }
            receiverImage.setOnClickListener(v -> {
                if (mReceiverImage.length() != 0) {
                    ImageUtils.showFrescoImage(itemView.getContext(), image, 0);
                }
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    class VoiceViewHolder extends BaseViewHolder {

        @BindView(R.id.voiceSenderView)
        AudioSenseiPlayerView voiceSenderView;
        @BindView(R.id.voiceReceiverView)
        AudioSenseiPlayerView voiceReceiverView;
        @BindView(R.id.layoutVoiceSender)
        LinearLayout layoutVoiceSender;
        @BindView(R.id.layoutVoiceReceiver)
        LinearLayout layoutVoiceReceiver;
        @BindView(R.id.tvDateSender)
        TextView tvDateSender;
        @BindView(R.id.tvDateReceiver)
        TextView tvDateReceiver;
        @BindView(R.id.receiverImage)
        CircleImageView receiverImage;
        @BindView(R.id.senderImage)
        CircleImageView senderImage;
        @BindView(R.id.ivCar)
        ImageView ivCar;

        public VoiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(itemView.getContext())
                    .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setDownsampleEnabled(true)
                    .build();
            Fresco.initialize(itemView.getContext(), config);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBind(int position) {
            MessageModel message = messageList.get(position);
            TimeModel timeModel = DateUtility.getTimeOnly(message.getTime(), itemView.getContext());
            if (message.getSenderId() == Integer.parseInt(currentUserId)) {
                layoutVoiceSender.setVisibility(View.VISIBLE);
                layoutVoiceReceiver.setVisibility(View.GONE);
                voiceSenderView.setAudioTarget(message.getMessage());
                ((TextView) voiceSenderView.getPlayerRootView().findViewById(R.id.tv_duration))
                        .setText(getAudioDuration(message.getMessage()));
                tvDateSender.setText(DateUtility.getDateOnly(message.getTime()) + " - " + timeModel.getTime() + " " + timeModel.getAmOrpm()
                        + " . " + itemView.getResources().getString(R.string.you));
                Glide.with(itemView)
                        .load(mSenderImage)
                        .error(R.drawable.ic_user_holder)
                        .placeholder(R.drawable.ic_user_holder)
                        .into(senderImage);
            } else {
                layoutVoiceSender.setVisibility(View.GONE);
                layoutVoiceReceiver.setVisibility(View.VISIBLE);
                voiceReceiverView.setAudioTarget(message.getMessage());
                ((TextView) voiceReceiverView.getPlayerRootView().findViewById(R.id.tv_duration))
                        .setText(getAudioDuration(message.getMessage()));
                tvDateReceiver.setText(mReceiverName + " . " + DateUtility.getDateOnly(message.getTime()) + " - " + timeModel.getTime() + " " + timeModel.getAmOrpm());
                if (mReceiverImage.length() == 0) {
                    Glide.with(itemView)
                            .load(mReceiverImage)
                            .error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(receiverImage);

                } else {
                    Glide.with(itemView)
                            .load(mReceiverImage)
                            .error(R.drawable.ic_user_holder)
                            .placeholder(R.drawable.ic_user_holder)
                            .into(receiverImage);
                }
            }

            if (mReceiverImage.length() == 0) {
                ivCar.setVisibility(View.GONE);
            } else {
                ivCar.setVisibility(View.VISIBLE);
            }

            List<String> image = new ArrayList<>();
            if (mReceiverImage.length() != 0) {
                image.add(mReceiverImage);
            }
            receiverImage.setOnClickListener(v -> {
                if (mReceiverImage.length() != 0) {
                    ImageUtils.showFrescoImage(itemView.getContext(), image, 0);
                }
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    class LocationViewHolder extends BaseViewHolder {

        @BindView(R.id.senderMsgLocation)
        ImageView senderMsgLocation;
        @BindView(R.id.layoutLocationSender)
        LinearLayout layoutLocationSender;
        @BindView(R.id.receiverMsgLocation)
        ImageView receiverMsgLocation;
        @BindView(R.id.receiverImage)
        CircleImageView receiverImage;
        @BindView(R.id.senderImage)
        CircleImageView senderImage;
        @BindView(R.id.tvDateSender)
        TextView tvDateSender;
        @BindView(R.id.tvDateReceiver)
        TextView tvDateReceiver;
        @BindView(R.id.layoutLocationReceiver)
        LinearLayout layoutLocationReceiver;
        @BindView(R.id.ivCar)
        ImageView ivCar;

        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(itemView.getContext())
                    .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setDownsampleEnabled(true)
                    .build();
            Fresco.initialize(itemView.getContext(), config);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBind(int position) {
            MessageModel message = messageList.get(position);
            TimeModel timeModel = DateUtility.getTimeOnly(message.getTime(), itemView.getContext());
            if (message.getSenderId() == Integer.parseInt(currentUserId)) {
                layoutLocationSender.setVisibility(View.VISIBLE);
                layoutLocationReceiver.setVisibility(View.GONE);
                tvDateSender.setText(DateUtility.getDateOnly(message.getTime()) + " - " + timeModel.getTime() + " " +
                        timeModel.getAmOrpm() + " . " + itemView.getResources().getString(R.string.you));
                Glide.with(itemView)
                        .load(mSenderImage)
                        .error(R.drawable.ic_user_holder)
                        .placeholder(R.drawable.ic_user_holder)
                        .into(senderImage);
            } else {
                layoutLocationReceiver.setVisibility(View.VISIBLE);
                layoutLocationSender.setVisibility(View.GONE);
                tvDateReceiver.setText(mReceiverName + " . " + DateUtility.getDateOnly(message.getTime()) + " - " + timeModel.getTime() + " " + timeModel.getAmOrpm());
                if (mReceiverImage.length() == 0) {
                    Glide.with(itemView)
                            .load(mReceiverImage)
                            .error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(receiverImage);

                } else {
                    Glide.with(itemView)
                            .load(mReceiverImage)
                            .error(R.drawable.ic_user_holder)
                            .placeholder(R.drawable.ic_user_holder)
                            .into(receiverImage);
                }
            }
            senderMsgLocation.setOnClickListener(view -> goToMaps(message.getMessage()));
            receiverMsgLocation.setOnClickListener(view -> goToMaps(message.getMessage()));

            if (mReceiverImage.length() == 0) {
                ivCar.setVisibility(View.GONE);
            } else {
                ivCar.setVisibility(View.VISIBLE);
            }

            List<String> image = new ArrayList<>();
            if (mReceiverImage.length() != 0) {
                image.add(mReceiverImage);
            }
            receiverImage.setOnClickListener(v -> {
                if (mReceiverImage.length() != 0) {
                    ImageUtils.showFrescoImage(itemView.getContext(), image, 0);
                }
            });

        }
    }
}
