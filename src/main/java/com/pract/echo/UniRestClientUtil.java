package com.pract.echo;

import akka.dispatch.Futures;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import scala.concurrent.Future;
import scala.concurrent.Promise;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections4.MapUtils;

public class UniRestClientUtil {

	/*static {
	    Unirest.setDefaultHeader("Content-Type", "application/json");
	    Unirest.setDefaultHeader("Connection", "Keep-Alive");
	  }*/

	public static CompletableFuture<String> postAsync(String requestURL, Map<String, Object> fields, Map<String, String> headers) throws UnirestException, IOException {
		Unirest.setDefaultHeader("Content-Type", "application/json");
	    Unirest.setDefaultHeader("Connection", "Keep-Alive");
	    Promise<String> promise = Futures.promise();
	    CompletableFuture<String>  completableFuture = new CompletableFuture<>();
	    HttpRequestWithBody requestWithBody = Unirest.post(requestURL);
	    if (MapUtils.isNotEmpty(headers)) {
	      requestWithBody.headers(headers);
	    }
	  
	    requestWithBody.fields(fields).asStringAsync(
	      new Callback<String>() {
	        @Override
	        public void completed(HttpResponse<String> httpResponse) {
	          //promise.success(httpResponse.getBody());
	          //promise.complete(Try<httpResponse.getBody()>);
	          completableFuture.complete(httpResponse.getBody());
	        }

	        @Override
	        public void failed(UnirestException e) {
	        	//completableFuture.exceptionally(e);//complete(httpResponse.getBody());
	          //promise.failure(e);
	        }

	        @Override
	        public void cancelled() {
	          //promise.failure(new Exception("cancelled"));
	        }
	      }
	    );
	    
	    return completableFuture;
	  }
	  
}
