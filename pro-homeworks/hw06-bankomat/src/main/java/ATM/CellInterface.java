package ATM;

import enums.Banknote;

public interface CellInterface {
    public void putBanknote(Banknote banknote);
    public Banknote getBanknote();
    public int getNominal();
    public int getQuantity();
}
