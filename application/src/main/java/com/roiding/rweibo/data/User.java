package com.roiding.rweibo.data;

import android.content.ContentValues;
import android.database.Cursor;


public class User implements SQLiteable {

    private static final long serialVersionUID = 1L;

    public int id;
    public String name;
    public String screenName;
    public String location;
    public int province;

    public int city;
    public String url;
    public String profileImageUrl;
    public String domain;
    public String gender;

    public int statusesCount;
    public int friendsCount;
    public int followersCount;
    public int favouritesCount;
    public long createdAt;

    public String description;
    public boolean geoEnabled;
    public boolean verified;
    public long lastUpdatedAt;

    private static final String TABLE_USER = "user";

    private static final String FIELD_USER_ID = "id";
    private static final String FIELD_USER_NAME = "name";
    private static final String FIELD_USER_SCREEN_NAME = "screen_name";
    private static final String FIELD_USER_LOCATION = "location";
    private static final String FIELD_USER_PROVINCE = "province";

    private static final String FIELD_USER_CITY = "city";
    private static final String FIELD_USER_URL = "url";
    private static final String FIELD_USER_PROFILE_IMAGE_URL = "profile_image_url";
    private static final String FIELD_USER_DOMAIN = "domain";
    private static final String FIELD_USER_GENDER = "gender";

    private static final String FIELD_USER_STATUSES_COUNT = "statuses_count";
    private static final String FIELD_USER_FRIENDS_COUNT = "friends_count";
    private static final String FIELD_USER_FOLLOWERS_COUNT = "followers_count";
    private static final String FIELD_USER_FAVOURITES_COUNT = "favourites_count";
    private static final String FIELD_USER_CREATED_AT = "created_at";

    private static final String FIELD_USER_DESCRIPTION = "description";
    private static final String FIELD_USER_GEO_ENABLED = "geo_enabled";
    private static final String FIELD_USER_VERIFIED = "verified";
    private static final String FIELD_USER_LAST_UPDATED_AT = "last_updated_at";



    @Override
    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(FIELD_USER_ID, id);
        values.put(FIELD_USER_NAME, name);
        values.put(FIELD_USER_SCREEN_NAME, screenName);
        values.put(FIELD_USER_LOCATION, location);
        values.put(FIELD_USER_PROVINCE, province);

        values.put(FIELD_USER_CITY, city);
        values.put(FIELD_USER_URL, url);
        values.put(FIELD_USER_PROFILE_IMAGE_URL, profileImageUrl);
        values.put(FIELD_USER_DOMAIN, domain);
        values.put(FIELD_USER_GENDER, gender);

        values.put(FIELD_USER_STATUSES_COUNT, statusesCount);
        values.put(FIELD_USER_FRIENDS_COUNT, friendsCount);
        values.put(FIELD_USER_FOLLOWERS_COUNT, followersCount);
        values.put(FIELD_USER_FAVOURITES_COUNT, favouritesCount);
        values.put(FIELD_USER_CREATED_AT, createdAt);

        values.put(FIELD_USER_DESCRIPTION, description);
        values.put(FIELD_USER_GEO_ENABLED, geoEnabled);
        values.put(FIELD_USER_VERIFIED, verified);
        values.put(FIELD_USER_LAST_UPDATED_AT, System.currentTimeMillis());
        return values;
    }

    @Override
    public String getSQLiteTableName() {
        return TABLE_USER;
    }

    @Override
    public String getSQLiteTableSchema() {
        String sql = "CREATE TABLE " + TABLE_USER + " (" + FIELD_USER_ID + " INTEGER PRIMARY KEY, " + FIELD_USER_NAME
                + " TEXT, " + FIELD_USER_SCREEN_NAME + " TEXT, " + FIELD_USER_LOCATION + " TEXT, "
                + FIELD_USER_PROVINCE + " INTEGER, "

                + FIELD_USER_CITY + " INTEGER, " + FIELD_USER_URL + " TEXT, " + FIELD_USER_PROFILE_IMAGE_URL
                + " TEXT, " + FIELD_USER_DOMAIN + " TEXT, " + FIELD_USER_GENDER + " TEXT, "

                + FIELD_USER_STATUSES_COUNT + " INTEGER, " + FIELD_USER_FRIENDS_COUNT + " INTEGER, "
                + FIELD_USER_FOLLOWERS_COUNT + " INTEGER, " + FIELD_USER_FAVOURITES_COUNT + " INTEGER, "
                + FIELD_USER_CREATED_AT + " LONG, "

                + FIELD_USER_DESCRIPTION + " TEXT, " + FIELD_USER_GEO_ENABLED + " BOOLEAN, " + FIELD_USER_VERIFIED
                + " BOOLEAN , " + FIELD_USER_LAST_UPDATED_AT + " LONG " + ")";
        return sql;
    }

    @SuppressWarnings("unchecked")
    @Override
    public User retrieve(Cursor c) {
        User o = new User();
        o.id = (c.getInt(c.getColumnIndexOrThrow(FIELD_USER_ID)));
        o.name = (c.getString(c.getColumnIndexOrThrow(FIELD_USER_NAME)));
        o.screenName = (c.getString(c.getColumnIndexOrThrow(FIELD_USER_SCREEN_NAME)));
        o.location = (c.getString(c.getColumnIndexOrThrow(FIELD_USER_LOCATION)));
        o.province = (c.getInt(c.getColumnIndexOrThrow(FIELD_USER_PROVINCE)));

        o.city = (c.getInt(c.getColumnIndexOrThrow(FIELD_USER_CITY)));
        o.url = (c.getString(c.getColumnIndexOrThrow(FIELD_USER_URL)));
        o.profileImageUrl = (c.getString(c.getColumnIndexOrThrow(FIELD_USER_PROFILE_IMAGE_URL)));
        o.domain = (c.getString(c.getColumnIndexOrThrow(FIELD_USER_DOMAIN)));
        o.gender = (c.getString(c.getColumnIndexOrThrow(FIELD_USER_GENDER)));

        o.statusesCount = (c.getInt(c.getColumnIndexOrThrow(FIELD_USER_STATUSES_COUNT)));
        o.friendsCount = (c.getInt(c.getColumnIndexOrThrow(FIELD_USER_FRIENDS_COUNT)));
        o.followersCount = (c.getInt(c.getColumnIndexOrThrow(FIELD_USER_FOLLOWERS_COUNT)));
        o.favouritesCount = (c.getInt(c.getColumnIndexOrThrow(FIELD_USER_FAVOURITES_COUNT)));
        o.createdAt = (c.getLong(c.getColumnIndexOrThrow(FIELD_USER_CREATED_AT)));

        o.description = (c.getString(c.getColumnIndexOrThrow(FIELD_USER_DESCRIPTION)));
        o.geoEnabled = (c.getShort(c.getColumnIndexOrThrow(FIELD_USER_GEO_ENABLED)) == 1);
        o.verified = (c.getShort(c.getColumnIndexOrThrow(FIELD_USER_VERIFIED)) == 1);
        o.lastUpdatedAt = (c.getLong(c.getColumnIndexOrThrow(FIELD_USER_LAST_UPDATED_AT)));
        return o;
    }
}
