package com.example.kafkaconsumer.service;

import com.example.kafkaconsumer.model.QueryCreatedEvent;

public interface QueryEventProcessor {

	void process(QueryCreatedEvent event);
}
