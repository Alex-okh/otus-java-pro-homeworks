package ATM;

import enums.Banknote;
import exceptions.ATMException;

import java.util.ArrayList;
import java.util.List;

public class CellImplementation implements CellInterface {
    private final List<Banknote> banknotes;
    private final int nominal;

    public CellImplementation(int nominal) {
        this.nominal = nominal;
        this.banknotes = new ArrayList<>();
    }

    @Override
    public void putBanknote(Banknote banknote) {
        if (banknote.getValue() == this.nominal) {
            banknotes.add(banknote);
        } else {
            throw new ATMException("The cell %d received wrong banknote (%d)".formatted(this.nominal, banknote.getValue()));
        }
    }

    @Override
    public Banknote getBanknote() {
        if (banknotes.isEmpty()) {
            throw new ATMException("The cell %d requested for banknote, it is empty.".formatted(this.nominal));
        } else {
            return banknotes.removeFirst();
        }
    }

    @Override
    public int getNominal() {
        return nominal;
    }

    @Override
    public int getQuantity() {
        return banknotes.size();
    }
}
