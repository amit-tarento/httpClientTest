package com.pract.echo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;

import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;
import scala.concurrent.Promise;

public class Practice2 {

	public static void main(String[] args) throws UnirestException {

		CompletableFuture<Integer> future
        = CompletableFuture.supplyAsync(() -> 0);

        future.thenApplyAsync(x -> x + 1) 
        .thenApplyAsync(x -> x + 1)
        .thenAccept(x -> System.out.println("async result: " + x));

        future.thenApply(x -> x + 1) 
        .thenApply(x -> x + 1)
        .thenAccept(x -> System.out.println("sync result:" + x));
      
	}
	
}
