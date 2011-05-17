package oauth.signpost.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import oauth.signpost.http.HttpRequest;

public class UrlStringRequestAdapter implements HttpRequest {

	private String url;
	private String method;

	public UrlStringRequestAdapter(String url) {
		this.url = url;
		this.method = "GET";
	}

	public UrlStringRequestAdapter(String url, String method) {
		this.url = url;
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public String getRequestUrl() {
		return url;
	}

	public void setRequestUrl(String url) {
		this.url = url;
	}

	public void setHeader(String name, String value) {
	}

	public String getHeader(String name) {
		return null;
	}

	public Map<String, String> getAllHeaders() {
		return Collections.emptyMap();
	}

	public InputStream getMessagePayload() throws IOException {
		return null;
	}

	public String getContentType() {
		return null;
	}

	public Object unwrap() {
		return url;
	}
}
