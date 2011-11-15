/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

/**
 *
 * @author Igor
 */
public interface BankClient {
    public Account createAccount();

    public Account getAccount();

    public boolean deleteAccount();

    public void deposit(float value);

    public void withdraw(float value);

    public float balance();
}
