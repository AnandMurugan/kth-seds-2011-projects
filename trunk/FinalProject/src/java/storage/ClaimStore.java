/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storage;

import bean.Claim;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Anand
 */
public class ClaimStore {
    private static Map<Long, Claim> store;

    public static Map<Long, Claim> getStore() {
        return store;
    }
    
    public ClaimStore(){
        store = new HashMap<Long, Claim>();
    }
    
}
