package com.pract.echo;

import java.awt.PageAttributes.MediaType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHeaders;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;

public class TestHttpClient {
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			
			getAdminAccessToken();
			
		}
	
		long end = System.currentTimeMillis();
		System.out.println("Total time taken : "+(end-start));
		
	}
	
	
	
	public static void getAdminAccessToken() throws Exception {
	
	    Map<String, String> headers = new HashMap<>();
	    headers.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
	    String url = "http://localhost:8080/auth/"
	      + "realms/"
	      + "master"
	      + "/protocol/openid-connect/token";

	    Map<String, Object> fields = new HashMap<>();
	    fields.put("client_id", "lms");
	    fields.put("client_secret", "5cf10633-c888-447b-b115-fa7776e0c7ff");
	    fields.put("grant_type", "client_credentials");
	    
	  
	    CompletableFuture<String> future = UniRestClientUtil.postAsync(url, fields, headers);
	    //Future<String> future = promise.future();
	    /*Timeout t = new Timeout(Duration.create(10, TimeUnit.SECONDS));
	    Object  obj = Await.result(future, t.duration());
	    
        System.out.println((String)obj);
        System.out.println(future.isCompleted());*/
        
	    
	    future.thenAcceptAsync((result)->{
          if (null != result && !result.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
              Map<String, Object> data = mapper.readValue(result, Map.class);
              System.out.println(data);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });	
	    
        
        System.out.println(future.isDone());
        
       
   	  }
}


