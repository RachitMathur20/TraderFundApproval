package com.tradeapproval.request;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tradeapproval.process.TradeRequestProcessor;

public class TradeRequest {
	
	private String stockTicker;
	private int quantity;
	private double price;
	private String priceUnit;
	private String exchange;
	private Date startDate;

	private final static Logger LOGGER =  
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			
	public TradeRequest() {

	}

	public TradeRequest(String stockTicker, int quantity, double price, String priceUnit, String exchange,
			Date startDate) {
		this.stockTicker = stockTicker;
		this.quantity = quantity;
		this.price = price;
		this.priceUnit = priceUnit;
		this.exchange = exchange;
		this.startDate = startDate;
	}

	
	// checks Trade Request Approval using Configuration Rule
	public void checkFundsDisbusrment(String configRule,TradeRequestProcessor processor) {
				
		if(processor.checkApproval(configRule,this)){
			LOGGER.log(Level.INFO, "Trader Requested Fund for "+configRule +" Model is Approved");
			System.out.println("Trader Requested Fund for "+configRule +" Model is Approved");
		}else {
			LOGGER.log(Level.INFO, "Trader Requested Fund for "+configRule +" Model is Not Approved");
			System.out.println("Trader Requested Fund for "+configRule +" Model is Not Approved");
		}
	}
}
