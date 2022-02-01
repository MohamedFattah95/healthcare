package com.gp.health.ui.chats;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.ChatRoomModel;
import com.gp.health.ui.base.BaseViewHolder;
import com.gp.health.utils.DateUtility;
import com.gp.health.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_CHAT = 1;

    private Callback mCallback;
    private final List<ChatRoomModel> mChatsList;

    public ChatsAdapter(List<ChatRoomModel> list) {
        mChatsList = list;
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
            case VIEW_TYPE_CHAT:
                return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatsList != null && mChatsList.size() > 0) {
            return VIEW_TYPE_CHAT;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mChatsList != null && mChatsList.size() > 0) {
            return mChatsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<ChatRoomModel> list) {
        mChatsList.clear();
        mChatsList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(ChatRoomModel chatRoomModel) {
        if (isInList(chatRoomModel)) {
            for (ChatRoomModel model : mChatsList) {
                if (model.getMessageDate().equals(chatRoomModel.getMessageDate())) {
                    model.setMessage(chatRoomModel.getMessage());
                    model.setIsSeen(chatRoomModel.isSeen());
                }
            }
        } else {
            mChatsList.add(chatRoomModel);
        }

        notifyDataSetChanged();
    }

    private boolean isInList(ChatRoomModel roomModel) {
        for (ChatRoomModel model : mChatsList) {
            if (roomModel.getMessageDate().equals(model.getMessageDate())) {
                return true;
            }
        }
        return false;
    }

    public List<ChatRoomModel> getList() {
        return mChatsList;
    }

    public void clearItems() {
        mChatsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {
        void openChat(ChatRoomModel roomModel);
    }

    public static class EmptyViewHolder extends BaseViewHolder {


        EmptyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBind(int position) {

        }


    }

    @SuppressLint("NonConstantResourceId")
    public class ChatViewHolder extends BaseViewHolder {

        @BindView(R.id.tvDate)
        AppCompatTextView tvDate;
        @BindView(R.id.civUser)
        CircleImageView civUser;
        @BindView(R.id.tvUsername)
        AppCompatTextView tvUsername;
        @BindView(R.id.tvMessage)
        AppCompatTextView tvMessage;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint({"SetTextI18n", "LogNotTimber"})
        public void onBind(int position) {
            ChatRoomModel roomModel = mChatsList.get(position);
            tvDate.setText(DateUtility.getDateTimeSpaceFormat(roomModel.getMessageDate(), itemView.getContext()));
            tvUsername.setText(roomModel.getUsername());
            ImageUtils.loadUserImage(roomModel.getUserImage(), civUser, itemView.getContext());


            if (roomModel.isLastMsgByYou()) {

                if (roomModel.getMessage().contains(".mp3")) {
                    tvMessage.setText(itemView.getResources().getString(R.string.you) + ": " + itemView.getResources().getString(R.string.voice_record));
                } else if (roomModel.getMessage().contains("http")) {
                    tvMessage.setText(itemView.getResources().getString(R.string.you) + ": " + itemView.getResources().getString(R.string.picture));
                } else {
                    tvMessage.setText(itemView.getResources().getString(R.string.you) + ": " + roomModel.getMessage());
                }

            } else {

                if (roomModel.getMessage().contains(".mp3")) {
                    tvMessage.setText(itemView.getResources().getString(R.string.voice_record));
                } else if (roomModel.getMessage().contains("http")) {
                    tvMessage.setText(itemView.getResources().getString(R.string.picture));
                } else {
                    tvMessage.setText(roomModel.getMessage());
                }
            }

            Log.e("TAG", "onBind: " + roomModel.isSeen());
            if (roomModel.isSeen()) {
                tvMessage.setTextColor(itemView.getContext().getResources().getColor(R.color.semi_gray));
                tvMessage.setTypeface(tvMessage.getTypeface(), Typeface.NORMAL);
            } else {
                tvMessage.setTextColor(itemView.getContext().getResources().getColor(R.color.colorBlack));
                tvMessage.setTypeface(tvMessage.getTypeface(), Typeface.BOLD);
            }


            itemView.setOnClickListener(v -> mCallback.openChat(roomModel));
        }
    }
}
