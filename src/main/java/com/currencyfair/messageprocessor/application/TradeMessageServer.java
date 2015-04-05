package com.currencyfair.messageprocessor.application;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.currencyfair.messageprocessor.core.TradeMessageMetricsWorker;
import com.currencyfair.messageprocessor.core.TradeMessageWorker;
import com.currencyfair.messageprocessor.model.TradeMessage;
import com.currencyfair.messageprocessor.resources.TradeMessageResource;

/**
 * 
 * @author Kovalan
 * 
 * Application launcher that starts the server and deploys the resources to the server
 *
 */
public class TradeMessageServer extends
		Application<TradeMessageConfiguration> {
	public static void main(String[] args) throws Exception {
		new TradeMessageServer().run( "server" , "src/main/resources/config.yml");
	}

	@Override
	public void run(TradeMessageConfiguration config, Environment env)
			throws Exception {
		
		// Create a socketIOserver to publish Trade Messages to the front end through a socket connection
		Configuration cfg = new Configuration();
        cfg.setHostname("localhost");
        cfg.setPort(9092);
        final SocketIOServer server = new SocketIOServer(cfg);
        BroadcastOperations broadcastOperations = server.getBroadcastOperations();
        server.start();
        server.addConnectListener(new ConnectListener() {
			public void onConnect(SocketIOClient client) {
				client.sendEvent("publishMessage", TradeMessageMetricsWorker.getLatestCurrencyExchangeTrends());
			}
		});
		
        // Trade Message queue
		BlockingQueue<TradeMessage> tradeMessageQueue = new ArrayBlockingQueue<TradeMessage>(1000);
		
		int THREAD_POOL_SIZE = Integer.parseInt(config.getWorkerThreadPoolSize());
		
		for ( int i = 0; i < THREAD_POOL_SIZE; i++ ) {
		    // Create and start the worker thread, which will
		    // immediately wait for the queue to be populated and then to be signaled to begin processing
		    TradeMessageWorker worker = new TradeMessageWorker(tradeMessageQueue, broadcastOperations);
		    worker.start();
		}
		
		// message consumer endpoint
		final TradeMessageResource resource = new TradeMessageResource(tradeMessageQueue);
		env.jersey().register(resource);
	}
	
	@Override
	public void initialize(Bootstrap<TradeMessageConfiguration> bootstrap) {
		super.initialize(bootstrap);
		bootstrap.addBundle(new AssetsBundle("/assets", "/CurrencyTransactionTrends", "index.html", "static"));
	}
	
}
