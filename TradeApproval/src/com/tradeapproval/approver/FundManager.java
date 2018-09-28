package com.tradeapproval.approver;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tradeapproval.request.TradeRequest;

public class FundManager implements Approver,Callable<Object> {
	
	TradeRequest tradeRequest;
	CyclicBarrier cyclicBarrier;
	private final static Logger LOGGER =  
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public void setNewBarrier(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}
	
	public FundManager(TradeRequest tradeRequest) {
		this.tradeRequest = tradeRequest;
	}

	// Constraints for Trade Request Approval can be implemented in isApproved() method
	@Override
	public boolean isApproved() {

		LOGGER.log(Level.INFO, "Checking parameters for Trader Requested Fund");
		System.out.println("Checking parameters for Trader Requested Fund");

		LOGGER.log(Level.INFO, "Waiting for Fund Manager Approval... ");
		

		for (int i = 0; i < 5; i++) {
			try {

				System.out.println("Waiting for Fund Manager Approval... ");
				Thread.sleep(200);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

		LOGGER.log(Level.INFO, "Wait ended for Fund Manager Approval");
		System.out.println("Wait ended for Fund Manager Approval");

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
