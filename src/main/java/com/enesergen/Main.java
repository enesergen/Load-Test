package com.enesergen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static String authToken;
    private static int threadCount;
    private static int batchSize;
    private static int totalRequestCount;
    private static  String targetUrl;
    private static String postData;
    private static final AtomicInteger requestCounter = new AtomicInteger(0);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws   JsonProcessingException {
        if(args.length<1){
            System.out.println("Usage: java -jar JarName.jar <target url>");
        }
        String configFilePath = args[0];
        loadConfig(configFilePath);
        createPostData(batchSize);
        runLoadTest();

    }

    private static void loadConfig(String configFilePath) {
        Properties properties = new Properties();
        try(FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
            authToken = properties.getProperty("auth.token");
            threadCount = Integer.parseInt(properties.getProperty("thread.count"));
            batchSize = Integer.parseInt(properties.getProperty("batch.size"));
            totalRequestCount = Integer.parseInt(properties.getProperty("total-request.count"));
            targetUrl = properties.getProperty("target.url");
            System.out.println("Thread Count: " + threadCount);
            System.out.println("Batch Size: " + batchSize);
            System.out.println("Total Request Count: " + totalRequestCount);
            System.out.println("Target Url: " + targetUrl);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config file: " + configFilePath, e);
        }
    }
    private static void createPostData(int batchSize) throws JsonProcessingException {
        List<Trade>tradeList=new ArrayList<>();
        for(int i=0;i<batchSize;i++){
            Trade trade=new Trade();
            trade.setTransactionId(generateTransactionId());
            trade.setActionType("BUY");
            trade.setMkkMemberCode("MKKTEST");
            trade.setTransactionDate("2025-01-01");
            trade.setTransactionTime("2025-01-01T12:00:00");
            trade.setBuyerRegNo("00000001");
            trade.setSellerRegNo("00000002");
            trade.setBaseCurrency("BTC");
            trade.setQuoteCurrency("TRY");
            trade.setBaseAmount("1");
            trade.setQuoteAmount("60000");
            trade.setBuyerFee("0.0001");
            trade.setSellerFee("0.0001");
            trade.setBuyerCurrency("TRY");
            trade.setSellerCurrency("BTC");
            trade.setBuyerCommissionTransferredRegNo("90000001");
            trade.setSellerCommissionTransferredRegNo("90000002");
            trade.setCorrectionDate(null);
            trade.setCorrectionRefId(null);
            trade.setCorrectionDesc(null);
            tradeList.add(trade);
        }
        postData=objectMapper.writeValueAsString(tradeList);
        System.out.println("Post Data Created");

    }
    private static String generateTransactionId() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    private static void runLoadTest() {
        ExecutorService executorService= Executors.newFixedThreadPool(threadCount);
        for(int i=0;i<threadCount;i++){
            executorService.execute(Main::handleRequests);
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }
    private static void handleRequests(){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(targetUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(postData))
                .build();

        while (true){
            if(requestCounter.get()>=totalRequestCount)
                break;
            sendRequest(request);
            requestCounter.addAndGet(batchSize);
        }
    }

    private static void sendRequest( HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Thread " + Thread.currentThread().getName() + " - Response Code: " + response.statusCode() + " - Request Count: " + requestCounter.get());
        } catch (Exception e) {
            System.err.println("Thread " + Thread.currentThread().getName() + " - Error: " + e.getMessage());
        }
    }

}