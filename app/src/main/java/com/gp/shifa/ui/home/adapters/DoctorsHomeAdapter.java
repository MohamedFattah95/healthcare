package com.gp.shifa.ui.home.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.shifa.R;
import com.gp.shifa.data.models.DoctorModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorsHomeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_DOCTOR = 1;

    private Callback mCallback;
    public List<DoctorModel> doctorModelList;

    public DoctorsHomeAdapter(List<DoctorModel> list) {
        doctorModelList = list;
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
            case VIEW_TYPE_DOCTOR:
                return new DoctorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_home, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (doctorModelList != null && doctorModelList.size() > 0) {
            return VIEW_TYPE_DOCTOR;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (doctorModelList != null && doctorModelList.size() > 0) {
            return doctorModelList.size();
        } else {
            return 0;
        }
    }

    public void addItems(List<DoctorModel> list) {
        doctorModelList.clear();
        doctorModelList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        doctorModelList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

        void onDoctorClick(int id);
    }

    @SuppressLint("NonConstantResourceId")
    public class DoctorViewHolder extends BaseViewHolder {

        @BindView(R.id.userImageView)
        ImageView userImageView;
        ;
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvRateCount)
        TextView tvRateCount;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        public DoctorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            DoctorModel model = doctorModelList.get(position);

            Glide.with(itemView).load(model.getImgSrc() + "/" +
                            model.getImg())
                    .error(R.drawable.ic_doctor)
                    .placeholder(R.drawable.ic_doctor)
                    .into(userImageView);

            tvUsername.setText(model.getTitle() + " " + model.getName());
            ratingBar.setRating(model.getRating());
            tvRateCount.setText(model.getRating() + "/5");

            itemView.setOnClickListener(v -> mCallback.onDoctorClick(model.getId()));


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
