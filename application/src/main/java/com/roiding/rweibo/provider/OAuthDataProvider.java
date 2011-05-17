package com.roiding.rweibo.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.roiding.rweibo.Constants;
import com.roiding.rweibo.Private;
import com.roiding.rweibo.data.Note;
import com.roiding.rweibo.data.OAuthUser;
import com.roiding.rweibo.data.User;
import com.roiding.rweibo.util.DataTransformUtils;
import com.roiding.rweibo.util.UserUtils;

public class OAuthDataProvider {

	private CommonsHttpOAuthConsumer consumer;
	private OAuthProvider provider;
	@SuppressWarnings("unused")
	private Context mContext;
	private String apiHost;

	public OAuthDataProvider(Context context) {
		this.mContext = context;
		apiHost = Private.OAUTH_API_HOST;
		consumer = new CommonsHttpOAuthConsumer(Private.APP_KEY,
				Private.APP_SECRET);
		provider = new CommonsHttpOAuthProvider(
				Private.OAUTH_REQUEST_TOKEN_URL,
				Private.OAUTH_ACCESS_TOKEN_URL, Private.OAUTH_AUTHORIZATION_URL);
		provider.setOAuth10a(true);

	}

	public String getAuthUrl() throws OAuthMessageSignerException,
			OAuthNotAuthorizedException, OAuthExpectationFailedException,
			OAuthCommunicationException {
		System.out.println("getAuthUrl()...");
		String u = provider.retrieveRequestToken(consumer,
				Private.OAUTH_CALLBACK_URL);
		System.out.println("getAuthUrl() u=" + u);
		return u;
	}

	public OAuthUser retrieveAccessToken(String verifier)
			throws OAuthMessageSignerException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, JSONException, IOException {

		System.out.println("Verifier    : " + verifier);

		provider.retrieveAccessToken(consumer, verifier);

		System.out.println("Access token: " + consumer.getToken());
		System.out.println("Token secret: " + consumer.getTokenSecret());

		User user = getVerifiedAccount();
		OAuthUser oau = new OAuthUser();
		oau.setToken(consumer.getToken());
		oau.setTokenSecret(consumer.getTokenSecret());
		oau.setUserId(user.id);
		return oau;
	}

	public void setTokenWithSecret(String token, String tokenSecret) {
		consumer.setTokenWithSecret(token, tokenSecret);
	}

	public User getVerifiedAccount() throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, JSONException, IOException {
		String u = apiHost + "/account/verify_credentials.json";
		return DataTransformUtils.getUser(httpGet(u));
	}

	// public User getUser(int userId) throws OAuthMessageSignerException,
	// OAuthExpectationFailedException,
	// OAuthCommunicationException, ClientProtocolException, JSONException,
	// IOException {
	// String u = apiHost + "/user/getinfo.json?user_id=" + userId;
	// User user = DataTransformUtils.getUser(httpGet(u));
	// return user;
	// }
	//
	// public Note post(String text) throws OAuthMessageSignerException,
	// OAuthExpectationFailedException,
	// OAuthCommunicationException, ClientProtocolException, IOException {
	// String u = apiHost + "/timeline/posttext.json?text=" + text;
	// // return DataTransformUtils.getNotes(fetch(u));
	// httpPost(u);
	// return null;
	// }

	private synchronized String httpGet(String url)
			throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException {

		url = consumer.sign(url);
		Log.d(Constants.LOG_TAG, "HTTP_GET ==>" + url);
		HttpGet request = new HttpGet(url);
		return execute(request);
	}

	private synchronized String httpPost(String url)
			throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException {
		url = consumer.sign(url, "POST");
		Log.d(Constants.LOG_TAG, "HTTP_POST ==>" + url);
		return execute(UserUtils.buildHttpPost(url));
	}

