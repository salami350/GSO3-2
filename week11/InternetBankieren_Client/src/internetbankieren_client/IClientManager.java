/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetbankieren_client;

/**
 *
 * @author Stefan
 */
public interface IClientManager {
    
    /**
     *@precondities: property is veranderd binnen de registry
     *@postconditie: account wordt gepolled
     */
    public void propertyChanged();
    
}
