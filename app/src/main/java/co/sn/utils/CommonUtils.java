package co.sn.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import co.sn.app.R;
import rebus.permissionutils.PermissionEnum;

public final class CommonUtils {

    private static ProgressDialog progressDialog;

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static void showProgressDialog(Context context, String message, String title) {
        if (progressDialog != null) {
            dismissProgressDialog();
        }

        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            if (title != null) {
                progressDialog.setTitle(title);
            }
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        } catch (Exception e) {
            AppLogger.e("Progress Dialog", e.getMessage());
        }
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                AppLogger.e("Progress Dialog", e.getMessage());
            } finally {
                progressDialog = null;
            }
        }
        progressDialog = null;
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void showSnackBarLong(Context context, View view, String message) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView
                    .findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            snackbar.show();
        }
    }

    public static void showSnackBarShort(Context context, View view, String message) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView
                    .findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            snackbar.show();
        }
    }

    public static void showToastShort(Context context, String message) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String message) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static PermissionEnum[] getPermissionEnums(String... permissions) {
        PermissionEnum[] permissionEnums = new PermissionEnum[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            permissionEnums[i] = PermissionEnum.fromManifestPermission(permissions[i]);
        }
        return permissionEnums;
    }

    public static String getCurrentTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public static void copyToClipboard(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        try {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData
                        .newPlainText(
                                "copied text", text);
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callPhoneNumber(Context context, String phoneNo) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNo));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToastShort(context, "Error calling !!");
        }
    }

    public static void sendSms(Context context, String phoneNo) {
        try {
            Uri uri = Uri.parse("smsto:" + phoneNo);
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
            context.startActivity(smsIntent);
        } catch (Exception e) {
            e.printStackTrace();
            showToastShort(context, "Error sending SMS !!");
        }
    }

    public static void openMailTo(Context context, String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    private static Locale getIndiaLocale() {
        return new Locale("en", "IN");
    }
}