	private String execute(HttpUriRequest request) throws IOException,
			ClientProtocolException, UnsupportedEncodingException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}

		Log.d(Constants.LOG_TAG, "HTTP_RESPONSE <==" + builder.toString());

		return builder.toString();
	}

	//
	// public void updateLocation(int userId, double longitude, double latitude)
	// throws ClientProtocolException,
	// IOException, OAuthMessageSignerException,
	// OAuthExpectationFailedException, OAuthCommunicationException {
	// String url = "http://120.132.134.75/?c=locations&f=update&user_id=" +
	// userId + "&longitude=" + longitude
	// + "&latitude=" + latitude;
	//
	// httpPost(url);
	// }
	//
	// public Note postMessage(int userId, int targetUserId, String text) throws
	// ClientProtocolException, IOException,
	// OAuthMessageSignerException, OAuthExpectationFailedException,
	// OAuthCommunicationException, JSONException {
	// String url = "http://120.132.134.75/?c=messages&f=post&user_id=" + userId
	// + "&target_id=" + targetUserId
	// + "&text=" + URLEncoder.encode(text);
	// String s = httpGet(url);
	// Note note = DataTransformUtils.getMessage(s);
	// return note;
	// }
	//
	// public List<User> getNearbyUsers(int user_id, long distance) throws
	// ClientProtocolException, IOException,
	// OAuthMessageSignerException, OAuthExpectationFailedException,
	// OAuthCommunicationException, JSONException {
	//
	// String url = "http://120.132.134.75/?c=users&f=nearby&user_id=" +
	// user_id;
	// if (distance > 0) {
	// url += "&distance=" + distance;
	// }
	//
	// String s = httpGet(url);
	//
	// List<User> list = DataTransformUtils.getUsersNearby(s);
	//
	// return list;
	//
	// }
	//
	// public List<Note> getMessages(int user_id, long start_id, int count)
	// throws OAuthMessageSignerException,
	// OAuthExpectationFailedException, OAuthCommunicationException,
	// ClientProtocolException, JSONException,
	// IOException {
	// String url = "http://120.132.134.75/?c=messages&f=mlist&user_id=" +
	// user_id + "&start_id=" + start_id
	// + "&count=" + count;
	//
	// String s = httpGet(url);
	//
	// List<Note> list = DataTransformUtils.getMessages(s);
	// // Collections.reverse(list);
	// return list;
	// }
	//
	// public String getApkUrl(int curVersionCode) throws
	// OAuthMessageSignerException, OAuthExpectationFailedException,
	// OAuthCommunicationException, ClientProtocolException, IOException,
	// JSONException {
	// String url = "http://120.132.134.75/apps/last";
	// String s = httpGet(url);
	// Log.d("HTTP_GET", s);
	//
	// String apkUrl = DataTransformUtils.getApkUrl(s, curVersionCode);
	// return apkUrl;
	//
	// }

	public List<Note> getFriendsTimeline() throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException, JSONException {
		String u = apiHost + "/statuses/friends_timeline.json";
		String s = httpGet(u);
		return DataTransformUtils.getNotes(s);

	}

	public List<Note> getUserTimeline(int userId)
			throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException, JSONException {
		String u = apiHost + "/statuses/user_timeline.json?user_id=" + userId;
		String s = httpGet(u);
		return DataTransformUtils.getNotes(s);

	}

	public List<Note> getMentions() throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException, JSONException {
		String u = apiHost + "/statuses/mentions.json";
		String s = httpGet(u);
		return DataTransformUtils.getNotes(s);

	}

	public Note update(String text) throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException, JSONException {
		String u = apiHost + "/statuses/update.json?status=" + text;
		String s = httpPost(u);
		return DataTransformUtils.getNote(s);
	}

	public Note repost(String text, long noteId)
			throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException, JSONException {
		String u = apiHost + "/statuses/repost.json?id=" + noteId + "&status="
				+ text;
		String s = httpPost(u);
		return DataTransformUtils.getNote(s);
	}

	public Note comment(String text, long noteId)
			throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException, JSONException {
		String u = apiHost + "/statuses/comment.json?id=" + noteId
				+ "&comment=" + text;
		String s = httpPost(u);
		// return DataTransformUtils.getNote(s);
		// TODO
		return null;
	}
}
