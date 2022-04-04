package zb.hermes.customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerDao {
    void create(Customer customer);
    List<Customer> read();
    void update(Customer customer);
    void delete(UUID id);
    Optional<Customer> readById(UUID id);
}