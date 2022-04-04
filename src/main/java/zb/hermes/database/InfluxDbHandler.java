package zb.hermes.database;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.*;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zb.hermes.sensor.Sensor;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class InfluxDbHandler {
    private static InfluxDbHandler _instance;
    private InfluxDBClient _client;
    private InfluxDBClientOptions _options;
    private final Logger _log;

    private InfluxDbHandler() {
        _log = LoggerFactory.getLogger(InfluxDbHandler.class);
        _log.info("InfluxDbHandler constructed on " + Thread.currentThread());
    }

    static {
        _instance = new InfluxDbHandler();
    }

    public static InfluxDbHandler getInstance() {
        return _instance;
    }

    public void init(final String ip,
                     final int port,
                     final String user,
                     final String password,
                     final String bucket,
                     final String organization) {

        _options = new InfluxDBClientOptions.Builder()
                .url("http://" + ip + ":" + port)
                .authenticate(user, password.toCharArray())
                .bucket(bucket)
                .org(organization)
                .build();
    }

    public void connect() {
        _log.info("Connecting to InfluxDB @ " + _options.getUrl());
        _client = InfluxDBClientFactory.create(_options);
        _log.info("");
    }

    public void close() {
        _log.info("Closing InfluxDB on " + _options.getUrl());
        _client.close();
    }

    public void create(@NotNull final Sensor sensor) {
        WriteApiBlocking write = _client.getWriteApiBlocking();
        sensor.dbInsertionTime = Instant.now();
        _log.info("Writing data points: " + sensor);
        write.writeMeasurement(WritePrecision.MS, sensor);
    }

    public List<Sensor> read() {
        final String sensorString = "sensor";
        final String flux = "from(bucket:\"" + _options.getBucket() + "\") " +
                "|> range(start: 0) " +
                "|> filter(fn: (r) => r._measurement == \"" + sensorString + "\")";

        QueryApi queryApi = _client.getQueryApi();

//        List<FluxTable> tables = queryApi.query(flux);
//        for (FluxTable fluxTable: tables) {
//            List<FluxRecord> records = fluxTable.getRecords();
//            System.out.print("Time: " + records.get(0).getTime() + ": ");
//            for (FluxRecord fluxRecord : records) {
//                System.out.print(fluxRecord.getValueByKey("_value") + ", ");
//                _log.info(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"));
//            }
//            _log.info();
//        }

        List<FluxTable> tables = queryApi.query(flux);
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                _log.info(fluxRecord.getTime() + ": " + fluxRecord.getValueByKey("_value"));
            }
        }

//        List<Sensor> sensors = queryApi.query(flux, Sensor.class);
//        for (Sensor sensor: sensors) {
//            _log.info(sensor);
//        }

        queryApi.query(flux, Sensor.class,
                (cancellable, sensor) -> {
                    //process record
                    _log.info(sensor.toString());
                }
                , error -> {
                    //handle error
                    _log.error(error.getLocalizedMessage());
                }, () -> {
                    _log.info("Query completed.");
                });

        return null;
    }

    public void remove() {
        DeleteApi deleteApi = _client.getDeleteApi();
        deleteApi.delete(OffsetDateTime.now().minus(60, ChronoUnit.DAYS),
                OffsetDateTime.now(),
                "",
                _options.getBucket(),
                _options.getOrg());
    }
}