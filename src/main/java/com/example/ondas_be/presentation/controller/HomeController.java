package com.example.ondas_be.presentation.controller;

import com.example.ondas_be.application.dto.common.ApiResponse;
import com.example.ondas_be.application.dto.response.HomeResponse;
import com.example.ondas_be.application.service.port.HomeServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeServicePort homeServicePort;

    @GetMapping
    public ResponseEntity<ApiResponse<HomeResponse>> getHome(
            @RequestParam(defaultValue = "10") int trendingLimit,
            @RequestParam(defaultValue = "10") int artistLimit,
            @RequestParam(defaultValue = "10") int albumLimit) {
        return ResponseEntity.ok(ApiResponse.success(
                homeServicePort.getHome(trendingLimit, artistLimit, albumLimit)));
    }
}
