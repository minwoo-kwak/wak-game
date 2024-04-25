package com.wak.game.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
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

    public <T> ResultMatcher jsonContent(Class<T> clazz, T object) throws JsonProcessingException {
        checkClass(clazz, object);
        ApiResult<T> expectedResponse = ApiUtils.success(object);
        String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);
        return content().json(expectedJsonResponse);
    }

    public <T> ResultMatcher jsonListContent(Class<T> clazz, List<T> object) throws JsonProcessingException {
        checkClass(clazz, object.get(0));
        ApiResult<List<T>> expectedResponse = ApiUtils.success(object);
        String expectedJsonResponse = objectMapper.writeValueAsString(expectedResponse);
        return content().json(expectedJsonResponse);
    }

    private <T> void checkClass(Class<T> clazz, T object) {
        if (!clazz.isInstance(object)) {
            throw new IllegalArgumentException("Argument must be an instance of " + clazz.getSimpleName());
        }
    }

}
