package service;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.sun.xacml.Indenter;
import com.sun.xacml.Rule;
import com.sun.xacml.Target;
import com.sun.xacml.TargetMatch;
import com.sun.xacml.UnknownIdentifierException;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.AttributeDesignator;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.combine.CombiningAlgFactory;
import com.sun.xacml.combine.OrderedPermitOverridesRuleAlg;
import com.sun.xacml.combine.RuleCombiningAlgorithm;
import com.sun.xacml.cond.Apply;
import com.sun.xacml.cond.Function;
import com.sun.xacml.cond.FunctionFactory;
import com.sun.xacml.ctx.Result;

/**
 * This class represents a policy. 
 */
class Policy {
	private String policyID;
	private String description;
	private ArrayList<String> userID;
	private ArrayList<String> resourceID;
	private String actionID;
	private String groupID;
	private String groupAdminID;
	private int    decision;
	private StringBuffer policy;	
	
	public Policy() {
		policy = null;
		userID = new ArrayList<String>();
		resourceID = new ArrayList<String>();
	}
	
	StringBuffer getPolicy() {
		return policy;
	}
	
	void setPolicyID( String policyID ) { this.policyID = policyID; }
	void setDescription( String description ) { this.description = description; }
	void addUserID( String userID ) { this.userID.add( userID ); }
	void addResourceID( String resourceID ) { this.resourceID.add( resourceID ); }
	void setActionID( String actionID ) { this.actionID = actionID; }
	void setGroupID( String groupID ) { this.groupID = groupID; }
	void setGroupAdminID( String groupAdminID ) { this.groupAdminID = groupAdminID; }
	void setDecision( int decision ) { this.decision = decision; }
	
	/**
	 * 
	 * @return The policy ID.
	 */
	String getPolicyID() { return policyID; }
	
