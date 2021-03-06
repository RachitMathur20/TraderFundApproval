package com.tradeapproval.approver;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tradeapproval.request.TradeRequest;

public class DivisionHead implements Approver,Callable<Object> {
	
	TradeRequest tradeRequest;
	CyclicBarrier cyclicBarrier;
	private final static Logger LOGGER =  
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
	
	public DivisionHead(TradeRequest tradeRequest) {
		this.tradeRequest = tradeRequest;
	}

	public void setNewBarrier(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}
	
	// Constraints for Trade Request Approval can be implemented in isApproved() method
	@Override
	public boolean isApproved() {
				
		LOGGER.log(Level.INFO, "Checking parameters for Trader Requested Fund");
		System.out.println("Checking parameters for Trader Requested Fund");
		
		LOGGER.log(Level.INFO, "Waiting for Division Head Approval... ");
		
		
		for(int i=0;i<5;i++) {				
			try {
				System.out.println("Waiting for Division Head Approval... ");
				Thread.sleep(500);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
		LOGGER.log(Level.INFO, "Wait ended for Division Head Approval");
		System.out.println("Wait ended for Division Head Approval");
		
		return true;
	}

	@Override
	public Object call() throws Exception {
						
		boolean approved = isApproved();
		
		try
        { 
			
			//Wait for other Approver in case of Parallel processing otherwise keep running
			cyclicBarrier.await();
        }  
        catch (InterruptedException | BrokenBarrierException e)  
        { 
            e.printStackTrace(); 
        }
		
		return approved;
	}

}
