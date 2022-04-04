package zb.hermes.sensor;

import java.util.List;
import java.util.UUID;

public interface SensorDao {
    int create(Sensor sensor);
    List<Sensor> read();
    int update(Sensor sensor);
    int insert(Sensor sensor);
    List<Sensor> readByInterval(UUID id, long from, long to);
    List<Sensor> readById(UUID id);
    List<Sensor> readByCustomerId(UUID id);
    List<Sensor> readByType(SensorType type);
}
