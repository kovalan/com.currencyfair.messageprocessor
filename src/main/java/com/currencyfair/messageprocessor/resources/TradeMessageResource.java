package com.currencyfair.messageprocessor.resources;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.currencyfair.messageprocessor.model.TradeMessage;

@Path("/tradeMessage")
@Consumes(value = MediaType.APPLICATION_JSON)
@Produces(value = MediaType.APPLICATION_JSON)
public class TradeMessageResource {
	
	private BlockingQueue<TradeMessage> tradeMessageQueue = null;
	
	public TradeMessageResource(BlockingQueue<TradeMessage> newQueue){
		this.tradeMessageQueue = newQueue;
	}

	@POST
    @Path("/posting")
    @Consumes(MediaType.APPLICATION_JSON)
    public void consumeNewMessage(@Valid final TradeMessage newTradeMsg, @Suspended final AsyncResponse asyncResponse) {
		// Put the message in a queue
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
	                    .entity("Operation time out.").build());
			}
	      
	    });
	    asyncResponse.setTimeout(20, TimeUnit.SECONDS);
	    // Creating a new thread to improve performance when message processing gets expensive.
	    new Thread(new Runnable() {
			public void run() {
				try{
					tradeMessageQueue.put(newTradeMsg);
				}
				catch(InterruptedException e){
					
				}
		         asyncResponse.resume("success");
			}
			
	       
	    }).start();
		
    }
}
