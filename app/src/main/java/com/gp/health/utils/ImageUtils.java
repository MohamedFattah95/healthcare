package com.gp.health.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.jsibbold.zoomage.ZoomageView;
import com.gp.health.BuildConfig;
import com.gp.health.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ImageUtils {

    public static String getPath(Uri uri, Activity activity) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;

    }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        OutputStream out;
        File file = new File(getFilename(context));

        try {
            if (file.createNewFile()) {
                InputStream iStream = context.getContentResolver().openInputStream(contentUri);
                byte[] inputData = getBytes(iStream);
                out = new FileOutputStream(file);
                out.write(inputData);
                out.close();
                return file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private static String getFilename(Context context) {
        File mediaStorageDir = new File(context.getExternalFilesDir(""), "image_data");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".png";
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName;

    }


    public static File reduceImageSize(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }


    public static void loadImage(String imgUrl, ImageView imageView, Context context) {
        try {
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.img_back)
                    .error(R.drawable.img_back)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageDrawable(context.getDrawable(R.drawable.img_back));
        }
    }


    public static void loadImageLink(String name, ImageView imageView, Context context) {
        Glide.with(context)
                .load(BuildConfig.IMAGE_BASE_URL + name)
                .error(R.drawable.img_back)
                .placeholder(R.drawable.img_back)
                .into(imageView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void loadChatImage(String imgUrl, ImageView imageView, Context context) {
        try {
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.img_back)
                    .error(R.drawable.img_back)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageDrawable(context.getDrawable(R.drawable.img_back));
        }
    }

    public static void loadChatZoomImage(String name, ZoomageView imageView, Context context) {
        Glide.with(context)
                .load(name)
                .placeholder(R.drawable.img_back)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.color.colorBlack)
                .into(imageView);
    }

    public static void loadZoomImage(String name, ZoomageView imageView, Context context) {
        if (name.contains("storage/emulated"))
            Glide.with(context)
                    .load(name)
                    .placeholder(R.drawable.img_back)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.color.colorBlack)
                    .into(imageView);
        else if (!name.contains(BuildConfig.IMAGE_BASE_URL))
            Glide.with(context)
                    .load(BuildConfig.IMAGE_BASE_URL + name)
                    .placeholder(R.drawable.img_back)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.color.colorBlack)
                    .into(imageView);
        else
            Glide.with(context)
                    .load(name)
                    .placeholder(R.drawable.img_back)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.color.colorBlack)
                    .into(imageView);
    }

    public static void rotate(ImageView imageView, int factor) {
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(0.0f, -180.0f * factor,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(1000);
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);

        imageView.startAnimation(animSet);

    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @SuppressLint("LogNotTimber")
    public static Bitmap getBitmapFromLink(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.connect();
            } catch (Exception e) {
                Log.v("", e.getMessage());
            }
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.v("", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void showFrescoImage(Context context, List<String> images, int position) {
        if (context == null || images == null || images.isEmpty()) return;

        new ImageViewer.Builder<>(context, images)
                .hideStatusBar(false)
                .allowZooming(true)
                .setStartPosition(position)
                .allowSwipeToDismiss(true)
                .show();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public static void loadUserImage(String imgUrl, ImageView imageView, Context context) {
//        Glide.with(context)
//                .load(imgUrl)
//                .placeholder(R.drawable.ic_user_holder)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.drawable.ic_user_holder)
//                .into(imageView);

        Picasso.get()
                .load(imgUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_user_holder)
                .error(R.drawable.ic_user_holder)
                .into(imageView);
    }
}
