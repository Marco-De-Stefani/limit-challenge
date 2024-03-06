package com.deste.gateway.configuration;

import com.deste.gateway.domain.limit.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyRateReset {
    private final LimitService service;
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetRates() {
        service.resetAllRates();
    }

}
