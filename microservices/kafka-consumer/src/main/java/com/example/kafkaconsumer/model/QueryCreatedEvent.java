package com.example.kafkaconsumer.model;

public record QueryCreatedEvent(String id, String message, String createdAt) {
}
