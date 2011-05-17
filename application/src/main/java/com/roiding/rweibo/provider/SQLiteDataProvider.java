package com.roiding.rweibo.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.roiding.rweibo.Constants;
import com.roiding.rweibo.data.Note;
import com.roiding.rweibo.data.OAuthUser;
import com.roiding.rweibo.data.SQLiteable;
import com.roiding.rweibo.data.User;
import com.roiding.rweibo.util.FileLocalCache;

public class SQLiteDataProvider {
    public static final String DB_NAME = Constants.CACHE_STORE_PATH + "/rweibo.db";
    public static final int DB_VERSION = 1;
    @SuppressWarnings("unused")
    private Context context;

    public SQLiteDataProvider(Context context) {
        this.context = context;
        FileLocalCache.checkDir();
    }

    public SQLiteDatabase getReadableDatabase() {
        return getDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return getDatabase();
    }

    public synchronized SQLiteDatabase getDatabase() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
        int version = db.getVersion();
        if (version != DB_VERSION) {
            db.beginTransaction();
            try {
                if (version == 0) {
                    onCreate(db);
                } else {
                    onUpgrade(db, version, DB_VERSION);
                }
                db.setVersion(DB_VERSION);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        return db;
    }

    private static final SQLiteable[] tables = new SQLiteable[] { new OAuthUser(), new User(), new Note() };

    public void onCreate(SQLiteDatabase db) {

        for (SQLiteable table : tables) {
            db.execSQL(table.getSQLiteTableSchema());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            default:
                break;
        }
        // for (SQLiteable table : tables) {
        // try {
        // db.execSQL("drop table " + table.getSQLiteTableName());
        // } catch (Exception e) {
        // }
        // }
        // onCreate(db);

    }

    public void insert(SQLiteable o) {
        if (o == null)
            return;

        SQLiteDatabase db = getWritableDatabase();
        try {
            // db.insertWithOnConflict(o.getSQLiteTableName(), null,
            // o.getValues(),
            // SQLiteDatabase.CONFLICT_REPLACE);
            db.insert(o.getSQLiteTableName(), null, o.getValues());
        } finally {
            db.close();
        }
    }


    public void update(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends SQLiteable> T get(String sql, T t) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        try {
            if (c.moveToNext()) {
                T tt = (T) t.retrieve(c);
                return tt;
            } else {
                return null;
            }
        } finally {
            c.close();
            db.close();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends SQLiteable> List<T> gets(String sql, T t) {
        List<T> list = new ArrayList<T>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        try {
            while (c.moveToNext()) {
                list.add((T) t.retrieve(c));
            }
        } finally {
            c.close();
            db.close();

        }

        return list;
    }
}
