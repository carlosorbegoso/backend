package com.skyblue.backend.tracing.service;


import com.skyblue.backend.security.service.MarkDataService;
import com.skyblue.backend.tracing.models.Vehicle;
import com.skyblue.backend.tracing.models.Wheels;
import com.skyblue.backend.tracing.repository.VehicleRepository;
import com.skyblue.backend.tracing.repository.WheelsRepository;
import com.skyblue.backend.utils.PageSupport;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final WheelsRepository wheelsRepository;

    private final MarkDataService mark;
    private Mono<Vehicle> addWheels(Vehicle vehicle) {
        return wheelsRepository.findByVehicleId(vehicle.getId())
                .collectList()
                .map(wheels -> {
                    vehicle.setWheels(wheels);
                    return vehicle;
                });
    }


    private Mono<Vehicle> saveWheels(Vehicle vehicle) {
        List<Wheels> wheels = vehicle.getWheels();
        if(wheels == null || wheels.isEmpty())
            return Mono.just(vehicle);
        return Mono.from(
                Flux.fromIterable(wheels)
                        .log("llantas"+vehicle.getWheels().toString())
                        .flatMap(wheelsRepository::save)
                        .collectList()
                        .then(Mono.just(vehicle))
        );
    }
    private Mono<Vehicle> checkWheels(List<Wheels> wheels, Long vehicleId) {
        System.out.println( "checkWheels"+wheels.toString());
    return null;
    }



   public Mono<PageSupport<Vehicle>> listVehiclesPage(Pageable page){
       return vehicleRepository.findAll()
               .flatMap(this::addWheels)
               .collectList()
               .map(list -> new PageSupport<>(
                       list
                               .stream()
                               .skip((long) page.getPageNumber() * page.getPageSize())
                               .limit(page.getPageSize())
                               .collect(Collectors.toList()),
                       page.getPageNumber(), page.getPageSize(), list.size()));
   }

   private Mono<Vehicle> insert(Vehicle vehicle) {
        return Mono.just(vehicle)
                .flatMap(vehicleRepository::save)
                .map(it -> it.withWheels(vehicle.getWheels()))
                .flatMap(this::saveWheels);
   }

   public Mono<Vehicle> save(Vehicle vehicle) {
        return insert(vehicle);
   }


}
