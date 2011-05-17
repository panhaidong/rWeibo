package com.roiding.rweibo.data;

import java.util.List;

import com.roiding.rweibo.provider.SQLiteDataProvider;

import android.content.ContentValues;
import android.database.Cursor;

public class OAuthUser implements SQLiteable {

	private static final long serialVersionUID = 1L;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	private String token;
	private String tokenSecret;
	private int userId;

	private static final String TABLE_OAUTH_USER = "oauth_user";
	private static final String FIELD_OAUTH_USER_TOKEN = "token";
	private static final String FIELD_OAUTH_USER_TOKEN_SECRET = "token_secret";
	private static final String FIELD_OAUTH_USER_USER_ID = "user_id";

	public String getSQLiteTableName() {
		return TABLE_OAUTH_USER;
	}

	public String getSQLiteTableSchema() {
		String sql = "CREATE TABLE " + TABLE_OAUTH_USER + " (" + FIELD_OAUTH_USER_TOKEN + " TEXT PRIMARY KEY, "
				+ FIELD_OAUTH_USER_TOKEN_SECRET + " TEXT, " + FIELD_OAUTH_USER_USER_ID + " INTEGER " + " )";
		return sql;
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();

		values.put(FIELD_OAUTH_USER_TOKEN, token);
		values.put(FIELD_OAUTH_USER_TOKEN_SECRET, tokenSecret);
		values.put(FIELD_OAUTH_USER_USER_ID, userId);

		return values;

	}

	@SuppressWarnings("unchecked")
	public OAuthUser retrieve(Cursor c) {
		OAuthUser o = new OAuthUser();
		o.setToken(c.getString(c.getColumnIndexOrThrow(FIELD_OAUTH_USER_TOKEN)));
		o.setTokenSecret(c.getString(c.getColumnIndexOrThrow(FIELD_OAUTH_USER_TOKEN_SECRET)));
		o.setUserId(c.getInt(c.getColumnIndexOrThrow(FIELD_OAUTH_USER_USER_ID)));
		return o;
	}

	public static List<OAuthUser> getUsers(SQLiteDataProvider sqlite) {
		String sql = String.format("select * from %s", TABLE_OAUTH_USER);
		return sqlite.gets(sql, new OAuthUser());
	}

}
