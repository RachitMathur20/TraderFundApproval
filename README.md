A hedge fund firm is trying to leverage parallel processing feature for one of their requirement. 

Firm has an existing application which maintains workflow to approve funds for trader used in trading activities.
As of now it is sequential flow where lower level approver has to approve first then only request moves to next higher level approver. 
Team has realized that some pieces of approval can be done parallel which will save time in complete approval process.

So a small component need to be designed which can move request workflow in parallel wherever possible per configured rule.

Existing Rule:
 

Proposed Rule:
 

If you observe the proposed flow, research analyst approval and fund manager approval can be done in parallel which was not possible earlier.

As per proposed rule once trader submits a request, our processing class should send the request to research analyst and fund manager in parallel. After both the approval, processing class should send the request to Division Head and onwards.

Assumptions: 
Since it is not possible to design end-to-end approval workflow in short time, so consider approvers auto approve requests (technically: approver entity always return true for every request). 

Design a processing engine which will have pre-configured rule and request as an input. Depending on rule, engine will send the request to respective approver and after approval; request will be forwarded to next approver in hierarchy. 

Design should be flexible enough to configure the rules.
