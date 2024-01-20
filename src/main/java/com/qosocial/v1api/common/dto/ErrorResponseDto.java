package com.qosocial.v1api.common.dto;

import java.time.Instant;
import java.util.List;

public class ErrorResponseDto {

    private Instant timestamp;
    private List<String> message;

    public ErrorResponseDto(List<String> message) {
        this.timestamp = Instant.now();
        this.message = message;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getMessage() {
        return this.message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

}
