import ATM.ATMImplementation;
import enums.Banknote;

import java.util.ArrayList;
import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        ATMImplementation bankomat = new ATMImplementation();

        Collection<Banknote> pack = new ArrayList<>();
        pack.add(Banknote.FIVE_HUNDRED);
        pack.add(Banknote.FIFTY);
        pack.add(Banknote.FIVE_THOUSAND);
        bankomat.loadMoney(pack);

        System.out.println(bankomat.getBalance());
        bankomat.getMoney(500);
        System.out.println(bankomat.getBalance());
        bankomat.getMoney(100);
        System.out.println(bankomat.getBalance());
    }
}
