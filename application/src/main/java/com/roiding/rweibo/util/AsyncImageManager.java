package com.roiding.rweibo.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class AsyncImageManager {
	private final static String TAG = "AsyncImaMgr";
	private final static ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(30, true);

	public static Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback, final Context activity) {
		try {
			// System.out.println(imageUrl);
			Drawable drawable = loadImageFromCache(imageUrl);
			if (drawable != null) {
				Log.d(TAG, "Cache Hit:" + imageUrl);
				imageCallback.imageLoaded(drawable, imageUrl);
				return drawable;
			}
			Log.d(TAG, "Cache Miss:" + imageUrl);

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message message) {
					if (imageCallback != null)
						imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
				}
			};

			queue.put(new Runnable() {
				public void run() {
					Log.d(TAG, "URL loading:" + imageUrl);

					Drawable drawable = loadImageFromUrl(imageUrl, activity);
					Log.d(TAG, "URL Hit:" + imageUrl + ", drawable=" + drawable);
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				}
			});

			beep();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static boolean threadIsRunning = false;

	public static void beep() {
		Log.i("AsyncImage", "threadIsRunning=" + threadIsRunning);
		if (threadIsRunning) {
			return;
		} else {
			threadIsRunning = true;
			new Thread() {
				@Override
				public void run() {
					try {
						Runnable r = null;
						while ((r = queue.poll()) != null) {
							r.run();
						}
					} finally {
						threadIsRunning = false;
					}
				}
			}.start();
		}
	}

	public static Drawable loadDrawable(String url, Activity activity) {
		Drawable d = loadImageFromCache(url);
		if (d == null) {
			d = loadImageFromUrl(url, activity);
		}
		return d;
	}

	public static Drawable loadImageFromCache(String url) {

		InputStream inputStream = FileLocalCache.load(url);
		if (inputStream != null) {
			return Drawable.createFromStream(inputStream, "src");
		} else {
			return null;
		}
	}

	public static Drawable loadImageFromUrl(String url, Context activity) {
		InputStream inputStream = null;
		try {
			inputStream = new URL(url).openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		inputStream = FileLocalCache.save(url, inputStream);

		Log.i("AsycImg", "inputStream=" + inputStream);
		Drawable d = Drawable.createFromStream(inputStream, "src");
		Log.i("AsycImg", "d=" + d);
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

	public static class DefaultImageCallback implements ImageCallback {
		private ImageView v;

		public DefaultImageCallback(ImageView v) {
			this.v = v;
		}

		public void imageLoaded(Drawable imageDrawable, String imageUrl) {
			Log.i(TAG, "imageLoaded:" + imageUrl + " Drawable=" + imageDrawable);
			if (v != null)
				v.setImageDrawable(imageDrawable);

		}
	}
}
