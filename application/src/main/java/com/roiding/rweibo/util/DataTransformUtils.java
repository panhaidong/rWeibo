package com.roiding.rweibo.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiding.rweibo.data.Note;
import com.roiding.rweibo.data.User;

public class DataTransformUtils {

    private static boolean getBoolean(JSONObject o, String name) throws JSONException {
        if (!o.isNull(name))
            return o.getBoolean(name);
        return false;
    }

    private static int getInt(JSONObject o, String name) throws JSONException {
        if (!o.isNull(name))
            return o.getInt(name);
        return 0;
    }

    private static long getLong(JSONObject o, String name) throws JSONException {
        if (!o.isNull(name))
            return o.getLong(name);
        return 0l;
    }

    private static long parseLong(JSONObject o, String name) throws JSONException {
        if (o.isNull(name))
            return 0l;
        else {
            try {
                return Long.parseLong(getString(o, name));
            } catch (Exception e) {
                return 0l;
            }
        }
    }

    @SuppressWarnings("unused")
    private static double parseDouble(JSONObject o, String name) throws JSONException {
        if (o.isNull(name))
            return 0d;
        else {
            try {
                return Double.parseDouble(getString(o, name));
            } catch (Exception e) {
                return 0d;
            }
        }
    }

    private static int parseInt(JSONObject o, String name) throws JSONException {
        if (o.isNull(name))
            return 0;
        else {
            try {
                return Integer.parseInt(getString(o, name));
            } catch (Exception e) {
                return 0;
            }
        }
    }

    private static String getString(JSONObject o, String name) throws JSONException {
        if (!o.isNull(name))
            return o.getString(name);
        return "";
    }

    public static User getUser(String cx) throws JSONException {
        JSONObject j = new JSONObject(cx);
        return parseUser(j);
    }

    private static User parseUser(JSONObject j) throws JSONException {
        User u = new User();
        u.id = getInt(j, "id");
        u.name = getString(j, "name");
        u.screenName = getString(j, "screen_name");
        u.domain = getString(j, "domain");
        u.url = getString(j, "url");

        u.geoEnabled = getBoolean(j, "geo_enabled");
        u.followersCount = getInt(j, "followers_count");
        u.friendsCount = getInt(j, "friends_count");
        u.favouritesCount = getInt(j, "favourites_count");
        u.statusesCount = getInt(j, "statuses_count");
        u.province = getInt(j, "province");
        u.city = getInt(j, "city");
        u.description = getString(j, "description");
        u.verified = getBoolean(j, "verified");
        u.profileImageUrl = getString(j, "profile_image_url");

        u.createdAt = new Date(getString(j, "created_at")).getTime();
        u.location = getString(j, "location");

        return u;
    }

    // public static List<User> getUsersNearby(String cx) throws JSONException {
    // List<User> list = new ArrayList<User>();
    // JSONArray array = new
    // JSONObject(cx).getJSONObject("data").getJSONArray("users");
    //
    // for (int i = 0; i < array.length(); i++) {
    // if (array.get(i) instanceof JSONObject) {
    // JSONObject o = array.getJSONObject(i);
    // User u = new User();
    // u.setUserId(getInt(o, "user_id"));
    // u.setLongitude(parseDouble(o, "longitude"));
    // u.setLatitude(parseDouble(o, "latitude"));
    // list.add(u);
    // }
    // }
    //
    // return list;
    // }
    //
    // public static String getApkUrl(String cx, int curVersionCode) throws
    // JSONException {
    // JSONObject j = new JSONObject(cx).getJSONObject("data");
    // int versionCode = getInt(j, "version_code");
    // String apkUrl = getString(j, "apk_url");
    // DataService.APK_URL = apkUrl;
    // if (versionCode > curVersionCode) {
    // return apkUrl;
    // } else {
    // return null;
    // }
    // }
    //
    // public static List<Note> getMessages(String cx) throws JSONException {
    // List<Note> list = new ArrayList<Note>();
    // JSONArray array = new
    // JSONObject(cx).getJSONObject("data").getJSONArray("messages");
    //
    // for (int i = 0; i < array.length(); i++) {
    // JSONObject o = array.getJSONObject(i);
    //
    // Note note = new Note();
    // note.setNoteId(getLong(o, "id"));
    // note.setCreateTime(getLong(o, "created") * 1000);
    // note.setUserId(getInt(o, "user_id"));
    // note.setTargetId(UserUtils.getTargetId(note.getUserId(), getInt(o,
    // "target_id")));
    // note.setLongitude(parseDouble(o, "longitude"));
    // note.setLatitude(parseDouble(o, "latitude"));
    // note.setText(getString(o, "message"));
    // note.setDistance(parseDouble(o, "dis"));
    // list.add(note);
    // }
    //
    // return list;
    // }
    //
    // public static Note getMessage(String cx) throws JSONException {
    // JSONObject o = new JSONObject(cx).getJSONObject("data");
    //
    // Note note = new Note();
    // note.setNoteId(getLong(o, "id"));
    // note.setCreateTime(getLong(o, "created") * 1000);
    // note.setUserId(getInt(o, "user_id"));
    // note.setTargetId(UserUtils.getTargetId(note.getUserId(), getInt(o,
    // "target_id")));
    // note.setLongitude(parseDouble(o, "longitude"));
    // note.setLatitude(parseDouble(o, "latitude"));
    // note.setText(getString(o, "text"));
    // note.setDistance(parseDouble(o, "dis"));
    //
    // return note;
    // }
    //
    public static List<Note> getNotes(String cx) throws JSONException {
        List<Note> list = new ArrayList<Note>();
        JSONArray array = new JSONArray(cx);
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            list.add(parseNote(o));
        }

        return list;
    }

    public static Note getNote(String cx) throws JSONException {
        JSONObject o = new JSONObject(cx);
        Note note = parseNote(o);
        return note;
    }

    private static Note parseNote(JSONObject o) throws JSONException {
        Note n = new Note();
        n.id = getLong(o, "id");
        n.text = getString(o, "text");
        n.createdAt = new Date(getString(o, "created_at")).getTime();
        n.inReplyToScreenName = getString(o, "in_reply_to_screen_name");
        n.inReplyToStatusId = parseLong(o, "in_reply_to_status_id");
        n.inReplyToUserId = parseInt(o, "in_reply_to_user_id");
        n.source = getString(o, "source");
        n.isFavorited = getBoolean(o, "favorited");
        n.picBmiddle = getString(o, "bmiddle_pic");
        n.picThumbnail = getString(o, "thumbnail_pic");
        n.picOriginal = getString(o, "original_pic");


        JSONObject uo = o.getJSONObject("user");
        User u = parseUser(uo);
        n.userId = u.id;
        n.user = u;

        if (!o.isNull("retweeted_status")) {
            JSONObject fn = o.getJSONObject("retweeted_status");
            Note nf = parseNote(fn);
            n.forwardNoteId = nf.id;
            n.forwardNote = nf;
        }
        return n;
    }
}
