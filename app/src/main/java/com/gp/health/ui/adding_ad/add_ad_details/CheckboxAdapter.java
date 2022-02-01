package com.gp.health.ui.adding_ad.add_ad_details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.SpecOptionValueModel;
import com.gp.health.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckboxAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_CHECKBOX = 1;

    private Callback mCallback;
    private Context context;
    private List<SpecOptionValueModel> mCheckboxList;

    public CheckboxAdapter(Context context, List<SpecOptionValueModel> subAdd, Callback callback) {
        this.context = context;
        this.mCheckboxList = subAdd;
        mCallback = callback;
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

        return new CheckboxViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkbox, parent, false));
    }

    @Override
    public int getItemViewType(int position) {

        return VIEW_CHECKBOX;

    }

    @Override
    public int getItemCount() {
        return mCheckboxList.size();

    }

    public void addItems(List<SpecOptionValueModel> list) {
        mCheckboxList.clear();
        mCheckboxList.addAll(list);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onCheckboxClick(int optionParentId, int optionValueId, boolean isChecked);
    }

    public class CheckboxViewHolder extends BaseViewHolder {

        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.cbSpec)
        CheckBox cbSpec;

        public CheckboxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        public void onBind(int position) {

            SpecOptionValueModel model = mCheckboxList.get(position);

            cbSpec.setText(model.getOptionValueTitle());

            cbSpec.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (buttonView.isPressed()) {
                    mCallback.onCheckboxClick(model.getParentId(), model.getOptionValueId(), isChecked);
                }
            });

            cbSpec.setChecked(model.isSelected());
            mCallback.onCheckboxClick(model.getParentId(), model.getOptionValueId(), model.isSelected());

            setIsRecyclable(false);

        }
    }

}
