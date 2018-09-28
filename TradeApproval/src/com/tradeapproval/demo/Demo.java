package com.tradeapproval.demo;

import com.tradeapproval.process.TradeRequestProcessor;
import com.tradeapproval.request.TradeRequest;

public class Demo {
	
	public static void main(String[] args) {
		
		TradeRequest tradeRequest = new TradeRequest();
		TradeRequestProcessor processor = new TradeRequestProcessor();
		
		//configure the rule (Existing,Proposed) here
		tradeRequest.checkFundsDisbusrment("Proposed",processor);		
	}

}
