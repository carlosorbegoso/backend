package com.skyblue.backend.tracing.repository;

import com.skyblue.backend.tracing.models.Wheels;

import org.reactivestreams.Subscriber;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface WheelsRepository extends ReactiveCrudRepository<Wheels,Long> {
    Flux<Wheels> findByVehicleId(Long vehicleId);

}
