package com.currencyfair.messageprocessor.application;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class TradeMessageConfiguration extends Configuration {
	
	@NotEmpty
	private String workerThreadPoolSize;

	@JsonProperty
	public String getWorkerThreadPoolSize() {
		return workerThreadPoolSize;
	}

	@JsonProperty
	public void setWorkerThreadPoolSize(String workerThreadPoolSize) {
		this.workerThreadPoolSize = workerThreadPoolSize;
	}
	
	

}
