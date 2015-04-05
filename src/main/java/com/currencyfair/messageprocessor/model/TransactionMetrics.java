package com.currencyfair.messageprocessor.model;

public class TransactionMetrics{

	private String metricName;
	private long metricValue = 0l;
	
	public TransactionMetrics(String name){
		this.metricName = name;
	}
	
	public void addMetricValueCount(){
		metricValue ++;
	}

	public long getMetricValue() {
		return metricValue;
	}

	public String getMetricName() {
		return metricName;
	}
	
}
