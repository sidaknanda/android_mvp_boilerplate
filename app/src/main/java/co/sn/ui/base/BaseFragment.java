package co.sn.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Unbinder;
import co.sn.di.component.ActivityComponent;
import co.sn.utils.AppLogger;
import co.sn.utils.CommonUtils;
import co.sn.utils.ViewUtils;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;

public abstract class BaseFragment extends Fragment implements MvpView {

    private BaseActivity mActivity;
    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;
    private ViewGroup parentView;
    private boolean isInitialApiWorkDone; // used for implementing lazy load of fragment

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUi(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public boolean isLoading() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    @Override
    public void showSnackBarIndefinite(int resId, boolean showDismiss) {
        if (mActivity != null) {
            mActivity.showSnackBarIndefinite(resId, showDismiss);
        }
    }

    @Override
    public void showSnackBarIndefinite(String message, boolean showDismiss) {
        if (mActivity != null) {
            mActivity.showSnackBarIndefinite(message, showDismiss);
        }
    }

    @Override
    public void hideSnackBarIndefinite() {
        if (mActivity != null) {
            mActivity.hideSnackBarIndefinite();
        }
    }

    @Override
    public void onError(String message) {
        if (mActivity != null) {
            mActivity.onError(message);
        }
    }

    @Override
    public void showSnackBar(int resId) {
        if (mActivity != null) {
            mActivity.showSnackBar(resId);
        }
    }

    @Override
    public void showSnackBar(String message) {
        if (mActivity != null) {
            mActivity.showSnackBar(message);
        }
    }

    @Override
    public void showToast(int resId) {
        if (mActivity != null) {
            mActivity.showToast(resId);
        }
    }

    @Override
    public void showToast(String message) {
        if (mActivity != null) {
            mActivity.showToast(message);
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.onError(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public void showKeyboard() {
        if (mActivity != null) {
            mActivity.showKeyboard();
        }
    }

    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(int requestCode, PermissionEnum... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionManager.Builder()
                    .permission(permissions)
                    .callback((permissionsGranted, permissionsDenied, permissionsDeniedForever, permissionsAsked) -> {
                        onPermissionsResult(requestCode, permissionsGranted.size() == permissionsAsked.size());
                        onPermissionsResult(requestCode, permissionsGranted, permissionsDenied, permissionsDeniedForever, permissionsAsked);
                    })
                    .ask(this);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                PermissionUtils.isGranted(mActivity, PermissionEnum.fromManifestPermission(permission));
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    protected abstract void setupUi(View view);

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void enableTouch() {
        if (parentView != null) {
            ViewUtils.enableDisableTouch(parentView, true);
        } else {
            AppLogger.d("call setParentView() before using enable/disableTouch() methods");
        }
    }

    @Override
    public void disableTouch() {
        if (parentView != null) {
            ViewUtils.enableDisableTouch(parentView, false);
        } else {
            AppLogger.d("call setParentView() before using enable/disableTouch() methods");
        }
    }

    @Override
    public void setParentView(ViewGroup viewGroup) {
        this.parentView = viewGroup;
    }

    public boolean isInitialApiWorkDone() {
        return isInitialApiWorkDone;
    }

    public void setInitialApiWorkDone(boolean initialApiWorkDone) {
        isInitialApiWorkDone = initialApiWorkDone;
    }

    public void performApiBackgroundWork() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(this, requestCode, permissions, grantResults);
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    @Override
    public void onPermissionsResult(int requestCode, boolean isPermissionsGiven) {
    }

    @Override
    public void onPermissionsResult(int requestCode, ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
    }
}