package com.pract.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * This class is using commons-httpclient
 * @author amit
 *
 */
public class HttpClientTest {
	
	private static HttpClient httpclient = new HttpClient();

	public static void main(String[] args) {
		for (int i = 0; i < 30; i++) {
			callEchoService();
		}
	}

	private static void callEchoService() {

		GetMethod httpget = new GetMethod("http://localhost:9595/echo");
		BufferedReader reader = null;
		try {
			httpclient.executeMethod(httpget);
			reader = new BufferedReader(
					new InputStreamReader(httpget.getResponseBodyAsStream(), httpget.getResponseCharSet()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			System.out.println(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			httpget.releaseConnection();
		}

	}

}
