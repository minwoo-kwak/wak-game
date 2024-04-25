package com.wak.game.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Component
public class RequestUtil {

    @Autowired
    ObjectMapper objectMapper;

    public MockHttpServletRequestBuilder request(RequestMethod method, String urlTemplate, Object... uriVariables) {
        if (method == RequestMethod.GET) {
            return getRequest(urlTemplate, uriVariables);
        } else if (method == RequestMethod.POST) {
            return postRequest(urlTemplate, uriVariables);
        } else if (method == RequestMethod.PUT) {
            return putRequest(urlTemplate, uriVariables);
        } else if (method == RequestMethod.DELETE) {
            return deleteRequest(urlTemplate, uriVariables);
        }

        return null;
    }

    public static class UrlTemplate {


    }

    public MockHttpServletRequestBuilder getRequest(String urlTemplate, Object... uriVariables) {
        return get(urlTemplate, uriVariables);
    }

    public MockHttpServletRequestBuilder postRequest(String urlTemplate, Object... uriVariables) {
        return post(urlTemplate, uriVariables);
    }

    public MockHttpServletRequestBuilder putRequest(String urlTemplate, Object... uriVariables) {
        return put(urlTemplate, uriVariables);
    }

    public MockHttpServletRequestBuilder deleteRequest(String urlTemplate, Object... uriVariables) {
        return delete(urlTemplate, uriVariables);
    }

    public MockHttpServletRequestBuilder getRequestWithJson(String urlTemplate, Object request, Object... uriVariables) throws JsonProcessingException {
        return getRequest(urlTemplate, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
    }

    public MockHttpServletRequestBuilder postRequestWithJson(String urlTemplate, Object request, Object... uriVariables) throws JsonProcessingException {
        return postRequest(urlTemplate, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
    }

    public MockHttpServletRequestBuilder putRequestWithJson(String urlTemplate, Object request, Object... uriVariables) throws JsonProcessingException {
        return putRequest(urlTemplate, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
    }

    public MockHttpServletRequestBuilder deleteRequestWithJson(String urlTemplate, Object request, Object... uriVariables) throws JsonProcessingException {
        return deleteRequest(urlTemplate, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
    }

    public <T> ResultMatcher jsonContent(T object) throws JsonProcessingException {
        String expectedJsonResponse = objectMapper.writeValueAsString(ApiUtils.success(object));
        return content().json(expectedJsonResponse);
    }

    public <T> ResultMatcher jsonListContent(List<T> object) throws JsonProcessingException {
        String expectedJsonResponse = objectMapper.writeValueAsString(ApiUtils.success(object));
        return content().json(expectedJsonResponse);
    }

    public StatusResultMatchers status() {
        return MockMvcResultMatchers.status();
    }

}
