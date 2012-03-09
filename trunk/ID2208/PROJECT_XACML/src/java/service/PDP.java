package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.xacml.PDPConfig;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.RFC822NameAttribute;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;
import com.sun.xacml.ctx.Subject;
import com.sun.xacml.finder.AttributeFinder;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.impl.CurrentEnvModule;
import com.sun.xacml.finder.impl.FilePolicyModule;

/**
 * Policy Decision Point (PDP) 
 */
class PDP {
	/** Decision after evaluation by PDP: Permit. */
	public static final int DECISION_PERMIT = Result.DECISION_PERMIT;
	/** Decision after evaluation by PDP: Deny. */
	public static final int DECISION_DENY = Result.DECISION_DENY;
	/** Decision after evaluation by PDP: Indeterminate. */
	public static final int DECISION_INDETERMINATE = Result.DECISION_INDETERMINATE;
	/** Decision after evaluation by PDP: Not applicable. */
	public static final int DECISION_NOT_APPLICABLE = Result.DECISION_NOT_APPLICABLE;

	private com.sun.xacml.PDP pdp;
	private String path;	
	
	/**
	 * 
	 * @param policyPath The path to policy files.
	 */
	PDP( String policyPath ) {
		path = policyPath;
		init();
	}
	/**
	 * You can also load policies from a folder containing all policies
	 */
	private void init() {
		File file = new File(path);
		String[] policyFiles = file.list();
		
		FilePolicyModule filePolicyModule = new FilePolicyModule();
		
		for( String fileName : policyFiles )
			filePolicyModule.addPolicy(path + File.separator + fileName);		
		
		CurrentEnvModule envModule = new CurrentEnvModule();
				
		Set policyModules = new HashSet();
		policyModules.add(filePolicyModule);
		
		PolicyFinder policyFinder = new PolicyFinder();
		policyFinder.setModules(policyModules);
				
		List attrModules = new ArrayList();
		attrModules.add(envModule);
		AttributeFinder attrFinder = new AttributeFinder();
		attrFinder.setModules(attrModules);
		
		PDPConfig pdpConfig = new PDPConfig(attrFinder, policyFinder, null);
		pdp = new com.sun.xacml.PDP(pdpConfig);		
	}
	
	/**
	 * Add a new policy
	 * @param policy New policy to be added.
	 */
	void addPolicy( Policy policy ) {
		File file = new File(path + File.separator + policy.getPolicyID() + ".xml");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(policy.getPolicy().toString().getBytes());
			fos.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		init();
	}
	
	/**
	 * Evaluate a request
	 * @param request Authorization request
	 * @return Authorization response
	 */
	AuthorizationResponse evaluate( AuthorizationRequest request ) {		
        URI subjectId;
		try {
			subjectId = new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
        RFC822NameAttribute value = new RFC822NameAttribute(request.getUserID());
        HashSet attributes = new HashSet();
        attributes.add(new Attribute(subjectId, null, null, value));        
        try {
			attributes.add(new Attribute(new URI("group"), request.getGroupAdminID(), null, new StringAttribute( request.getGroupID() )));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
        HashSet subjects = new HashSet();
        subjects.add(new Subject(attributes));
        
        AnyURIAttribute resValue;
        try {
			resValue = new AnyURIAttribute(new URI(request.getResourceID()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
        HashSet resource = new HashSet();        
        try {
			resource.add(new Attribute(new URI("urn:oasis:names:tc:xacml:1.0:resource:resource-id"), null, null, resValue));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
        
        URI actionId;
		try {
			actionId = new URI("urn:oasis:names:tc:xacml:1.0:action:action-id");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
        HashSet action = new HashSet();
        action.add(new Attribute(actionId, null, null, new StringAttribute(request.getActionID())));
        
        RequestCtx requestCtx = new RequestCtx(subjects, resource, action, new HashSet());
		ResponseCtx responseCtx = pdp.evaluate(requestCtx);
		AuthorizationResponse response = new AuthorizationResponse(responseCtx);
		
		return response;
	}
}
