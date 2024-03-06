package com.deste.gateway.configuration;

import com.deste.gateway.domain.limit.Limit;
import com.deste.gateway.domain.limit.LimitRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "test")
class MonthlyRateResetTest {

    @Autowired
    private MonthlyRateReset monthlyRateReset;

    @Autowired
    private LimitRepository limitRepository;

    @Test
    void resetAllRates() {
        Limit limit = Limit.builder().setLimit(10L).remainingLimit(5L).build();
        limitRepository.save(limit);

        monthlyRateReset.resetRates();

        List<Limit> limits = limitRepository.findAll();
        assertEquals(1, limits.size());
        Limit actual = limits.get(0);

        assertEquals(10L, actual.getRemainingLimit());
        assertEquals(10L, actual.getSetLimit());

    }
}