package com.gp.health.ui.faqs;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.gp.health.R;
import com.gp.health.data.models.FAQsModel;
import com.gp.health.ui.base.BaseViewHolder;
import com.gp.health.utils.ImageUtils;
import com.gp.health.utils.LanguageHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FAQsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_FAQ = 1;
    private Callback mCallback;
    private List<FAQsModel> mFAQsList;

    public FAQsAdapter(List<FAQsModel> list) {
        mFAQsList = list;
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
            case VIEW_TYPE_FAQ:
                return new FAQViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFAQsList != null && mFAQsList.size() > 0) {
            return VIEW_TYPE_FAQ;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mFAQsList != null && mFAQsList.size() > 0) {
            return mFAQsList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<FAQsModel> list) {
        mFAQsList.clear();
        mFAQsList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mFAQsList.clear();
        notifyDataSetChanged();
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
    public class FAQViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_question)
        AppCompatTextView tvQuestion;
        @BindView(R.id.ll_expand_collapse)
        LinearLayout llExpandCollapse;
        @BindView(R.id.iv_expand_collapse)
        ImageView ivExpandCollapse;
        @BindView(R.id.tv_answer)
        AppCompatTextView tvAnswer;


        public FAQViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        protected void clear() {

        }

        @SuppressLint("SetTextI18n")
        public void onBind(int position) {

            FAQsModel faQsModel = mFAQsList.get(position);

            if (LanguageHelper.getLanguage(itemView.getContext()).equals("ar")) {
                tvQuestion.setText(faQsModel.getQuestion().getAr());
                tvAnswer.setText(faQsModel.getAnswer().getAr());
            } else {
                tvQuestion.setText(faQsModel.getQuestion().getEn());
                tvAnswer.setText(faQsModel.getAnswer().getEn());
            }

            llExpandCollapse.setOnClickListener(v -> {
                ImageUtils.rotate(ivExpandCollapse, tvAnswer.getVisibility() == View.VISIBLE ? 0 : 1);
                tvAnswer.setVisibility(tvAnswer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            });

        }
    }
}
