package com.service;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class Gatewayservice {

    private final DiscoveryClient DC;

    private int current = 0;

    public Gatewayservice(DiscoveryClient DC) {
        this.DC = DC;
    }

    // -------------------------------------------------
    // GET REQUEST
    // -------------------------------------------------

    public Object invokeService(String service, String endpoint) {

        List<ServiceInstance> services = DC.getInstances(service);

        if (services.isEmpty()) {
            return "Service not available: " + service;
        }

        // Round-robin load balancing
        if (current >= services.size()) {
            current = 0;
        }

        ServiceInstance instance = services.get(current);

        current = (current + 1) % services.size();

        String url = String.format(
                "%s/%s",
                instance.getUri(),
                endpoint
        );

        System.out.println("Gateway calling: " + url);

        RestClient client = RestClient.create();

        return client.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    // -------------------------------------------------
    // POST REQUEST
    // -------------------------------------------------

    public Object invokePostService(
            String service,
            String endpoint,
            Object data) {

        List<ServiceInstance> services = DC.getInstances(service);

        if (services.isEmpty()) {
            return "Service not available: " + service;
        }

        // Round-robin load balancing
        if (current >= services.size()) {
            current = 0;
        }

        ServiceInstance instance = services.get(current);

        current = (current + 1) % services.size();

        String url = String.format(
                "%s/%s",
                instance.getUri(),
                endpoint
        );

        System.out.println("Gateway calling: " + url);

        RestClient client = RestClient.create();

        return client.post()
                .uri(url)
                .header(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .body(data)
                .retrieve()
                .body(String.class);
    }

    // -------------------------------------------------
    // PUT REQUEST
    // -------------------------------------------------

    public Object invokePutService(
            String service,
            String endpoint,
            Object data) {

        List<ServiceInstance> services = DC.getInstances(service);

        if (services.isEmpty()) {
            return "Service not available: " + service;
        }

        if (current >= services.size()) {
            current = 0;
        }

        ServiceInstance instance = services.get(current);

        current = (current + 1) % services.size();

        String url = String.format(
                "%s/%s",
                instance.getUri(),
                endpoint
        );

        System.out.println("Gateway calling: " + url);

        RestClient client = RestClient.create();

        return client.put()
                .uri(url)
                .header(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .body(data)
                .retrieve()
                .body(String.class);
    }

    // -------------------------------------------------
    // DELETE REQUEST
    // -------------------------------------------------

    public Object invokeDeleteService(
            String service,
            String endpoint) {

        List<ServiceInstance> services = DC.getInstances(service);

        if (services.isEmpty()) {
            return "Service not available: " + service;
        }

        if (current >= services.size()) {
            current = 0;
        }

        ServiceInstance instance = services.get(current);

        current = (current + 1) % services.size();

        String url = String.format(
                "%s/%s",
                instance.getUri(),
                endpoint
        );

        System.out.println("Gateway calling: " + url);

        RestClient client = RestClient.create();

        return client.delete()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    // -------------------------------------------------
    // GET REQUEST WITH JWT
    // -------------------------------------------------

    public Object invokeServiceWithToken(
            String service,
            String endpoint,
            String token) {

        List<ServiceInstance> services = DC.getInstances(service);

        if (services.isEmpty()) {
            return "Service not available: " + service;
        }

        if (current >= services.size()) {
            current = 0;
        }

        ServiceInstance instance = services.get(current);

        current = (current + 1) % services.size();

        String url = String.format(
                "%s/%s",
                instance.getUri(),
                endpoint
        );

        System.out.println("Gateway calling: " + url);

        RestClient client = RestClient.create();

        return client.get()
                .uri(url)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        token
                )
                .retrieve()
                .body(String.class);
    }

    // -------------------------------------------------
    // POST REQUEST WITH JWT
    // -------------------------------------------------

    public Object invokePostServiceWithToken(
            String service,
            String endpoint,
            Object data,
            String token) {

        List<ServiceInstance> services = DC.getInstances(service);

        if (services.isEmpty()) {
            return "Service not available: " + service;
        }

        if (current >= services.size()) {
            current = 0;
        }

        ServiceInstance instance = services.get(current);

        current = (current + 1) % services.size();

        String url = String.format(
                "%s/%s",
                instance.getUri(),
                endpoint
        );

        System.out.println("Gateway calling: " + url);

        RestClient client = RestClient.create();

        return client.post()
                .uri(url)
                .header(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .header(
                        HttpHeaders.AUTHORIZATION,
                        token
                )
                .body(data)
                .retrieve()
                .body(String.class);
    }
}