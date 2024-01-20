package com.qosocial.v1api.common.exception;

import jakarta.servlet.RequestDispatcher;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.*;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> attributes = super.getErrorAttributes(webRequest, options);

        // Convert existing timestamp to Instant
        // if the original timestamp value cannot be converted to an Instant, we set timestamp to Instant.now()
        Object timestampObj = attributes.get("timestamp");
        Instant instant = timestampObj instanceof Date ? ((Date) timestampObj).toInstant() : Instant.now();
        attributes.put("timestamp", instant);

        Throwable error = getError(webRequest);
        if (error == null) {
            return attributes;
        }

        String message = error.getMessage();
        if (StringUtils.isEmpty(message)) {
            message = getAttribute(webRequest, RequestDispatcher.ERROR_MESSAGE, String.class).orElse("No message available");
        }

        List<String> messages = Collections.singletonList(message);
        attributes.put("message", messages);
        attributes.remove("error");
        attributes.remove("trace");
        attributes.remove("path");
        attributes.remove("status");
        attributes.remove("requestId");

        return attributes;
    }

    private <T> Optional<T> getAttribute(RequestAttributes requestAttributes, String name, Class<T> type) {
        Object value = requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
        if (type.isInstance(value)) {
            return Optional.of((T) value);
        }
        return Optional.empty();
    }
}
