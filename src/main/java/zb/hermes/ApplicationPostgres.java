package zb.hermes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zb.hermes.database.PostgreSqlHandler;
import zb.hermes.customer.Customer;

import java.sql.SQLException;
import java.util.UUID;

public class ApplicationPostgres {
    public static void main(String[] args) throws SQLException {
        final Logger log = LoggerFactory.getLogger(ApplicationPostgres.class);
        log.info("ApplicationPostgres started on " + Thread.currentThread().toString());

        PostgreSqlHandler postgreSqlHandler = PostgreSqlHandler.getInstance();
        postgreSqlHandler.init("192.168.3.202",
                5432,
                "zbuser",
                "password",
                "customer");

        postgreSqlHandler.connect();

        Customer customer = new Customer();
        customer.id = UUID.randomUUID();

        postgreSqlHandler.create(customer);

        postgreSqlHandler.readCustomers();

        postgreSqlHandler.close();
    }
}
