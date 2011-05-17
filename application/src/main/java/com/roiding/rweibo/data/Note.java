package com.roiding.rweibo.data;

import android.content.ContentValues;
import android.database.Cursor;

public class Note implements SQLiteable {

    private static final long serialVersionUID = 1L;

    public long id;
    public long createdAt;
    public String text;
    public String source;
    public long inReplyToStatusId;
    public int inReplyToUserId;
    public String inReplyToScreenName;
    public boolean isFavorited;
    public double latitude = -1;
    public double longitude = -1;
    public String picThumbnail;
    public String picBmiddle;
    public String picOriginal;

    public int userId;
    public User user;

    public long forwardNoteId;
    public Note forwardNote;

    public long lastUpdatedAt;

    private static final String TABLE_NOTE = "note";

    private static final String FIELD_NOTE_ID = "id";
    private static final String FIELD_NOTE_CREATED_AT = "created_at";
    private static final String FIELD_NOTE_TEXT = "content";
    private static final String FIELD_NOTE_SOURCE = "source";
    private static final String FIELD_NOTE_INREPLYTO_STASUS_ID = "in_reply_to_status_id";

    private static final String FIELD_NOTE_INREPLYTO_USER_ID = "in_reply_to_user_id";
    private static final String FIELD_NOTE_INREPLYTO_SCREEN_NAME = "in_reply_to_screen_name";
    private static final String FIELD_NOTE_FAVORITED = "favorited";
    private static final String FIELD_NOTE_LATITUDE = "latitude";
    private static final String FIELD_NOTE_LONGITUDE = "longitude";

    private static final String FIELD_NOTE_PIC_ORIGINAL = "original_pic";
    private static final String FIELD_NOTE_PIC_BMIDDLE = "bmiddle_pic";
    private static final String FIELD_NOTE_PIC_THUMBNAIL = "thumbnail_pic";
    private static final String FIELD_NOTE_USER_ID = "user_id";
    private static final String FIELD_NOTE_LAST_UPDATED_AT = "last_updated_at";

    @Override
    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(FIELD_NOTE_ID, id);
        values.put(FIELD_NOTE_CREATED_AT, createdAt);
        values.put(FIELD_NOTE_TEXT, text);
        values.put(FIELD_NOTE_SOURCE, source);
        values.put(FIELD_NOTE_INREPLYTO_STASUS_ID, inReplyToStatusId);

        values.put(FIELD_NOTE_INREPLYTO_USER_ID, inReplyToUserId);
        values.put(FIELD_NOTE_INREPLYTO_SCREEN_NAME, inReplyToScreenName);
        values.put(FIELD_NOTE_FAVORITED, isFavorited);
        values.put(FIELD_NOTE_LATITUDE, latitude);
        values.put(FIELD_NOTE_LONGITUDE, longitude);

        values.put(FIELD_NOTE_PIC_ORIGINAL, picOriginal);
        values.put(FIELD_NOTE_PIC_BMIDDLE, picBmiddle);
        values.put(FIELD_NOTE_PIC_THUMBNAIL, picThumbnail);
        values.put(FIELD_NOTE_USER_ID, userId);
        values.put(FIELD_NOTE_LAST_UPDATED_AT, System.currentTimeMillis());
        return values;
    }

    @Override
    public String getSQLiteTableName() {
        return TABLE_NOTE;
    }

    @Override
    public String getSQLiteTableSchema() {
        String sql = "CREATE TABLE " + TABLE_NOTE + " (" + FIELD_NOTE_ID + " LONG PRIMARY KEY, "
                + FIELD_NOTE_CREATED_AT + " LONG, " + FIELD_NOTE_TEXT + " TEXT, " + FIELD_NOTE_SOURCE + " TEXT, "
                + FIELD_NOTE_INREPLYTO_STASUS_ID + " LONG, "

                + FIELD_NOTE_INREPLYTO_USER_ID + " INTEGER, " + FIELD_NOTE_INREPLYTO_SCREEN_NAME + " TEXT, "
                + FIELD_NOTE_FAVORITED + " BOOLEAN, " + FIELD_NOTE_LATITUDE + " DOUBLE , " + FIELD_NOTE_LONGITUDE
                + " DOUBLE, "

                + FIELD_NOTE_PIC_ORIGINAL + " TEXT, " + FIELD_NOTE_PIC_BMIDDLE + " TEXT, " + FIELD_NOTE_PIC_THUMBNAIL
                + " TEXT, " + FIELD_NOTE_USER_ID + " INTEGER, " + FIELD_NOTE_LAST_UPDATED_AT + " LONG " + ")";
        return sql;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Note retrieve(Cursor c) {
        Note o = new Note();
        o.id = (c.getLong(c.getColumnIndexOrThrow(FIELD_NOTE_ID)));
        o.createdAt = (c.getLong(c.getColumnIndexOrThrow(FIELD_NOTE_CREATED_AT)));
        o.text = (c.getString(c.getColumnIndexOrThrow(FIELD_NOTE_TEXT)));
        o.source = (c.getString(c.getColumnIndexOrThrow(FIELD_NOTE_SOURCE)));
        o.inReplyToStatusId = (c.getLong(c.getColumnIndexOrThrow(FIELD_NOTE_INREPLYTO_STASUS_ID)));

        o.inReplyToUserId = (c.getInt(c.getColumnIndexOrThrow(FIELD_NOTE_INREPLYTO_USER_ID)));
        o.inReplyToScreenName = (c.getString(c.getColumnIndexOrThrow(FIELD_NOTE_INREPLYTO_SCREEN_NAME)));
        o.isFavorited = (c.getShort(c.getColumnIndexOrThrow(FIELD_NOTE_FAVORITED)) == 1);
        o.latitude = (c.getDouble(c.getColumnIndexOrThrow(FIELD_NOTE_LATITUDE)));
        o.longitude = (c.getDouble(c.getColumnIndexOrThrow(FIELD_NOTE_LONGITUDE)));

        o.picOriginal = (c.getString(c.getColumnIndexOrThrow(FIELD_NOTE_PIC_ORIGINAL)));
        o.picBmiddle = (c.getString(c.getColumnIndexOrThrow(FIELD_NOTE_PIC_BMIDDLE)));
        o.picThumbnail = (c.getString(c.getColumnIndexOrThrow(FIELD_NOTE_PIC_THUMBNAIL)));
        o.userId = (c.getInt(c.getColumnIndexOrThrow(FIELD_NOTE_USER_ID)));
        o.lastUpdatedAt = (c.getLong(c.getColumnIndexOrThrow(FIELD_NOTE_LAST_UPDATED_AT)));

        return o;
    }

}
