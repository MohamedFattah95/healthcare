package com.gp.health.ui.adding_ad.add_ad_details;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.SpecOptionModel;
import com.gp.health.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DynamicSpecsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_COMPLETELY_EMPTY = -1;
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_SELECT = 1;
    public static final int VIEW_TYPE_TEXT = 2;
    public static final int VIEW_TYPE_CHECKBOX = 3;

    private Callback mCallback;
    public List<SpecOptionModel> mSpecsList;


    public DynamicSpecsAdapter(List<SpecOptionModel> list) {
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
            case VIEW_TYPE_SELECT:
                return new SpecSelectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spec_select, parent, false));
            case VIEW_TYPE_TEXT:
                return new SpecTextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spec_text, parent, false));
            case VIEW_TYPE_CHECKBOX:
                return new SpecCheckboxViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spec_checkbox, parent, false));
            case VIEW_TYPE_COMPLETELY_EMPTY:
                return new CompletelyEmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_completely_empty_view, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mSpecsList != null && mSpecsList.size() > 0) {

            switch (mSpecsList.get(position).getOptionType()) {
                case "select":
                    return VIEW_TYPE_SELECT;
                case "text":
                case "number":
                    return VIEW_TYPE_TEXT;
                case "checkbox":
                    return VIEW_TYPE_CHECKBOX;
                default:
                    return VIEW_TYPE_COMPLETELY_EMPTY;
            }

        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mSpecsList != null && mSpecsList.size() > 0) {
            return mSpecsList.size();
        } else {
            return 0;
        }
    }

    public void addItems(List<SpecOptionModel> list) {
        mSpecsList.clear();
        mSpecsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mSpecsList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {

        void setOption(String key, String value);

        void removeOption(String key);
    }

    public static class CompletelyEmptyViewHolder extends BaseViewHolder {


        CompletelyEmptyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBind(int position) {

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

    @SuppressLint("NonConstantResourceId")
    public class SpecSelectViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_spec_title)
        TextView tvSpecTitle;
        @BindView(R.id.sp_select)
        Spinner spSelect;


        public SpecSelectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            SpecOptionModel model = mSpecsList.get(position);

            tvSpecTitle.setText(model.getOptionTitle());

            List<String> optionValues = new ArrayList<>();
            optionValues.add(itemView.getResources().getString(R.string.choose_value));

            for (int i = 0; i < model.getOptionValues().size(); i++) {
                optionValues.add(model.getOptionValues().get(i).getOptionValueTitle());
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            itemView.getContext(),
                            R.layout.spinner_item,
                            optionValues);

            spSelect.setAdapter(adapter);

            if (model.getSelectedOptionValue() != null) {

                for (int i = 0; i < model.getOptionValues().size(); i++) {
                    if (model.getOptionValues().get(i).getOptionValueId() == model.getSelectedOptionValue().getOptionValueId()) {
                        spSelect.setSelection(i + 1);

                        mCallback.setOption("options[" + model.getOptionId() + "][" + model.getOptionId() + "]",
                                String.valueOf(model.getSelectedOptionValue().getOptionValueId()));

                        mSpecsList.get(position).setSelectedOptionValue(model.getOptionValues().get(i));

                        break;
                    } else if (i == model.getOptionValues().size() - 1) {
                        spSelect.setSelection(0);

                        mCallback.removeOption("options[" + model.getOptionId() + "][" + model.getOptionId() + "]");

                        mSpecsList.get(position).setSelectedOptionValue(null);
                    }
                }
            } else {
                mSpecsList.get(position).setSelectedOptionValue(null);
            }

            spSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id) {

                    if (pos != 0) {
                        mCallback.setOption("options[" + model.getOptionId() + "][" + model.getOptionId() + "]",
                                String.valueOf(model.getOptionValues().get(pos - 1).getOptionValueId()));

                        mSpecsList.get(mSpecsList.indexOf(model)).setSelectedOptionValue(model.getOptionValues().get(pos - 1));
                    } else {
                        mCallback.removeOption("options[" + model.getOptionId() + "][" + model.getOptionId() + "]");
                        mSpecsList.get(mSpecsList.indexOf(model)).setSelectedOptionValue(null);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });

        }
    }

    @SuppressLint("NonConstantResourceId")
    public class SpecTextViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_spec_title)
        TextView tvSpecTitle;
        @BindView(R.id.etText)
        AppCompatEditText etText;


        public SpecTextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            SpecOptionModel model = mSpecsList.get(position);

            if (model.getOptionType().equalsIgnoreCase("text")) {
                etText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (model.getOptionType().equalsIgnoreCase("number")) {
                etText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                etText.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            tvSpecTitle.setText(model.getOptionTitle());
            etText.setHint(model.getOptionTitle());

            if (model.getOptionValue() != null) {
                etText.setText(model.getOptionValue());
                mCallback.setOption("options[" + model.getOptionId() + "]", model.getOptionValue());
                mSpecsList.get(position).setOptionValue(etText.getText().toString().trim());
            } else {
                etText.setText("");
                mCallback.removeOption("options[" + model.getOptionId() + "]");
                mSpecsList.get(position).setOptionValue(null);
            }


            etText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()) {
                        mCallback.removeOption("options[" + model.getOptionId() + "]");
                        try {
                            mSpecsList.get(mSpecsList.indexOf(mSpecsList.get(position))).setOptionValue(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mCallback.setOption("options[" + model.getOptionId() + "]", s.toString());
                        mSpecsList.get(mSpecsList.indexOf(mSpecsList.get(position))).setOptionValue(s.toString());
                    }
                }
            });


        }
    }

    @SuppressLint("NonConstantResourceId")
    public class SpecCheckboxViewHolder extends BaseViewHolder implements CheckboxAdapter.Callback {

        @BindView(R.id.tv_spec_title)
        TextView tvSpecTitle;
        @BindView(R.id.rvCheckbox)
        RecyclerView rvCheckbox;

        CheckboxAdapter checkboxAdapter;

        public SpecCheckboxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            SpecOptionModel model = mSpecsList.get(position);

            tvSpecTitle.setText(model.getOptionTitle());

            rvCheckbox.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                    LinearLayoutManager.VERTICAL, true));
            checkboxAdapter = new CheckboxAdapter(itemView.getContext(),
                    model.getOptionValues(), this);

            rvCheckbox.setAdapter(checkboxAdapter);

            setIsRecyclable(false);

        }

        @Override
        public void onCheckboxClick(int optionParentId, int optionValueId, boolean isChecked) {

            if (isChecked)
                mCallback.setOption("options[" + optionParentId + "][" + optionValueId + "]", String.valueOf(optionValueId));
            else
                mCallback.removeOption("options[" + optionParentId + "][" + optionValueId + "]");
        }
    }
}
