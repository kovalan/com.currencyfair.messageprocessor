package com.currencyfair.messageprocessor.core;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.currencyfair.messageprocessor.model.TradeMessage;
import com.currencyfair.messageprocessor.model.TransactionMetrics;

/**
 * 
 * @author Kovalan
 * Worker to build transaction metrics based on incoming messages
 *
 */
public class TradeMessageMetricsWorker {

	private static List<TradeMessage> tradeMessageList = new SizeLimitedArrayList();
	
	private static Map<String, List<TransactionMetrics>> latestCurrencyExchangeTrends = null;

	public static void addTradeMessages(TradeMessage message) {
		tradeMessageList.add(message);
	}

	public static class SizeLimitedArrayList extends ArrayList<TradeMessage> {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean add(TradeMessage newTradeMsg) {
			if (this.size() >= 100) {
				super.remove(0);
			}
			return super.add(newTradeMsg);
		}
	}

	public static Map<String, List<TransactionMetrics>> retrieveAllMetrics() {

		Map<String, TransactionMetrics> currencyPairTransMap = new HashMap<String, TransactionMetrics>();
		Map<String, TransactionMetrics> currencyBoughtTransMap = new HashMap<String, TransactionMetrics>();
		Map<String, TransactionMetrics> currencySoldTransMap = new HashMap<String, TransactionMetrics>();
		Map<String, TransactionMetrics> countryTransMap = new HashMap<String, TransactionMetrics>();
		Map<String, TransactionMetrics> userTransMap = new HashMap<String, TransactionMetrics>();

		for (TradeMessage tradeMessage : tradeMessageList) {
			// Capture currency pair trends
			manageTransMetrics(currencyPairTransMap,
					getConversionMetricName(tradeMessage.getCurrencyPair()));
			// Capture currency that is being bought
			manageTransMetrics(currencyBoughtTransMap,
					getConversionMetricName(tradeMessage.getCurrencyTo()));
			// Capture currency that is being sold
			manageTransMetrics(currencySoldTransMap,
					getConversionMetricName(tradeMessage.getCurrencyFrom()));
			// Capture transaction originating country
			manageTransMetrics(countryTransMap,
					tradeMessage.getOriginatingCountry());
			// Capture user performing the transaction
			manageTransMetrics(userTransMap, tradeMessage.getUserId());
		}
		
		Map<String, List<TransactionMetrics>> totalCurrencyExchangeTrends = new HashMap<String, List<TransactionMetrics>>();

		// Render currency pair trends from max to min transaction
		List<TransactionMetrics> currencyPairTrends = retrieveTansactionMetrics(currencyPairTransMap, "{0} count is {1}");
		totalCurrencyExchangeTrends.put("CURRENCY_PAIR_EXCHANGE", currencyPairTrends);
		// Render currency that is being bought from max to min transaction
		List<TransactionMetrics> currencyBuyingTrends = retrieveTansactionMetrics(currencyBoughtTransMap, "{0} count is {1}");
		totalCurrencyExchangeTrends.put("CURRENCY_BUYING", currencyBuyingTrends);
		// Render currency that is being sold from max to min transaction
		List<TransactionMetrics> currencySellingTrends = retrieveTansactionMetrics(currencySoldTransMap, "{0} count is {1}");
		totalCurrencyExchangeTrends.put("CURRENCY_SELLING", currencySellingTrends);
		// Render transaction originating country from max to min transaction
		List<TransactionMetrics> transOringCountryTrends = retrieveTansactionMetrics(countryTransMap, "Total conversions orginating from {0} is {1}");
		totalCurrencyExchangeTrends.put("TRANSACTION_ORIGIN_COUNTRY", transOringCountryTrends);
		// Render user performing max to min transaction
		List<TransactionMetrics> userTrends = retrieveTansactionMetrics(userTransMap, "Total convsersions made by user {0} is {1}");
		totalCurrencyExchangeTrends.put("USER_TRANSACTION", userTrends);
		
		
		latestCurrencyExchangeTrends = totalCurrencyExchangeTrends;
		
		return totalCurrencyExchangeTrends;
	}
	
	private static String getConversionMetricName(String transactionItem){
		return "total|"+transactionItem+"|conversion";
	}

	private static void manageTransMetrics(
			Map<String, TransactionMetrics> transMetricsMap, String paramValue) {

		TransactionMetrics transMetric = transMetricsMap.get(paramValue);
		if (transMetric == null) {
			transMetric = new TransactionMetrics(paramValue);
			transMetricsMap.put(paramValue, transMetric);
		}
		transMetric.addMetricValueCount();
	}

	private static List<TransactionMetrics> retrieveTansactionMetrics(
			Map<String, TransactionMetrics> currencyTransMap, String messageFormat) {
		List<TransactionMetrics> currencyTrans = new ArrayList<TransactionMetrics>(
				currencyTransMap.values());
		Collections.sort(currencyTrans,
				Collections.reverseOrder(new CurrencyPairTransComparator()));
		for (TransactionMetrics transaction : currencyTrans) {
			System.out.println(MessageFormat.format(messageFormat, transaction.getMetricName(), transaction.getMetricValue()));
		}
		
		return currencyTrans;

	}

	private static class CurrencyPairTransComparator implements
			Comparator<TransactionMetrics> {
		public int compare(TransactionMetrics o1, TransactionMetrics o2) {
			return o1.getMetricValue() > o2.getMetricValue() ? 1 : -1;
		}

	}
	
	public static Map<String, List<TransactionMetrics>> getLatestCurrencyExchangeTrends(){
		return latestCurrencyExchangeTrends;
	}

}
