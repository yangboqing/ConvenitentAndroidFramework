package com.convenitent.framework.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yangboqing on 16/8/16.
 */
public final class ToastUtils {

    public static void $(Context context, String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void $(Context context, int stringId) {
        Toast.makeText(context.getApplicationContext(), stringId, Toast.LENGTH_SHORT).show();
    }
}
