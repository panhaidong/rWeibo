package com.roiding.rweibo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.Spanned;

public class UserUtils {
	public static enum USER_ICON_SPEC {
		_200x200, _120x120, _80x80
	};

	public static enum PHOTO_SPEC {
		middle, thumbnail, original
	};

	public static String getIcon(String iconUrl) {
		if (iconUrl == null || iconUrl.length() < 1) {
			return "";
		}
		int lastIndex = iconUrl.lastIndexOf("/");
		return iconUrl.substring(lastIndex + 1, iconUrl.length() - 4);
		// http://static1.139js.com/system/avatar/1505087/80x80/1293434332946159.jpg

	}

	public static String getPhoto(String photoUrl) {
		if (photoUrl == null || photoUrl.length() < 1) {
			return "";
		}
		int lastIndex = photoUrl.lastIndexOf("/");
		return photoUrl.substring(lastIndex + 1, photoUrl.length() - 4);
		// http://fs0.139js.com/file/Cwv58.jpg
	}

	public static SimpleDateFormat sdf = new SimpleDateFormat("M月dd日");
	public static SimpleDateFormat sdf_complete = new SimpleDateFormat("M月d日H时m分");

	public static String getTime(long time) {
		long timeSpace = (System.currentTimeMillis() - time) / 1000;
		if (timeSpace < 60) {
			return "刚刚";
		} else if (timeSpace < 60 * 60) {
			return ((int) (timeSpace / 60)) + "分钟前";
		} else if (timeSpace < 24 * 60 * 60) {
			return ((int) (timeSpace / 60 / 60)) + "小时前";
		} else {
			return sdf.format(new Date(time));
		}
	}
	public static String getCompleteTime(long time) {
		return sdf_complete.format(new Date(time));
	}
	public static String getIconUrl(int userId, USER_ICON_SPEC spec, String icon) {
		// TODO
		switch (spec) {
		case _200x200:
			return "http://static1.139js.com/system/avatar/" + userId + "/200x200/" + icon + ".jpg";
		case _120x120:
			return "http://static1.139js.com/system/avatar/" + userId + "/120x120/" + icon + ".jpg";
		case _80x80:
			return "http://static1.139js.com/system/avatar/" + userId + "/80x80/" + icon + ".jpg";

		default:
			return "";
		}

	}

	public static String getPhotoUrl(PHOTO_SPEC spec, String photo) {
		// TODO
		switch (spec) {
		case middle:
			return "http://fs0.139js.com/file/" + photo + "_470x7200.jpg";
		case thumbnail:
			return "http://fs0.139js.com/file/" + photo + "_120x90.jpg";
		case original:
			return "http://fs0.139js.com/file/" + photo + ".jpg";

		default:
			return "";
		}

	}

	public static Spanned getFormattedText(String t) {
		return Html.fromHtml(t);
	}

	private static class MyImageGetter implements ImageGetter {
		private Context context;

		public MyImageGetter(Context context) {
			this.context = context;
		}

		public Drawable getDrawable(String source) {
			System.out.println("imageGetter:" + source);
			Drawable drawFromPath;
			int path = context.getResources().getIdentifier("icon", "drawable", "com.i139.mp.client");
			drawFromPath = (Drawable) context.getResources().getDrawable(path);
			drawFromPath.setBounds(0, 0, drawFromPath.getIntrinsicWidth(), drawFromPath.getIntrinsicHeight());
			return drawFromPath;
		}
	}

	private static class MyTagHandler implements TagHandler {

		public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
			System.out.println(opening + "," + tag);
			if (tag.equalsIgnoreCase("mytag")) {
				if (!opening) {
					output.append("<img src=\"fuckss\">");
				}

			}

		}
	}

	public static String connect(List<? extends Number> list) {
		if (list == null || list.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (Number i : list) {
			sb.append(i);
			sb.append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	public static HttpPost buildHttpPost(String url){
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		String keyValues[] = url.substring(url.indexOf("?") + 1).split("&");
		int len = keyValues.length;
		for (int i = 0; i < len; i++) {
			String keyValue[] = keyValues[i].split("=");
			try {
				String value = "";
				if (keyValue.length == 2) {
					value = URLDecoder.decode(keyValue[1], "UTF-8");
				}
				formparams.add(new BasicNameValuePair(keyValue[0], value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		HttpPost request = new HttpPost(url.substring(0,url.indexOf("?")));
		try {
			UrlEncodedFormEntity initEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			request.setEntity(initEntity);
		} catch (UnsupportedEncodingException e) {
		}		
		return request;
	}

}
