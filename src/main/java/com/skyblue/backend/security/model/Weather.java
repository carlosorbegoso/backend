package com.skyblue.backend.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    Long id;
    LocalDateTime date;
    Long direction;
    Long speed;
    Long temperature;
}
