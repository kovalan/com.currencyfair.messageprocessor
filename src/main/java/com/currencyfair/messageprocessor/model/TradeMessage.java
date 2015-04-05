package com.currencyfair.messageprocessor.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

public class TradeMessage {

	@NotBlank
	private String userId;
	
	private String currencyFrom;
	
	private String currencyTo;
	
	private long amountSell;
	
	private long amountBuy;
	
	private double rate;
	
	private Date timePlaced;
	
	private String originatingCountry;
	
	public TradeMessage(){
		
	}

	public TradeMessage(String userId, String currencyFrom, String currencyTo,
			long amountSell, long amountBuy, double rate, String timePlaced,
			String originatingCountry) {
		super();
		this.userId = userId;
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
		this.amountSell = amountSell;
		this.amountBuy = amountBuy;
		this.rate = rate;
		setTimePlaced(timePlaced);
		this.originatingCountry = originatingCountry;
	}
	
	public TradeMessage(String userId, String currencyFrom, String currencyTo){
		super();
		this.userId = userId;
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}

	public String getCurrencyTo() {
		return currencyTo;
	}

	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}

	public long getAmountSell() {
		return amountSell;
	}

	public void setAmountSell(long amountSell) {
		this.amountSell = amountSell;
	}

	public long getAmountBuy() {
		return amountBuy;
	}

	public void setAmountBuy(long amountBuy) {
		this.amountBuy = amountBuy;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Date getTimePlaced() {
		return timePlaced;
	}

		
	public void setTimePlaced(String timePlaced) {
		try {
		this.timePlaced = new SimpleDateFormat("dd-MMM-yy HH:mm:ss").parse(timePlaced);
		    } 
	catch (ParseException e) {
		this.timePlaced = null;
		}
	}

	public String getOriginatingCountry() {
		return originatingCountry;
	}

	public void setOriginatingCountry(String originatingCountry) {
		this.originatingCountry = originatingCountry;
	}
	
	@Override
	public String toString() {
		return "Trade Message";
	}
	
	public String getCurrencyPair(){
		return this.currencyFrom+"_to_"+this.currencyTo;
	}
	
}
