/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.controller;

import converter.model.CurrencyDTO;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author julio
 */
@Stateless
@LocalBean
public class ConverterFacade {

    public List<CurrencyDTO> getCurrencyList() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    
    
}
