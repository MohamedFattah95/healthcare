package com.gp.health.ui.commission;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.gp.health.R;
import com.gp.health.data.models.BanksModel;
import com.gp.health.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BanksAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_BANK = 1;

    private Callback mCallback;
    private final List<BanksModel> mBanksList;

    public BanksAdapter(List<BanksModel> list) {
        mBanksList = list;
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
            case VIEW_TYPE_BANK:
                return new BankViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBanksList != null && mBanksList.size() > 0) {
            return VIEW_TYPE_BANK;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mBanksList != null && mBanksList.size() > 0) {
            return mBanksList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<BanksModel> list) {
        mBanksList.clear();
        mBanksList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mBanksList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {
        void onBankSelected(BanksModel model);
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
    public class BankViewHolder extends BaseViewHolder {

        @BindView(R.id.cvBankBg)
        CardView cvBankBg;
        @BindView(R.id.bankImg)
        RoundedImageView bankImg;

        public BankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            BanksModel model = mBanksList.get(position);

            Glide.with(itemView)
                    .load(model.getImage() + "")
                    .placeholder(R.drawable.img_back)
                    .error(R.drawable.img_back)
                    .into(bankImg);

            if (model.isSelected()) {
                cvBankBg.setCardBackgroundColor(itemView.getResources().getColor(R.color.green));
            } else {
                cvBankBg.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorGraySemiDark));
            }

            itemView.setOnClickListener(v -> {

                for (int i = 0; i < mBanksList.size(); i++) {
                    mBanksList.get(i).setSelected(false);
                    notifyItemChanged(i, mBanksList.get(i));
                }
                mBanksList.get(position).setSelected(true);
                notifyItemChanged(position, mBanksList.get(position));

                mCallback.onBankSelected(model);
            });

            setIsRecyclable(false);

        }
    }
}
