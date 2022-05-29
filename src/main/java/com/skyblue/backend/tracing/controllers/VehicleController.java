package com.skyblue.backend.tracing.controllers;
import com.skyblue.backend.security.model.dto.SysHttpResponse;
import com.skyblue.backend.tracing.models.Vehicle;
import com.skyblue.backend.tracing.service.VehicleService;
import com.skyblue.backend.utils.PageSupport;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.skyblue.backend.utils.PageSupport.FIRST_PAGE_NUM;
import static com.skyblue.backend.utils.PageSupport.DEFAULT_PAGE_SIZE;


@RestController
@RequestMapping("/api/vehicle")
@Slf4j
@AllArgsConstructor
public class VehicleController {
	private final VehicleService vehicleService;



	//@Operation(summary = "Get all vehicles ")
	public Mono<PageSupport<Vehicle>> listVehicles(
			@RequestParam(name = "page", defaultValue = FIRST_PAGE_NUM) int page,
			@RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
		return vehicleService.listVehiclesPage(PageRequest.of(page, size));
	}
	@PostMapping(path = "all")
	public List<Vehicle> listVehicles() {
		return vehicleService.list();
	}

	@PostMapping(path ="save", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	Mono <SysHttpResponse> save(@RequestBody Vehicle vehicle) {
		return vehicleService.save(vehicle)
				.map(it -> SysHttpResponse
						.builder()
						.status(HttpStatus.OK.value())
						.message("Stored successfully")
						.data(it)
						.build())
				.onErrorResume(err -> {
					SysHttpResponse response = new SysHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Error running",err.getMessage());
					if(err instanceof DataIntegrityViolationException){
						response = new SysHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Some field of stored data are not unique",err.getMessage());

					}
					return Mono.just(response);
				});

	}


}
