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
    private static int maxRetryCount;
    private static int retryInterval;
    private static final AtomicInteger requestCounter = new AtomicInteger(0);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args)  {
        if(args.length<1){
            System.out.println("Usage: java -jar JarName.jar <target url>");
            System.exit(1);
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
            maxRetryCount = Integer.parseInt(properties.getProperty("max-retry.count"));
            retryInterval = Integer.parseInt(properties.getProperty("retry-interval"));
            System.out.println("Thread Count: " + threadCount);
            System.out.println("Batch Size: " + batchSize);
            System.out.println("Total Request Count: " + totalRequestCount);
            System.out.println("Target Url: " + targetUrl);
            System.out.println("Max Retry Count: " + maxRetryCount);
            System.out.println("Retry Interval: " + retryInterval);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config file: " + configFilePath, e);
        }
    }
    private static String createPostData(int batchSize)  {
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
        System.out.println("Post Data Created");
        try {
            return objectMapper.writeValueAsString(tradeList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
    private static void handleRequests()  {
        while (true){
            requestCounter.incrementAndGet();
            if(requestCounter.get()>=totalRequestCount)
                break;
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(targetUrl))
                        .header("Content-Type", "application/json")
                        .header("Authorization", authToken)
                        .POST(HttpRequest.BodyPublishers.ofString(createPostData(batchSize)))
                        .build();
                sendRequest(request);
            }catch (Exception e){
                System.err.println("Request Failed. Error: " + e.getMessage());
            }

        }
    }

    private static void sendRequest(HttpRequest request) {
        boolean success = false;

        for (int attempt = 0; attempt < maxRetryCount; attempt++) {
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    success = true;
                    break;
                } else {
                    System.out.println("Request Failed. Status Code: " + response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Request Failed at attempt " + (attempt + 1) + ". Error: " + e.getMessage());
            }

            if (attempt < maxRetryCount - 1) {
                System.out.println("Retrying Request in " + retryInterval + " seconds...");
                try {
                    Thread.sleep(retryInterval * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        if (!success) {
            System.err.println("Request Failed. All retry attempts exhausted.");
        }
    }


}