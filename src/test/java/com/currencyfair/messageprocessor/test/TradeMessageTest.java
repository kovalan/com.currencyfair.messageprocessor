package com.currencyfair.messageprocessor.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class TradeMessageTest {

	private static final Random rand = new Random();
	
		public static void main(String[] args) {
	 
		  try {
			  for(int i=0; i<10 ; i++){
			URL url = new URL("http://localhost:8080/tradeMessage/posting");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			String input = getRandamTradeMessage();
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
	 
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}
			
			conn.disconnect();
			
			  }
	 
		  } catch (MalformedURLException e) {
	 
			e.printStackTrace();
	 
		  } catch (IOException e) {
	 
			e.printStackTrace();
	 
		 }
	 
		}
		
		private static String getRandamTradeMessage(){
			
			switch (rand.nextInt(6)) {
			
				case 0:
					return "{\"userId\": \"10001\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"GBP\", \"amountSell\": 1000.00, \"amountBuy\": 747.10, \"rate\": 0.7471, \"timePlaced\" : \"24-JAN-15 10:27:44\", \"originatingCountry\" : \"FR\"}";
				
				case 1:
					return "{\"userId\": \"10002\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"USD\", \"amountSell\": 1000.00, \"amountBuy\": 1090.76, \"rate\": 1.0907, \"timePlaced\" : \"24-JAN-15 10:27:44\", \"originatingCountry\" : \"FR\"}";
							
				case 2:
					return "{\"userId\": \"10003\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"AUD\", \"amountSell\": 1000.00, \"amountBuy\": 1358.1, \"rate\": 1.3581, \"timePlaced\" : \"24-JAN-15 10:27:44\", \"originatingCountry\" : \"FR\"}";
					
				case 3:
					return "{\"userId\": \"10004\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"SGD\", \"amountSell\": 1000.00, \"amountBuy\": 1491.58, \"rate\": 1.4915, \"timePlaced\" : \"24-JAN-15 10:27:44\", \"originatingCountry\" : \"US\"}";
					
				case 4:
					return "{\"userId\": \"10005\", \"currencyFrom\": \"USD\", \"currencyTo\": \"EUR\", \"amountSell\": 1000.00, \"amountBuy\": 916.55, \"rate\": 0.9165, \"timePlaced\" : \"24-JAN-15 10:27:44\", \"originatingCountry\" : \"FR\"}";
				
				case 5:
					return "{\"userId\": \"10006\", \"currencyFrom\": \"GBP\", \"currencyTo\": \"USD\", \"amountSell\": 1000.00, \"amountBuy\": 1487.48, \"rate\": 1.4874, \"timePlaced\" : \"24-JAN-15 10:27:44\", \"originatingCountry\" : \"FR\"}";

				default:
					return "{\"userId\": \"99999\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"GBP\", \"amountSell\": 1000.00, \"amountBuy\": 747.10, \"rate\": 0.7471, \"timePlaced\" : \"24-JAN-15 10:27:44\", \"originatingCountry\" : \"FR\"}";
			}
		}

}
