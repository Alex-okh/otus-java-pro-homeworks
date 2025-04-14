package ATM;

import enums.Banknote;
import exceptions.ATMException;

import java.util.*;

public class StorageImplementation implements StorageInterface {
    private final Map<Integer, CellImplementation> cells;

    public StorageImplementation() {
        cells = new TreeMap<>(Comparator.comparing(Integer::intValue, Comparator.reverseOrder()));
        for (Banknote b : Banknote.values()) {
            cells.put((b.getValue()), new CellImplementation(b.getValue()));
        }
    }

    @Override
    public void store(Banknote banknote) {
        CellImplementation cell = cells.get(banknote.getValue());
        cell.putBanknote(banknote);
    }

    @Override
    public Collection<Banknote> retrieve(int value) {
        Collection<Banknote> extracted = new ArrayList<>();
        for (Map.Entry<Integer, CellImplementation> e : cells.entrySet()) {
            while (e.getKey() <= value && cells.get(e.getKey()).getQuantity() > 0) {
                extracted.add(cells.get(e.getKey()).getBanknote());
                value -= e.getKey();

            }
        }
        if (value > 0) {
            for (Banknote b : extracted) {
                store(b);
            }
            throw new ATMException("Cannot retrieve sum");
        }
        return extracted;

    }

    @Override
    public int getTotalSum() {
        int sum = 0;
        for (CellImplementation cell : cells.values()) {
            sum += cell.getNominal() * cell.getQuantity();
        }
        return sum;
    }

}
