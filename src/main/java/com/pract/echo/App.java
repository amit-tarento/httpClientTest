package com.pract.echo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

/**
 * Hello world!
 *
 */
public class App 
{
	private static HttpClient httpClient = null;
	public static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.89 Safari/537.36";
	private static RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setSocketTimeout(60000)
            .setConnectTimeout(60000)
            .setConnectionRequestTimeout(60000)
            .setContentCompressionEnabled(true)
            .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
            .build(); 
	static {
		System.setProperty("http.keepAlive", "true"); // ensure that the keep alive strategy is used when default is built
		System.setProperty("http.agent", USER_AGENT);
	}
	
	private static HttpClient getHttpClient() {	   
		
		
		if (null == httpClient) {
		     
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
		     httpClient = HttpClients.custom()
		                .useSystemProperties()
		                .setKeepAliveStrategy(myStrategy)
		    		    .setDefaultRequestConfig(defaultRequestConfig)
		                .setMaxConnPerRoute(20)
		                .setMaxConnTotal(50)
		                .build();
			//httpClient = HttpClients.createDefault();
		    }
		return httpClient;
	  }
	
    public static void main( String[] args )
    {
    	for (int i =0 ; i< 30 ; i++) {
    		callEchoService();
    	}
    }
    


	private static void callEchoService() {
		HttpGet httpGet = new HttpGet("http://localhost:9595/echo");
		
        httpGet.addHeader("content-type", "application/json");
        HttpResponse response;
		try {
			
			//CloseableHttpClient client = HttpClients.createDefault();
			//response = client.execute(httpGet);
			
			BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
			//HttpRoute route = new HttpRoute(new HttpHost("www.baeldung.com", 80));
			//ConnectionRequest connRequest = connManager.requestConnection(route, null);
			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();
			response = httpClient.execute(httpGet);
			StatusLine sl = response.getStatusLine();
	        System.out.println("Response from echo service : "+sl.getStatusCode() +" - "+sl.getReasonPhrase());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
