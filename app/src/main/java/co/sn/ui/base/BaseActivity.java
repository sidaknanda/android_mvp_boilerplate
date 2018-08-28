package co.sn.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import butterknife.Unbinder;
import co.sn.App;
import co.sn.app.R;
import co.sn.di.component.ActivityComponent;
import co.sn.di.component.DaggerActivityComponent;
import co.sn.di.module.ActivityModule;
import co.sn.utils.AppLogger;
import co.sn.utils.CommonUtils;
import co.sn.utils.NetworkUtils;
import co.sn.utils.ViewUtils;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;

public abstract class BaseActivity extends AppCompatActivity
        implements MvpView, BaseFragment.Callback {

    private ProgressDialog mProgressDialog;
    private Snackbar snackbar;
    private ActivityComponent mActivityComponent;
    private Unbinder mUnBinder;
    private ViewGroup parentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((App) getApplication()).getComponent())
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        setParentView((ViewGroup) getView());
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
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
                PermissionUtils.isGranted(this, PermissionEnum.fromManifestPermission(permission));
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
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
        showSnackBarIndefinite(getString(resId), showDismiss);
    }

    @Override
    public void showSnackBarIndefinite(String message, boolean showDismiss) {
        hideSnackBarIndefinite();
        snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, BaseTransientBottomBar.LENGTH_INDEFINITE);
        if (showDismiss) {
            snackbar.setAction("DISMISS", v -> hideSnackBarIndefinite());
        }
        snackbar.show();
    }

    @Override
    public void hideSnackBarIndefinite() {
        if (snackbar != null && snackbar.isShownOrQueued()) {
            snackbar.dismiss();
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void onError(String message) {
        if (message != null) {
            showSnackBarIndefinite(message, true);
        } else {
            showSnackBarIndefinite(getString(R.string.api_default_error), true);
        }
    }

    @Override
    public void showSnackBar(int resId) {
        showSnackBar(getString(resId));
    }

    @Override
    public void showSnackBar(String message) {
        CommonUtils.showSnackBarShort(this, findViewById(android.R.id.content), message);
    }

    @Override
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(String message) {
        CommonUtils.showToastShort(this, message);
    }

    @Override
    public void onPermissionsResult(int requestCode, boolean isPermissionsGiven) {
    }

    @Override
    public void onPermissionsResult(int requestCode, ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
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

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void onFragmentAttached() {
    }

    @Override
    public void onFragmentDetached(String tag) {
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void showKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(this, requestCode, permissions, grantResults);
    }

    private View getView() {
        return findViewById(android.R.id.content);
    }
}