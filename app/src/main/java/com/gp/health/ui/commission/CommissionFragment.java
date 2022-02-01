package com.gp.health.ui.commission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developers.imagezipper.ImageZipper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.gp.health.R;
import com.gp.health.data.models.BanksModel;
import com.gp.health.databinding.FragmentCommissionBinding;
import com.gp.health.di.component.FragmentComponent;
import com.gp.health.ui.base.BaseFragment;
import com.gp.health.utils.CommonUtils;
import com.gp.health.utils.ErrorHandlingUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommissionFragment extends BaseFragment<CommissionViewModel> implements CommissionNavigator, BanksAdapter.Callback {

    private static final int PICK_BILL_IMG = 1;
    File billImg = null;

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    BanksAdapter banksAdapter;

    FragmentCommissionBinding binding;

    String selectedTransferType = null;

    public static CommissionFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(BaseFragment.ARGS_INSTANCE, instance);
        CommissionFragment fragment = new CommissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCommissionBinding.inflate(inflater, container, false);
        setUp();

        return binding.getRoot();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        banksAdapter.setCallback(this);
    }

    public void refreshData() {

        showLoading();
        mViewModel.getBanks();

        binding.accountOwner.setText("");
        binding.accountNumber.setText("");
        binding.accountNumberInternational.setText("");

    }

    private void setupDatePicker() {
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            binding.commDateTV.setText(sdf.format(myCalendar.getTime()));
        };
        binding.commDateTV.setOnClickListener(view2 -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            //datePickerDialog.getDatePicker().setMinDate(Math.min(myCalendar.getTimeInMillis(), System.currentTimeMillis()));
            datePickerDialog.show();
        });
    }

    @SuppressLint("SetTextI18n")
    private void subscribeViewModel() {

        mViewModel.getBanksLiveData().observe(requireActivity(), response -> {
            hideLoading();

            if (response.getData() != null && !response.getData().isEmpty()) {
                response.getData().get(0).setSelected(true);
                banksAdapter.addItems(response.getData());
                binding.accountOwner.setText(response.getData().get(0).getWonerName());
                binding.accountNumber.setText(response.getData().get(0).getIbn());
                binding.accountNumberInternational.setText(response.getData().get(0).getAccountNo());
            }
        });

        mViewModel.getSubmitCommissionLiveData().observe(requireActivity(), response -> {
            hideLoading();
            clearViews();
            getBaseActivity().showNoteDialog(getString(R.string.done), getString(R.string.msg_comm), getActivity());
        });

        mViewModel.getCommissionCalcLiveData().observe(requireActivity(), response ->
                binding.tvDeservedCommission.setText(CommonUtils.formatCurrency(response.getData().getValue())));

    }


    private void setUp() {

        subscribeViewModel();
        setUpOnViewClicked();
        setupDatePicker();

        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvBanks.setLayoutManager(linearLayoutManager);
        binding.rvBanks.setAdapter(banksAdapter);

        mViewModel.getBanks();

        binding.etPriceCommission.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().isEmpty() && s.length() > 1) {
                    mViewModel.calculateCommission(s.toString());
                } else if (s.toString().isEmpty() || s.toString().length() == 1) {
                    binding.tvDeservedCommission.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void setUpOnViewClicked() {

        binding.rbBank.setOnClickListener(v -> selectedTransferType = "1");
        binding.rbElectronic.setOnClickListener(v -> selectedTransferType = "2");

        binding.billImageView.setOnClickListener(v -> ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(720, 720)
                .start(PICK_BILL_IMG));

        binding.sendBtn.setOnClickListener(v -> submitCommission());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_BILL_IMG) {
                billImg = new File(data.getData().getPath());
                Glide.with(this)
                        .load(billImg.getPath())
                        .placeholder(R.drawable.ic_camera01)
                        .error(R.drawable.ic_camera01)
                        .into(binding.billImageView);
            }

        }
    }

    private void submitCommission() {
        int error = FormValidator.validate(
                binding.commAmountET.getText().toString().trim(),
                binding.bankNameET.getText().toString().trim(),
                binding.commDateTV.getText().toString().trim(),
                binding.commNameET.getText().toString().trim(),
                billImg);
        if (error != -1) {
            showErrorMessage(getString(error));
            return;
        }

        if (selectedTransferType == null) {
            showMessage(R.string.choose_transfer_type);
            return;
        }

        showLoading();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart("user_id", mViewModel.getDataManager().getCurrentUserId() + "");
        builder.addFormDataPart("mobile", mViewModel.getDataManager().getUserObject().getMobile());
        builder.addFormDataPart("amount", binding.commAmountET.getText().toString().trim());
        builder.addFormDataPart("bank_name", binding.bankNameET.getText().toString().trim());
        builder.addFormDataPart("date", binding.commDateTV.getText().toString().trim());
        builder.addFormDataPart("name", mViewModel.getDataManager().getUserObject().getName());
        builder.addFormDataPart("transferer_name", binding.commNameET.getText().toString().trim());
        builder.addFormDataPart("note", binding.commNotesET.getText().toString().trim());

        builder.addFormDataPart("type", selectedTransferType);


        if (billImg != null) {
            RequestBody requestBody;
            try {
                requestBody = RequestBody.create(MediaType.parse("image/*"), new ImageZipper(getActivity()).compressToFile(billImg));
            } catch (IOException e) {
                e.printStackTrace();
                requestBody = RequestBody.create(MediaType.parse("image/*"), billImg);
            }

            builder.addFormDataPart("image", billImg.getName(), requestBody);
        }

        mViewModel.submitCommission(builder);
    }


    private void clearViews() {
        binding.commAmountET.setText(null);
        binding.bankNameET.setText(null);
        binding.commDateTV.setText(null);
        binding.commNameET.setText(null);
        binding.commNotesET.setText(null);
        billImg = null;
        binding.billImageView.setImageResource(R.drawable.ic_camera01);
    }


    @Override
    public void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }


    @Override
    public void handleError(Throwable throwable) {
        hideLoading();
        ErrorHandlingUtils.handleErrors(throwable);
    }

    @Override
    public void showMyApiMessage(String message) {
        hideLoading();
        showErrorMessage(message);
    }

    @Override
    public void onBankSelected(BanksModel model) {
        binding.accountOwner.setText(model.getWonerName());
        binding.accountNumber.setText(model.getIbn());
        binding.accountNumberInternational.setText(model.getAccountNo());

    }
}
