package zb.hermes.customer;

import java.util.List;
import java.util.UUID;

public class Customer {
    public UUID id;
    List<UUID> sensors;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id.toString() +
                ", sensors=" + sensors +
                '}';
    }
}
