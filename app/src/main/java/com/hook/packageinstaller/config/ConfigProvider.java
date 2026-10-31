package com.hook.packageinstaller.config;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;

public final class ConfigProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if (ConfigKeys.METHOD_GET.equals(method)) {
            Bundle result = new Bundle();
            SharedPreferences preferences = prefs();
            for (String key : ConfigKeys.ALL) {
                result.putBoolean(key, preferences.getBoolean(key, true));
            }
            return result;
        }
        return super.call(method, arg, extras);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SharedPreferences preferences = prefs();
        MatrixCursor cursor = new MatrixCursor(new String[]{"key", "value"});
        for (String key : ConfigKeys.ALL) {
            cursor.addRow(new Object[]{key, preferences.getBoolean(key, true) ? 1 : 0});
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.com.hook.packageinstaller.config";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private SharedPreferences prefs() {
        Context context = getContext();
        if (context == null) {
            throw new IllegalStateException("ConfigProvider is not attached");
        }
        return context.getSharedPreferences(ConfigKeys.PREFS, Context.MODE_PRIVATE);
    }
}
