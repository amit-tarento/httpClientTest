package com.pract.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UrlConnection {

	public static void main(String[] args) {
		for (int i = 0; i < 30; i++) {
			callEchoService();
		}

	}

	private static void callEchoService() {
		try {
			URL url = new URL("http://localhost:9595/echo");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(false);
			System.out.println(getResponse(con));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getResponse(HttpURLConnection httpURLConnection) throws IOException {
		InputStream inStream = null;
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try {
			inStream = httpURLConnection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return builder.toString();
	}

}
