package co.sn.ui.base;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.sn.di.component.ActivityComponent;
import rebus.permissionutils.PermissionEnum;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements MvpView {

    private BaseActivity mActivity;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void setParentView(ViewGroup viewGroup) {
    }

    @Override
    public void enableTouch() {
    }

    @Override
    public void disableTouch() {
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
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void requestPermissionsSafely(int requestCode, PermissionEnum... permissions) {
    }

    @Override
    public void onPermissionsResult(int requestCode, boolean isPermissionsGiven) {
    }

    @Override
    public void onPermissionsResult(int requestCode, ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
    }
}
