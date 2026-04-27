package com.example.ondas_be.application.service.port;

import com.example.ondas_be.application.dto.response.HomeResponse;

public interface HomeServicePort {

    HomeResponse getHome(int trendingLimit, int artistLimit, int albumLimit);
}
