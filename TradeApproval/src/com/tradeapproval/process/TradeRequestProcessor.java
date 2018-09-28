package com.tradeapproval.process;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tradeapproval.approver.DivisionHead;
import com.tradeapproval.approver.FundManager;
import com.tradeapproval.approver.Operations;
import com.tradeapproval.approver.ResearchAnalyst;
import com.tradeapproval.request.TradeRequest;
 
public class TradeRequestProcessor {

	public static CyclicBarrier cyclicBarrier;
	Callable<Object> researchAnalyst;
	Callable<Object> fundManager;
	Callable<Object> divisionHead;
	Callable<Object> operations;
	
	FutureTask<?>[] approvalTasks;
	
	Thread researchAnalystApprovalThread;
	Thread fundManagerApprovalThread;
	Thread divisionHeadApprovalThread;
	Thread operationsApprovalThread;
	
	private final static Logger LOGGER =  
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public boolean checkApproval(String configRule, TradeRequest tradeRequest) {
		
		boolean approval = false;
				
		researchAnalyst = new ResearchAnalyst(tradeRequest);
		fundManager = new FundManager(tradeRequest);
		divisionHead = new DivisionHead(tradeRequest);
		operations = new Operations(tradeRequest);
		
		approvalTasks = new FutureTask[4];
		
		approvalTasks[0] = new FutureTask<Object>(researchAnalyst);
		approvalTasks[1] = new FutureTask<Object>(fundManager);
		approvalTasks[2] = new FutureTask<Object>(divisionHead);
		approvalTasks[3] = new FutureTask<Object>(operations);
		  
	    researchAnalystApprovalThread = new Thread(approvalTasks[0]);
	    fundManagerApprovalThread = new Thread(approvalTasks[1]);
	    divisionHeadApprovalThread = new Thread(approvalTasks[2]);
	    operationsApprovalThread = new Thread(approvalTasks[3]);
	    
	    researchAnalystApprovalThread.setName("researchAnalystApprovalThread");
	    fundManagerApprovalThread.setName("fundManagerApprovalThread");
	    divisionHeadApprovalThread.setName("divisionHeadApprovalThread");
	    operationsApprovalThread.setName("operationsApprovalThread");

		if("Proposed".equalsIgnoreCase(configRule)) {
			try {
				cyclicBarrier = new CyclicBarrier(3);
				((ResearchAnalyst) researchAnalyst).setNewBarrier(cyclicBarrier);
				((FundManager) fundManager).setNewBarrier(cyclicBarrier);
				
				researchAnalystApprovalThread.start();			    		    
			    fundManagerApprovalThread.start();			    
			    
			    try {
			    	//Wait for other Approver in case of Parallel processing otherwise keep running
					cyclicBarrier.await();
				} catch (BrokenBarrierException e) {
					
					e.printStackTrace();
				}
			    
				System.out.println("Wait ended for Research Analyst & Fund Manager... ");
				
				if(!(boolean)approvalTasks[0].get() || !(boolean)approvalTasks[1].get() ) {
					LOGGER.log(Level.INFO, "Research Analyst OR Fund Manager Denied Approval");
					System.out.println("Research Analyst OR Fund Manager Denied Approval");
					return false;
				}
			    
				approval = checkDivisionAndOperationsApproval();		
				
			} catch (InterruptedException | ExecutionException e) {
				
				e.printStackTrace();
			}
			
				
		}else if("Existing".equalsIgnoreCase(configRule)) {
			try {
				
				cyclicBarrier = new CyclicBarrier(2);
				((ResearchAnalyst) researchAnalyst).setNewBarrier(cyclicBarrier);
			    
			    researchAnalystApprovalThread.start();
			    
			    try {
			    	//Wait for the Approval
					cyclicBarrier.await();
				} catch (BrokenBarrierException e) {
					
					e.printStackTrace();
				}
		    
			    System.out.println("Wait ended for Research Analyst... ");
		    
				if(!(boolean)approvalTasks[0].get()) {
					LOGGER.log(Level.INFO, "Research Analyst Denied Approval");
					System.out.println("Research Analyst Denied Approval");
					return false;
				}
				
				cyclicBarrier = new CyclicBarrier(2);
				((FundManager) fundManager).setNewBarrier(cyclicBarrier);
				
				fundManagerApprovalThread.start();
				
				 try {
					 	//Wait for the Approval
						cyclicBarrier.await();
					} catch (BrokenBarrierException e) {
						
						e.printStackTrace();
					}
				
				System.out.println("Wait ended for Fund Manager... ");
				
				if(!(boolean)approvalTasks[1].get()) {
					LOGGER.log(Level.INFO, "Fund Manager Denied Approval");
					System.out.println("Fund Manager Denied Approval");
					return false;
				}
			    
				approval = checkDivisionAndOperationsApproval();		
				
			} catch (InterruptedException | ExecutionException e) {
				
				e.printStackTrace();
			}
		    			
		}else {
			LOGGER.log(Level.INFO, "No configuration rule with the name "+configRule +" exists");
			System.out.println("No configuration rule with the name "+configRule +" exists");
			
			throw new InvalidInputException("No configuration rule with the name "+configRule +" exists");
		}
		
		return approval;
	}

	public boolean checkDivisionAndOperationsApproval()throws InterruptedException, ExecutionException {
		
		boolean approval = true;
		
		cyclicBarrier = new CyclicBarrier(2); 
		
		((DivisionHead) divisionHead).setNewBarrier(cyclicBarrier);
		
		divisionHeadApprovalThread.start();
		
		try {
			//Wait for the Approval
			cyclicBarrier.await();
		} catch (BrokenBarrierException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("Wait ended for Division Head... ");
		
		if(!(boolean)approvalTasks[2].get()) {
			LOGGER.log(Level.INFO, "Division Head Denied Approval");
			System.out.println("Division Head Denied Approval");
			return false;
		}
		
		cyclicBarrier = new CyclicBarrier(2);
		((Operations) operations).setNewBarrier(cyclicBarrier);
		
		operationsApprovalThread.start();
		
		try {
			//Wait for the Approval
			cyclicBarrier.await();
		} catch (BrokenBarrierException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("Wait ended for Operations... ");
		
		if(!(boolean)approvalTasks[3].get()) {
			LOGGER.log(Level.INFO, "Operations Denied Approval");
			System.out.println("Operations Denied Approval");
			return false;
		}
		
		return approval;
	}

}
