package com.roiding.rweibo.provider;

import java.io.IOException;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;

import com.roiding.rweibo.data.Note;
import com.roiding.rweibo.data.OAuthUser;


public class DataProvider {
    private static DataProvider instance;
    private SQLiteDataProvider sqlite;
    private OAuthDataProvider oauth;

    public static DataProvider getInstance(Context context) {
        if (instance == null) {
            instance = new DataProvider(context);
        }
        return instance;
    }

    public DataProvider(Context context) {
        sqlite = new SQLiteDataProvider(context);
        oauth = new OAuthDataProvider(context);
    }

    public String getAuthUrl() throws OAuthMessageSignerException, OAuthNotAuthorizedException,
            OAuthExpectationFailedException, OAuthCommunicationException {
        return oauth.getAuthUrl();
    }

    public OAuthUser retrieveAccessToken(String verifier) throws OAuthMessageSignerException,
            OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException,
            ClientProtocolException, JSONException, IOException {
        OAuthUser oUser = oauth.retrieveAccessToken(verifier);

        if (oUser != null && oUser.getUserId() > 0) {
            sqlite.insert(oUser);
        }

        return oUser;

    }

    public OAuthUser getVerifiedAccount() {
        List<OAuthUser> list = OAuthUser.getUsers(sqlite);
        if (list.size() == 0) {
            return null;
        } else {
            OAuthUser oau = list.get(0);
            System.out.println("INIT:" + oau.getToken() + "," + oau.getTokenSecret());
            oauth.setTokenWithSecret(oau.getToken(), oau.getTokenSecret());
            return oau;
        }
    }

    public List<Note> getFriendsTimeline() throws OAuthMessageSignerException, OAuthExpectationFailedException,
            OAuthCommunicationException, ClientProtocolException, JSONException, IOException {
        List<Note> notes = oauth.getFriendsTimeline();
        return notes;
    }

    public List<Note> getUserTimeline(int userId) throws OAuthMessageSignerException, OAuthExpectationFailedException,
            OAuthCommunicationException, ClientProtocolException, JSONException, IOException {
        List<Note> notes = oauth.getUserTimeline(userId);
        return notes;
    }

    public List<Note> getMentions() throws OAuthMessageSignerException, OAuthExpectationFailedException,
            OAuthCommunicationException, ClientProtocolException, JSONException, IOException {
        List<Note> notes = oauth.getMentions();
        return notes;
    }

    public Note update(String text) throws OAuthMessageSignerException, OAuthExpectationFailedException,
            OAuthCommunicationException, ClientProtocolException, JSONException, IOException {
        return oauth.update(text);
    }

    public Note repost(String text, long noteId) throws OAuthMessageSignerException, OAuthExpectationFailedException,
            OAuthCommunicationException, ClientProtocolException, JSONException, IOException {
        return oauth.repost(text,noteId);
    }

    public Note comment(String text, long noteId) throws OAuthMessageSignerException, OAuthExpectationFailedException,
            OAuthCommunicationException, ClientProtocolException, JSONException, IOException {
        return oauth.comment(text, noteId);
    }


}
