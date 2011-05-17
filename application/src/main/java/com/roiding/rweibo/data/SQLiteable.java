package com.roiding.rweibo.data;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

public interface SQLiteable extends Serializable {
    public ContentValues getValues();

    public String getSQLiteTableName();

    public String getSQLiteTableSchema();

    public <T extends SQLiteable> T retrieve(Cursor c);
}
