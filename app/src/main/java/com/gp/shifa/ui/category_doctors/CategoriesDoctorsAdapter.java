package com.gp.shifa.ui.category_doctors;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gp.shifa.R;
import com.gp.shifa.data.models.CategoryDoctorsModel;
import com.gp.shifa.databinding.ItemEmptyViewBinding;
import com.gp.shifa.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesDoctorsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_DOCTORS = 1;

    private Callback mCallback;
    private List<CategoryDoctorsModel.MedicalsBean> mDoctorsList;

    public CategoriesDoctorsAdapter(List<CategoryDoctorsModel.MedicalsBean> list) {
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
            case VIEW_TYPE_DOCTORS:
                return new DoctorsViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDoctorsList != null && mDoctorsList.size() > 0) {
            return VIEW_TYPE_DOCTORS;
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

    public void addItems(List<CategoryDoctorsModel.MedicalsBean> list) {
        mDoctorsList.clear();
        mDoctorsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mDoctorsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {
        void onDoctorClick(int id);
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

        @BindView(R.id.userImageView)
        CircleImageView userImageView;
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.tvRateScore)
        TextView tvRateScore;
        @BindView(R.id.tvDesc)
        TextView tvDesc;
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

            CategoryDoctorsModel.MedicalsBean medicalsBean = mDoctorsList.get(position);

            Glide.with(itemView).load(medicalsBean.getDoctor().getImgSrc() + "/" +
                            medicalsBean.getDoctor().getImg())
                    .error(R.drawable.ic_doctor)
                    .placeholder(R.drawable.ic_doctor)
                    .into(userImageView);

            tvUsername.setText(medicalsBean.getDoctor().getTitle() + ". " + medicalsBean.getDoctor().getName());

            ratingBar.setRating(medicalsBean.getDoctor().getRating());
            tvRateScore.setText(medicalsBean.getDoctor().getRating() + "/5");
            tvDesc.setText(medicalsBean.getDoctor().getSpecialty());

            if (!medicalsBean.getDoctor().getMedicalsAreas().isEmpty())
                tvLocation.setText(medicalsBean.getDoctor().getMedicalsAreas().get(0).getArea().getName());

            itemView.setOnClickListener(v -> mCallback.onDoctorClick(medicalsBean.getDoctorId()));


        }
    }
}
