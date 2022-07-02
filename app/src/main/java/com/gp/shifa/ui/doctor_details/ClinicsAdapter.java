package com.gp.shifa.ui.doctor_details;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.shifa.R;
import com.gp.shifa.data.models.DoctorDetailsModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClinicsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_CLINICS = 1;

    private Callback mCallback;
    private List<DoctorDetailsModel.MedicalsBean> mDoctorsList;

    public ClinicsAdapter(List<DoctorDetailsModel.MedicalsBean> list) {
        mDoctorsList = list;
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
            case VIEW_TYPE_CLINICS:
                return new DoctorsViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clinic, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDoctorsList != null && mDoctorsList.size() > 0) {
            return VIEW_TYPE_CLINICS;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mDoctorsList != null && mDoctorsList.size() > 0) {
            return mDoctorsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<DoctorDetailsModel.MedicalsBean> list) {
        mDoctorsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mDoctorsList.clear();
    }

    public interface Callback {
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
    public class DoctorsViewHolder extends BaseViewHolder {

        @BindView(R.id.tvSpec)
        TextView tvSpec;
        @BindView(R.id.tvPhone)
        TextView tvPhone;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvLocation)
        TextView tvLocation;

        public DoctorsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            DoctorDetailsModel.MedicalsBean medicalsBean = mDoctorsList.get(position);

            tvSpec.setText(itemView.getResources().getString(R.string.speciality) + ": " + medicalsBean.getCategory().getName());
            tvPhone.setText(medicalsBean.getPhone() + " - " + medicalsBean.getPhone2());
            tvLocation.setText(medicalsBean.getAddress());
            tvPrice.setText(itemView.getResources().getString(R.string.det_price) + " " +
                    medicalsBean.getDetectionPrice() + " " + itemView.getResources().getString(R.string.egp));

        }
    }
}
