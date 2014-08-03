package com.simian.game.server.thread;


import com.lmax.disruptor.EventFactory;

public class TicketPoolService {
	private static TicketEvent entity= new TicketEvent();
 /*   public final static EventFactory<TicketEvent> INSTANCE =
	new EventFactory<TicketEvent>() {
	public TicketEvent newInstance() {
	    return entity;
	}
    };
    */
    public final static EventFactory<TicketEvent> QueryFactory =
    		new EventFactory<TicketEvent>() {
    		public TicketEvent newInstance() {
    			TicketEvent e = new TicketEvent();
    		    return e;
    		}
    	    }; 
    	    
}