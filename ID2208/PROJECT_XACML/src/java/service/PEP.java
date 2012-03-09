
package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Policy Enforcment Point (PEP) 
 */
public class PEP {
	private String pdpIP;
	private int pdpPort;
	
	/**
	 * 
	 * @param pdpIP The ip address of a PDP
	 * @param pdpPort The port number of PDP
	 */
	PEP( String pdpIP, int pdpPort ) {
		this.pdpIP = pdpIP;
		this.pdpPort = pdpPort;
	}
	
	/**
	 * 
	 * @param request Authorization request
	 * @return Authorization response
	 */
	AuthorizationResponse evaluate( AuthorizationRequest request ) {
		AuthorizationResponse response = null;
		
		try {
			Socket socket = new Socket(pdpIP, pdpPort);			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(request);
			oos.flush();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());			
			try {
				response = (AuthorizationResponse)ois.readObject();				
				ois.close();
				oos.close();
				socket.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return response;
	}
}