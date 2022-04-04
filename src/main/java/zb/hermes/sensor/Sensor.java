package zb.hermes.sensor;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;
import java.util.UUID;

@Measurement(name = "sensor")
public class Sensor {

    @Column(tag = true, name = "id")
//    @Column(name = "id")
    public String id;

    @Column(name = "customerId")
    public String customerId;

    @Column(name = "type")
    public SensorType type;

    @Column(timestamp = true)
    public Instant timestamp;

    @Column(name = "acquisitionTime")
    public Instant acquisitionTime;

    @Column(name = "dbInsertionTime")
    public Instant dbInsertionTime;

    @Column(name = "data")
    public int data;

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + String.valueOf(id).toString() +
                ", customerId=" + String.valueOf(customerId).toString() +
                ", type=" + String.valueOf(type).toString() +
                ", timestamp=" + timestamp +
                ", acquisitionTime=" + acquisitionTime +
                ", dbInsertionTime=" + dbInsertionTime +
                ", data=" + data +
                '}';
    }
}