	/**
	 * Generates policy from the parameters.
	 * @return Policy
	 */
	StringBuffer generatePolicy() {       
		URI policyId = null;
		try {
			policyId = new URI(policyID);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

        URI combiningAlgId = null;
		try {
			combiningAlgId = new URI(OrderedPermitOverridesRuleAlg.algId);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        CombiningAlgFactory factory = CombiningAlgFactory.getInstance();
    	RuleCombiningAlgorithm combiningAlg = null;
        try {
			combiningAlg =
			    (RuleCombiningAlgorithm)(factory.createAlgorithm(combiningAlgId));
		} catch (UnknownIdentifierException e) {
			e.printStackTrace();
		}        
        
        Target policyTarget = createPolicyTarget();

        Rule rule = createRule();

        Rule defaultRule = null;
        try {
			defaultRule = new Rule(new URI("FinalRule"), Result.DECISION_DENY,
			        null, null, null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
        List ruleList = new ArrayList();
        ruleList.add(rule);
        //ruleList.add(defaultRule);

        com.sun.xacml.Policy policy = new com.sun.xacml.Policy(policyId, combiningAlg, description,
                policyTarget, ruleList);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        policy.encode(out, new Indenter());
        this.policy = new StringBuffer(out.toString());
        
        return this.policy;
	}
	
    private Rule createRule() {
        URI ruleId = null;
		try {
			ruleId = new URI("CommitRule");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

        int effect = decision;
        

        Target target = createRuleTarget();

        Apply condition = createRuleCondition();
        
        return new Rule(ruleId, effect, null, target, condition);
    }
	
    private Target createRuleTarget() {
        List actions = new ArrayList();
                
        List action = new ArrayList();

        String actionMatchId =
            "urn:oasis:names:tc:xacml:1.0:function:string-equal";

        URI actionDesignatorType = null;
		try {
			actionDesignatorType = new URI("http://www.w3.org/2001/XMLSchema#string");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        URI actionDesignatorId = null;
		try {
			actionDesignatorId = new URI("urn:oasis:names:tc:xacml:1.0:action:action-id");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        AttributeDesignator actionDesignator =
            new AttributeDesignator(AttributeDesignator.ACTION_TARGET,
                                    actionDesignatorType,
                                    actionDesignatorId, false);

        StringAttribute actionValue = new StringAttribute(actionID);

        action.add(createTargetMatch(TargetMatch.ACTION, actionMatchId,
                                     actionDesignator, actionValue));

        actions.add(action);

        return new Target(null, null, actions);
    }
    
    private Apply createRuleCondition() {
        List conditionArgs = new ArrayList();

        FunctionFactory factory = FunctionFactory.getConditionInstance();
        Function conditionFunction = null;
        try {
            conditionFunction =
                factory.createFunction("urn:oasis:names:tc:xacml:1.0:function:"
                                       + "string-equal");
        } catch (Exception e) {
            return null;
        }
        
        List applyArgs = new ArrayList();

        factory = FunctionFactory.getGeneralInstance();
        Function applyFunction = null;
        try {
            applyFunction =
                factory.createFunction("urn:oasis:names:tc:xacml:1.0:function:"
                                       + "string-one-and-only");
        } catch (Exception e) {
            return null;
        }
        
        URI designatorType = null;
		try {
			designatorType = new URI("http://www.w3.org/2001/XMLSchema#string");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        URI designatorId = null;
		try {
			designatorId = new URI("group");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        URI designatorIssuer = null;
		try {
			designatorIssuer = new URI(groupAdminID);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        AttributeDesignator designator =
            new AttributeDesignator(AttributeDesignator.SUBJECT_TARGET,
                                    designatorType, designatorId, false,
                                    designatorIssuer);
        applyArgs.add(designator);

        Apply apply = new Apply(applyFunction, applyArgs, false);
        
        // add the new apply element to the list of inputs to the condition
        conditionArgs.add(apply);

        // create an AttributeValue and add it to the input list
        StringAttribute value = new StringAttribute(groupID);
        conditionArgs.add(value);

        // finally, create & return our Condition
        return new Apply(conditionFunction, conditionArgs, true);
    }
	
	private Target createPolicyTarget() {
		List subjects = new ArrayList();
        List resources = new ArrayList();

        // create the Subject section
        for( int i = 0 ; i < userID.size() ; i++ ) {    
        List subject = new ArrayList();
        
        String subjectMatchId =
            "urn:oasis:names:tc:xacml:1.0:function:rfc822Name-match";

        URI subjectDesignatorType = null;
		try {
			subjectDesignatorType = new URI("urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        URI subjectDesignatorId = null;
		try {
			subjectDesignatorId = new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        AttributeDesignator subjectDesignator =
            new AttributeDesignator(AttributeDesignator.SUBJECT_TARGET,
                                    subjectDesignatorType,
                                    subjectDesignatorId, false);

        StringAttribute subjectValue =
            new StringAttribute(userID.get(i));
        
        subject.add(createTargetMatch(TargetMatch.SUBJECT, subjectMatchId,
                                      subjectDesignator, subjectValue));
        
        subjects.add(subject);        
        }
        
        // create the Resource section
        for( int i = 0 ; i < resourceID.size() ; i++ ) {
        List resource = new ArrayList();

        String resourceMatchId =
            "urn:oasis:names:tc:xacml:1.0:function:anyURI-equal";

        URI resourceDesignatorType = null;
		try {
			resourceDesignatorType = new URI("http://www.w3.org/2001/XMLSchema#anyURI");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        URI resourceDesignatorId = null;
		try {
			resourceDesignatorId = new URI("urn:oasis:names:tc:xacml:1.0:resource:resource-id");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        AttributeDesignator resourceDesignator =
            new AttributeDesignator(AttributeDesignator.RESOURCE_TARGET,
                                    resourceDesignatorType,
                                    resourceDesignatorId, false);


        	AnyURIAttribute resourceValue = null;
        	try {
        		resourceValue = new AnyURIAttribute(new URI(resourceID.get(i)));
        	} catch (URISyntaxException e) {
        		e.printStackTrace();
        	}

        	resource.add(createTargetMatch(TargetMatch.RESOURCE, resourceMatchId,
        			resourceDesignator, resourceValue));

        resources.add(resource);
        }

         return new Target(subjects, resources, null);		
	}
	
	private TargetMatch createTargetMatch(int type, String functionId,
            AttributeDesignator designator,
            AttributeValue value) {
		try {
			FunctionFactory factory = FunctionFactory.getTargetInstance();
			Function function = factory.createFunction(functionId);

			return new TargetMatch(type, function, designator, value);
		} catch (Exception e) {
			return null;
		}
	}	
}