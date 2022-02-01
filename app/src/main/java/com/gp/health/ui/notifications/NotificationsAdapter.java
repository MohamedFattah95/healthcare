package com.gp.health.ui.notifications;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.NotificationsModel;
import com.gp.health.databinding.ItemEmptyViewBinding;
import com.gp.health.ui.base.BaseViewHolder;
import com.gp.health.utils.DateUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NOTIFICATION = 1;

    private Callback mCallback;
    private List<NotificationsModel> mNotificationsList;

    public NotificationsAdapter(List<NotificationsModel> list) {
        mNotificationsList = list;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NOTIFICATION:
                return new NotificationViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mNotificationsList != null && mNotificationsList.size() > 0) {
            return VIEW_TYPE_NOTIFICATION;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mNotificationsList != null && mNotificationsList.size() > 0) {
            return mNotificationsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<NotificationsModel> list) {
        mNotificationsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mNotificationsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {
        void onNotificationClicked(NotificationsModel model);
    }

    @SuppressLint("NonConstantResourceId")
    public class NotificationViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_notification_title)
        AppCompatTextView tvNotificationTitle;
        @BindView(R.id.tv_notification_date)
        AppCompatTextView tvNotificationDate;
        @BindView(R.id.ivNotification)
        ImageView ivNotification;
        @BindView(R.id.cv_notification)
        CardView cvNotification;


        public NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            NotificationsModel model = mNotificationsList.get(position);

            if (model.getReadAt() != null) {
                cvNotification.setCardBackgroundColor(ColorStateList.valueOf(itemView.getResources().getColor(R.color.white)));
            } else {
                cvNotification.setCardBackgroundColor(ColorStateList.valueOf(itemView.getResources().getColor(R.color.light_gray)));
            }

            tvNotificationTitle.setText(model.getData());
            tvNotificationDate.setText(DateUtility.getDateOnlyTFormat(model.getCreatedAt()));
            itemView.setOnClickListener(view -> mCallback.onNotificationClicked(model));
        }
    }

    public static class EmptyViewHolder extends BaseViewHolder {


        EmptyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBind(int position) {

        }


    }
}
