package co.sn.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import co.sn.app.R;

public final class ViewUtils {

    private ViewUtils() {
        // This utility class is not publicly instantiable
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void setImageWithDrawable(ImageView imageView, String imageUrl, Drawable drawable) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheOnDisk(true)
                .cacheInMemory(true)
                .build();
        if (drawable != null) {
            builder.showImageOnLoading(drawable);
            builder.showImageOnFail(drawable);
        } else {
            builder.showImageOnLoading(R.drawable.ic_launcher_foreground);
            builder.showImageOnFail(R.drawable.ic_launcher_foreground);
        }
        DisplayImageOptions displayImageOptions = builder.build();
        if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
            imageView.setImageDrawable(drawable);
        } else {
            ImageLoader.getInstance().displayImage(imageUrl, imageView, displayImageOptions);
        }
    }

    public static void enableDisableTouch(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableTouch((ViewGroup) view, enabled);
            }
        }
    }
}
