package com.currencyfair.messageprocessor.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.corundumstudio.socketio.BroadcastOperations;
import com.currencyfair.messageprocessor.model.TradeMessage;
import com.currencyfair.messageprocessor.model.TransactionMetrics;
/**
 * 
 * @author Kovalan
 *
 *Responsible for processing the consumed messages and publishing to the Socket io front end
 */

public class TradeMessageWorker extends Thread {
	
	BlockingQueue<TradeMessage> tradeMessageQueue = null;
	BroadcastOperations operations = null;
	
	public TradeMessageWorker(BlockingQueue<TradeMessage> newMsgQueue, BroadcastOperations broadcastOperations){
		this.tradeMessageQueue = newMsgQueue;
		this.operations = broadcastOperations;
	}
	
	@Override
	public void run() {
		
		while ( true ) {
            try {
                    // Get the next trade message off of the queue
                  TradeMessage tradeMessage = tradeMessageQueue.take();
                  TradeMessageMetricsWorker.addTradeMessages(tradeMessage);
                  Map<String, List<TransactionMetrics>> totalCurrencyExchangeTrends = TradeMessageMetricsWorker.retrieveAllMetrics();
                  synchronized (operations) {
                  operations.sendEvent("publishMessage", totalCurrencyExchangeTrends);
                  }
            }
            catch ( InterruptedException ie ) {
            	ie.printStackTrace();
                break;  // Terminate
            }
        }
	}
}
