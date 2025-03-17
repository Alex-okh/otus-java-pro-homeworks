package homework;

import java.util.AbstractMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class CustomerService {
    private final NavigableMap<Customer, String> customers = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallest = customers.firstEntry();
        return smallest == null ? null :
            new AbstractMap.SimpleEntry<>(new Customer(smallest.getKey().getId(), smallest.getKey().getName(), smallest.getKey().getScores()), smallest.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> next = customers.higherEntry(customer);

        return next ==null ? null :
            new AbstractMap.SimpleEntry<>(new Customer(next.getKey().getId(), next.getKey().getName(), next.getKey().getScores()), next.getValue());

    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
