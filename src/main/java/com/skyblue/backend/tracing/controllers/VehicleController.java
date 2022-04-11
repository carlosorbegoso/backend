package com.skyblue.backend.tracing.controllers;

import com.skyblue.backend.tracing.models.Vehicle;
import com.skyblue.backend.tracing.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("Vehicle/")
@Slf4j

public class VehicleController {
     VehicleRepository vehicleRepository;

    @GetMapping("list")
    public Mono<ResponseEntity<Flux<Vehicle>>> listVehicles(){
        Flux<Vehicle> vehicleFlux = vehicleRepository.findAll();

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(vehicleFlux));
    }


}
