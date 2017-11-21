package com.example.contentprovidertest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    /**
     * 定义一个Uri路径的匹配器，检查Uri是否合法，如果Uri不合法则匹配失败
     */

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int CONTACT_INFO_ALL = 1;

    public static final int CONTACT_INFO_SINGLE = 2;

    private MyDBOpenHelper helper;

    static {
        uriMatcher.addURI("com.example", "contactinfo", CONTACT_INFO_ALL);
        uriMatcher.addURI("com.example", "contactinfo/#", CONTACT_INFO_SINGLE);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = uriMatcher.match(uri);
        if (code == CONTACT_INFO_ALL) {
            SQLiteDatabase db = helper.getWritableDatabase();
            int count = db.delete("contactinfo", selection, selectionArgs);
            db.close();
            return count;
        } else {
            throw new IllegalArgumentException("Uri匹配错误");
        }
    }

    @Override
    public String getType(Uri uri) {
        int code = uriMatcher.match(uri);
        if (code == CONTACT_INFO_ALL) {
            return "vnd.android.cursor.dir/contact";
        } else if (code == CONTACT_INFO_SINGLE) {
            return "vnd.android.cursor.item/contact";
        } else {
            throw new IllegalArgumentException("Uri匹配错误");
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = uriMatcher.match(uri);
        if (code == CONTACT_INFO_ALL) {
            SQLiteDatabase db = helper.getWritableDatabase();
            long row = db.insert("contactinfo", null, values);
            db.close();
            return Uri.parse("content://com.example/contactinfo/" + row);
        } else {
            throw new IllegalArgumentException("Uri匹配错误");
        }

    }

    @Override
    public boolean onCreate() {
        helper = new MyDBOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //判断路径是否正确
        int code = uriMatcher.match(uri);
        if (code == CONTACT_INFO_ALL) {
            SQLiteDatabase db = helper.getReadableDatabase();
            return db.query("contactinfo", projection, selection, selectionArgs, null, null, sortOrder);
        } else if (code == CONTACT_INFO_SINGLE) {
            //content://com.example/contactinfo/1
            String path = uri.toString();
            SQLiteDatabase db = helper.getReadableDatabase();
            return db.query("contactinfo", projection, "id = ?", new String[]{path.substring(path.lastIndexOf("/") + 1)}, null, null, sortOrder);
        } else {
            throw new IllegalArgumentException("Uri匹配错误");
        }


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int code = uriMatcher.match(uri);
        if (code == CONTACT_INFO_ALL) {
            SQLiteDatabase db = helper.getWritableDatabase();
            int count = db.update("contactinfo", values, selection, selectionArgs);
            db.close();
            return count;
        } else {
            throw new IllegalArgumentException("Uri匹配错误");
        }
}
}
