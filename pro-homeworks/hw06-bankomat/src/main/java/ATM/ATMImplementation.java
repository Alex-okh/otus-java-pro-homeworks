package ATM;

import enums.Banknote;
import exceptions.ATMException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ATMImplementation implements ATMInterface {
    StorageInterface moneyBank;

    public ATMImplementation() {
        this.moneyBank = new StorageImplementation();
    }

    @Override
    public void loadMoney(Collection<Banknote> banknotes) {
        for (Banknote banknote : banknotes) {
            moneyBank.store(banknote);
        }

    }

    @Override
    public Collection<Banknote> getMoney(int sum) {
        Collection<Banknote> extracted = new ArrayList<>();
        try {
            extracted = moneyBank.retrieve(sum);
            return extracted;
        } catch (ATMException ex) {
            System.out.println("Error occured: %s".formatted(ex.getMessage()));

        }
        return Collections.emptyList();
    }

    @Override
    public String getBalance() {
        return "Sum of money in ATM is %d".formatted(moneyBank.getTotalSum());
    }
}
