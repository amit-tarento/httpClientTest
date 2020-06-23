package com.pract.echo;

import java.io.IOException;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpClientTest2 {
	private static CloseableHttpClient httpclient = null;
	static {
	//httpclient = HttpClients.createDefault();
	RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setSocketTimeout(60000)
            .setConnectTimeout(60000)
            .setConnectionRequestTimeout(60000)
            .setContentCompressionEnabled(true)
            .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
            .build(); 
	ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
	       public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
	        HeaderElementIterator it = new BasicHeaderElementIterator
	            (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
	        while (it.hasNext()) {
	            HeaderElement he = it.nextElement();
	            String param = he.getName();
	            String value = he.getValue();
	            if (value != null && param.equalsIgnoreCase
	               ("timeout")) {
	                return Long.parseLong(value) * 1000;
	            }
	        }
	        return 60 * 1000;
	    }
	};
	HttpClientConnectionManager poolingConnManager
	  = new PoolingHttpClientConnectionManager();
	httpclient = HttpClients.custom()
			.setConnectionManager(poolingConnManager)
            .useSystemProperties()
            .setKeepAliveStrategy(myStrategy)
		    .setDefaultRequestConfig(defaultRequestConfig)
            .setMaxConnPerRoute(2)
            .setMaxConnTotal(10)
            .build();
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 30; i++) {
			try {
				callEchoService();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void callEchoService() throws IOException {
		HttpGet httpGet = new HttpGet("http://localhost:9595/echo");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager. 
		try {
		    HttpEntity entity1 = response.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    EntityUtils.consume(entity1);
		    StatusLine sl = response.getStatusLine();
	        System.out.println("Response from echo service : "+sl.getStatusCode() +" - "+sl.getReasonPhrase());
		} finally {
		    response.close();
		}
	}

}
