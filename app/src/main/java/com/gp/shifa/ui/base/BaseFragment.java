
package com.gp.shifa.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gp.shifa.BaseApp;
import com.gp.shifa.di.component.DaggerFragmentComponent;
import com.gp.shifa.di.component.FragmentComponent;
import com.gp.shifa.di.module.FragmentModule;
import com.gp.shifa.utils.CommonUtils;

import javax.inject.Inject;

public abstract class BaseFragment<V extends BaseViewModel> extends Fragment {

    private BaseActivity mActivity;
    private ProgressDialog mProgressDialog;
    private View mRootView;

    protected static final String ARGS_INSTANCE = "com.qrc.CSC.argsInstance";
    FragmentNavigation fragmentNavigation;
    int mInt = 0;


    @Inject
    protected V mViewModel;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        } else if (context instanceof FragmentNavigation) {
            fragmentNavigation = (FragmentNavigation) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection(getBuildComponent());
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return mRootView;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(mActivity);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    public void showKeyboard() {
        if (mActivity != null) {
            mActivity.showKeyboard();
        }
    }

    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    public void openActivityOnTokenExpire() {
        if (mActivity != null) {
            mActivity.openActivityOnTokenExpire();
        }
    }

    public abstract void performDependencyInjection(FragmentComponent buildComponent);


    private FragmentComponent getBuildComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(((BaseApp) (getContext().getApplicationContext())).appComponent)
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    protected void showMessage(String m) {
        if (mActivity != null)
            mActivity.showMessage(m);
    }

    protected void showMessage(int id) {
        if (mActivity != null)
            mActivity.showMessage(id);
    }

    protected void showErrorMessage(String m) {
        if (mActivity != null)
            mActivity.showErrorMessage(m);
    }

    protected void showSuccessMessage(String m) {
        if (mActivity != null)
            mActivity.showSuccessMessage(m);
    }

    public interface FragmentNavigation {
        void pushFragment(Fragment fragment);
    }
}
