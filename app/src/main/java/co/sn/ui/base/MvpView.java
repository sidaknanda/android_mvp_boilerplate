package co.sn.ui.base;

import android.support.annotation.StringRes;
import android.view.ViewGroup;

import java.util.ArrayList;

import rebus.permissionutils.PermissionEnum;

public interface MvpView {

    void showLoading();

    void hideLoading();

    boolean isLoading();

    void setParentView(ViewGroup viewGroup);

    void enableTouch();

    void disableTouch();

    void showSnackBarIndefinite(int resId, boolean showDismiss);

    void showSnackBarIndefinite(String message, boolean showDismiss);

    void hideSnackBarIndefinite();

    void onError(@StringRes int resId);

    void onError(String message);

    void showSnackBar(@StringRes int resId);

    void showSnackBar(String message);

    void showToast(@StringRes int resId);

    void showToast(String message);

    boolean isNetworkConnected();

    void hideKeyboard();

    void showKeyboard();

    boolean hasPermission(String permission);

    void requestPermissionsSafely(int requestCode, PermissionEnum... permissions);

    void onPermissionsResult(int requestCode, boolean isPermissionsGiven);

    void onPermissionsResult(int requestCode, ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked);
}
