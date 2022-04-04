package zb.hermes;

import io.grpc.BindableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zb.hermes.communicator.SensorDatabaseServiceImpl;
import zb.hermes.database.InfluxDbHandler;
import zb.hermes.sensor.Sensor;
import zb.hermes.sensor.SensorType;
import zb.hermes.util.GrpcServer;

import java.time.Instant;
import java.util.UUID;

public class ApplicationInflux {
    public static void main(String[] args) throws InterruptedException {
        final Logger log = LoggerFactory.getLogger(ApplicationInflux.class);
        log.info("ApplicationInflux started on " + Thread.currentThread().toString());

        InfluxDbHandler influxDbHandler = InfluxDbHandler.getInstance();
        influxDbHandler.init("localhost",
                8086,
                "zbuser",
                "password",
                "mybucket",
                "myorg");

        influxDbHandler.connect();

        influxDbHandler.remove();

        Sensor sensor = new Sensor();
        sensor.id = "1";
        sensor.customerId = "2";
        sensor.type = SensorType.TEMPERATURE;
        sensor.timestamp = Instant.now();
        sensor.acquisitionTime = Instant.now();
        sensor.dbInsertionTime = Instant.now();
        sensor.data = 42;

        influxDbHandler.create(sensor);

        GrpcServer grpcServer = new GrpcServer(18086, new SensorDatabaseServiceImpl());
        grpcServer.start();

//        Thread.sleep(3000);
//
//        Sensor sensor = new Sensor();
//        sensor.id = UUID.randomUUID().toString();
//        sensor.customerId = UUID.randomUUID().toString();
//        sensor.type = SensorType.TEMPERATURE;
//        sensor.timestamp = Instant.now();
//        sensor.acquisitionTime = Instant.now();
//        sensor.dbInsertionTime = Instant.now();
//        sensor.data = 42;
//
//        influxDbHandler.create(sensor);
//
//        Sensor sensor2 = new Sensor();
//        sensor2.id = UUID.randomUUID().toString();
//        sensor2.customerId = UUID.randomUUID().toString();
//        sensor2.type = SensorType.HUMIDITY;
//        sensor2.timestamp = Instant.now();
//        sensor2.acquisitionTime = Instant.now();
//        sensor2.dbInsertionTime = Instant.now();
//        sensor2.data = 43;
//
//        influxDbHandler.create(sensor2);
//
//        influxDbHandler.read();

        influxDbHandler.close();
    }
}
