package com.tradeapproval.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tradeapproval.approver.DivisionHead;
import com.tradeapproval.approver.FundManager;
import com.tradeapproval.approver.Operations;
import com.tradeapproval.approver.ResearchAnalyst;
import com.tradeapproval.process.TradeRequestProcessor;
import com.tradeapproval.process.InvalidInputException;
import com.tradeapproval.request.TradeRequest;

public class TradeRequestTest {
	
	@Test
	public void shouldCheckConfigRuleExisting()
	{
		TradeRequest tradeRequest = new TradeRequest();
		TradeRequestProcessor processor = new TradeRequestProcessor();
		
		boolean result=processor.checkApproval("Existing",tradeRequest);
		
		assertEquals(true,result);
	}
	
	@Test
	public void shouldCheckConfigRuleProposed()
	{
		TradeRequest tradeRequest = new TradeRequest();
		TradeRequestProcessor processor = new TradeRequestProcessor();
		
		boolean result=processor.checkApproval("Proposed",tradeRequest);
		
		assertEquals(true,result);
	}
	
	@Test(expected=InvalidInputException.class)
	public void shouldCheckInvalidConfigRule()
	{
		TradeRequest tradeRequest = new TradeRequest();
		TradeRequestProcessor processor = new TradeRequestProcessor();
		
		boolean result=processor.checkApproval("XYZ",tradeRequest);
	}
	
	@Test
	public void shouldCheckResearchAnalystApproval()
	{
		TradeRequest tradeRequest = new TradeRequest();
		
		ResearchAnalyst approver = new ResearchAnalyst(tradeRequest);
		
		boolean result=approver.isApproved();
		
		assertEquals(true,result);
	}
	
	@Test
	public void shouldCheckFundManagerApproval()
	{
		TradeRequest tradeRequest = new TradeRequest();
		
		FundManager approver = new FundManager(tradeRequest);
		
		boolean result=approver.isApproved();
		
		assertEquals(true,result);
	}
	
	@Test
	public void shouldCheckDivisionHeadApproval()
	{
		TradeRequest tradeRequest = new TradeRequest();
		
		DivisionHead approver = new DivisionHead(tradeRequest);
		
		boolean result=approver.isApproved();
		
		assertEquals(true,result);
	}
	
	@Test
	public void shouldCheckOperationstApproval()
	{
		TradeRequest tradeRequest = new TradeRequest();
		
		Operations approver = new Operations(tradeRequest);
		
		boolean result=approver.isApproved();
		
		assertEquals(true,result);
	}
	
}
