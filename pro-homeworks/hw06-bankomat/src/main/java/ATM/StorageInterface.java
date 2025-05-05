package ATM;

import enums.Banknote;

import java.util.Collection;

public interface StorageInterface {
    public void store(Banknote banknote);
    public Collection<Banknote> retrieve(int value);
    public int getTotalSum();
}
