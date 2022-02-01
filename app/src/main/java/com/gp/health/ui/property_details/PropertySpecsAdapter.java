package com.gp.health.ui.property_details;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.SpecOptionModel;
import com.gp.health.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertySpecsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_SPEC = 1;

    private Callback mCallback;
    private final List<SpecOptionModel> mSpecsList;

    public PropertySpecsAdapter(List<SpecOptionModel> list) {
        mSpecsList = list;
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
            case VIEW_TYPE_SPEC:
                return new PropertySpecViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property_spec, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mSpecsList != null && mSpecsList.size() > 0) {
            return VIEW_TYPE_SPEC;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mSpecsList != null && mSpecsList.size() > 0) {
            return mSpecsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<SpecOptionModel> list) {
        mSpecsList.clear();
        mSpecsList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(SpecOptionModel item) {
        mSpecsList.add(item);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mSpecsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

    }

    @SuppressLint("NonConstantResourceId")
    public class PropertySpecViewHolder extends BaseViewHolder {

        @BindView(R.id.llSpec)
        LinearLayout llSpec;
        @BindView(R.id.tv_spec_title)
        TextView tvSpecTitle;
        @BindView(R.id.tv_spec_value)
        TextView tvSpecValue;

        public PropertySpecViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {

            SpecOptionModel model = mSpecsList.get(position);

            if (position % 2 == 0) {
                llSpec.setBackgroundColor(itemView.getResources().getColor(R.color.colorGrayLight2));
            } else {
                llSpec.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            }

            if (model.getOptionTitle() != null)
                tvSpecTitle.setText(model.getOptionTitle());
            else
                tvSpecTitle.setText(itemView.getResources().getString(R.string.n_a));

            switch (model.getOptionType()) {
                case "text":
                case "number":

                    if (model.getOptionValue() != null)
                        tvSpecValue.setText(model.getOptionValue());
                    else
                        tvSpecValue.setText(itemView.getResources().getString(R.string.n_a));
                    break;

                case "select":

                    tvSpecValue.setText(model.getItemOptionValues().get(0).getOptionValueTitle());

                    if (model.getItemOptionValues() != null && !model.getItemOptionValues().isEmpty()
                            && model.getItemOptionValues().get(0).getOptionValueTitle() != null)
                        tvSpecValue.setText(model.getItemOptionValues().get(0).getOptionValueTitle());
                    else
                        tvSpecValue.setText(itemView.getResources().getString(R.string.n_a));

                    break;
                case "checkbox":

                    StringBuilder values = new StringBuilder();

                    for (int i = 0; i < model.getItemOptionValues().size(); i++) {

                        if (i == model.getItemOptionValues().size() - 1) {
                            values.append(model.getItemOptionValues().get(i).getOptionValueTitle());
                            tvSpecValue.setText(values);
                        } else {
                            values.append(model.getItemOptionValues().get(i).getOptionValueTitle()).append(", ");
                        }

                    }

                    break;
            }


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
