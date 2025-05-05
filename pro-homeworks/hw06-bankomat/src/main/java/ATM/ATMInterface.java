package ATM;

import enums.Banknote;

import java.util.Collection;

public interface ATMInterface {
    public void loadMoney(Collection<Banknote> banknotes);
    public Collection<Banknote> getMoney(int sum);
    public String getBalance();

}
