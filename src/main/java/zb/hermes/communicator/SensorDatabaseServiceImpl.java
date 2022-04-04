package zb.hermes.communicator;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zb.hermes.database.*;
import zb.hermes.sensor.Sensor;
import zb.hermes.sensor.SensorType;

import java.time.Instant;
import java.util.UUID;

public class SensorDatabaseServiceImpl extends SensorDatabaseServiceGrpc.SensorDatabaseServiceImplBase {
    private final Logger _log = LoggerFactory.getLogger(SensorDatabaseServiceImpl.class);

    @Override
    public void create(CreateRequest request, StreamObserver<Empty> responseObserver) {
//        _log.info("Received new sensor data:\n{");
//        _log.info("\tsensorId: " + request.getSensorId());
//        _log.info("\tcustomerId: " + request.getCustomerId());
//        _log.info("\ttype: " + request.getSensorType());
//        _log.info("\ttimestamp: " + request.getSensorTimestamp());
//        _log.info("\tacquisitionTimestamp: " + request.getAcquisitionTimestamp());
//        _log.info("\tdata: " + request.getData());
//        _log.info("}");
//        final UUID customerId = UUID.fromString(request.getCustomerId());
//        final UUID sensorId = UUID.fromString(request.getSensorId());
//        final SensorType type = request.getSensorType();
//        final long timestamp = request.getSensorTimestamp();
//        final long acquisitionTimestamp = request.getAcquisitionTimestamp();
//        final int data = request.getData();

        Sensor sensor = new Sensor();
        sensor.id = request.getSensorId();
        sensor.customerId = request.getCustomerId();
        sensor.type = request.getSensorType();
        sensor.timestamp = Instant.ofEpochMilli(request.getSensorTimestamp());
        sensor.acquisitionTime = Instant.ofEpochMilli(request.getAcquisitionTimestamp());
        sensor.data = request.getData();

        InfluxDbHandler.getInstance().create(sensor);

        _log.info("create onNext");
        responseObserver.onNext(Empty.newBuilder().build());
        _log.info("create onCompleted");
        responseObserver.onCompleted();
    }

    @Override
    public void read(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {

    }

    @Override
    public void update(UpdateRequest request, StreamObserver<Empty> responseObserver) {

    }

    @Override
    public void delete(DeleteRequest request, StreamObserver<Empty> responseObserver) {

    }

    @Override
    public StreamObserver<CreateRequest> createMultiple(StreamObserver<Empty> responseObserver) {
        return null;
    }

    @Override
    public StreamObserver<ReadRequest> readMultiple(StreamObserver<ReadResponse> responseObserver) {
        return null;
    }

    @Override
    public StreamObserver<UpdateRequest> updateMultiple(StreamObserver<Empty> responseObserver) {
        return null;
    }

    @Override
    public StreamObserver<DeleteRequest> deleteMultiple(StreamObserver<Empty> responseObserver) {
        return null;
    }
}
