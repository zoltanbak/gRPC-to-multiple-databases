package zb.hermes.database;

import zb.hermes.customer.Customer;

import org.jetbrains.annotations.NotNull;
import zb.hermes.sensor.Sensor;
import zb.hermes.sensor.SensorType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

enum state {
    CLOSED,
    CONNECTED,
}

public class PostgreSqlHandler {
    private static final String _connectorType = "jdbc";
    private static final String _dbType = "postgresql";
    private static  String _ip;
    private static int _port;
    private static String _dbName = "demo";
    private static final String _optional = "useSSL=false";
    private static String _user;
    private static String _password;
    private String _options;
    private Connection _connection = null;
    private Statement _statement = null;
    private PreparedStatement _preparedStatement = null;
    private CallableStatement _callableStatement = null;
    private ResultSet _resultSet = null;

    private static PostgreSqlHandler _instance;

    private PostgreSqlHandler() {
        System.out.println("PostgreSqlHandler constructed on " + Thread.currentThread());
    }

    static {
        _instance = new PostgreSqlHandler();
    }

    public static PostgreSqlHandler getInstance() {
        return _instance;
    }

    public void init(final String ip,
                     final int port,
                     final String user,
                     final String password,
                     final String dbName) {
        _ip = ip;
        _port = port;
        _dbName = dbName;
        _user = user;
        _password = password;
        _options = _connectorType + ":" +
                _dbType + "://" +
                ip + ":" +
                port + "/" +
                dbName + "?" +
                _optional;
    }

    public void connect() throws SQLException {
        System.out.println("Postgres connecting to " + _dbName + "@" + _ip + ":" + _port);
        _connection = DriverManager.getConnection(_options, _user, _password);
        System.out.println("Connected to Postgres@" + _ip + ":" + _port + "successfully!");
    }

    public void close() throws SQLException {
        System.out.println("Closing PostgreSqlHandler connection on " + _ip + ":" + _port);
        if (_resultSet != null) {
            _resultSet.close();
        }

        if (_statement != null) {
            _statement.close();
        }

        if (_preparedStatement != null) {
            _preparedStatement.close();
        }

        if (_callableStatement != null) {
            _callableStatement.close();
        }

        if (_connection != null) {
            _connection.close();
        }
    }

    public void create(@NotNull final Customer customer) throws SQLException {
        // TODO: if not connected
        final String sqlStatement = "INSERT INTO " + //_dbName + "." +
                "customers " +
                "(id) VALUES ('" + customer.id.toString() + "')";
//        _preparedStatement = _connection.prepareStatement(
//                "insert into ?.customers " +
//                        "(id) " +
//                        "values " +
//                        "('?')");
//
//        _preparedStatement.setString(1, _dbName);
//        _preparedStatement.setString(2, customer.id.toString());
//
//        _preparedStatement.executeUpdate();
        _statement = _connection.createStatement();
        _statement.execute(sqlStatement);
    }

    public void create(final List<Customer> customers) throws SQLException {
        for (final Customer customer: customers) {
            _preparedStatement = _connection.prepareStatement(
                    "insert into ?.customers " +
                            "(id) " +
                            "values " +
                            "('?')");

            _preparedStatement.setString(1, _dbName);
            _preparedStatement.setString(2, customer.id.toString());

            _preparedStatement.executeUpdate();
        }
    }

    public void create(@NotNull final Sensor sensor) throws SQLException {
        // TODO: if not connected
        // TODO: Create final string to be able to print it out
        _preparedStatement = _connection.prepareStatement(
                "INSERT INTO ?.sensors " +
                        "(id, customer_id, type) " +
                        "VALUES ('?', '?', '?', " +
                        "(SELECT id FROM ?.customers WHERE id = ?) " +
                        ")"); // insert if customerid exists

        _preparedStatement.setString(1, _dbName);
        _preparedStatement.setString(2, sensor.id.toString());
        _preparedStatement.setString(3, sensor.customerId.toString());
        _preparedStatement.setString(4, sensor.type.toString());
        _preparedStatement.setString(5, _dbName);
        _preparedStatement.setString(6, sensor.customerId.toString());

        _preparedStatement.executeUpdate();
    }

    public List<Customer> readCustomers() throws SQLException {
        // Prepare statement
        _preparedStatement = _connection
                .prepareStatement("select * from " + _dbName + ".customers");

        // Execute SQL query
        _resultSet = _preparedStatement.executeQuery();

        List<Customer> customers = new ArrayList<>();

        // Process result set
        while (_resultSet.next()) {
            Customer customer = new Customer();
            customer.id = UUID.fromString(_resultSet.getString("id"));
            System.out.println("ReadCustomer:" + customer);
            customers.add(customer);
        }

        return customers;
    }

    public List<Sensor> readSensors() throws SQLException {
        // Prepare statement
        _preparedStatement = _connection
                .prepareStatement("select * from " + _dbName + ".sensors");

        // Execute SQL query
        _resultSet = _preparedStatement.executeQuery();

        List<Sensor> sensors = new ArrayList<>();

        // Process result set
        while (_resultSet.next()) {
            Sensor sensor = new Sensor();
//            sensor.id = UUID.fromString(_resultSet.getString("id"));
//            sensor.customerId = UUID.fromString(_resultSet.getString("customer_id"));
//            sensor.type = SensorType.valueOf(_resultSet.getString("type"));

            sensors.add(sensor);
        }

        return sensors;
    }


    public void update(@NotNull final Customer customer) {

    }

    public void remove() {
    }
}