package com.deste.gateway.domain.limit;

import com.deste.gateway.domain.knowledgegraph.Key;
import com.deste.gateway.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LimitServiceTest {
    @InjectMocks
    private LimitService limitService;

    @Mock
    private LimitRepository limitRepository;

    @Captor
    ArgumentCaptor<Iterable<Limit>> limitsCaptor;

    @Captor
    ArgumentCaptor<Limit> limitCaptor;

    @Test
    void resetAllRatesTest() {
        Limit limit = Limit.builder().setLimit(10L).remainingLimit(1L).build();
        when(limitRepository.findAll()).thenReturn(List.of(limit));

        limitService.resetAllRates();

        verify(limitRepository).saveAll(limitsCaptor.capture());
        var capturedLimit = limitsCaptor.getValue();

        capturedLimit.forEach(it -> {
            assertEquals(10L, it.getSetLimit());
            assertEquals(10L, it.getRemainingLimit());
        });
    }

    @Test
    void whenKeyLimitNotReachedTheRemainingIsDecremented() {
        var key = Key.builder().name("key").build();
        var limit = Limit.builder()
                .setLimit(10L)
                .remainingLimit(9L)
                .build();
        when(limitRepository.findByKeyAndApiGroup(key, "api_white")).thenReturn(List.of(limit));

        limitService.isKeyLimitReached(key, "api_white");

        verify(limitRepository).save(limitCaptor.capture());
        assertEquals(10L, limitCaptor.getValue().getSetLimit());
        assertEquals(8L, limitCaptor.getValue().getRemainingLimit());
    }

    @Test
    void whenKeyLimitReachedTheRemainingIsNotDecremented() {
        var key = Key.builder().name("key").build();
        var limit = Limit.builder()
                .setLimit(10L)
                .remainingLimit(0L)
                .build();
        when(limitRepository.findByKeyAndApiGroup(key, "api_white")).thenReturn(List.of(limit));

        limitService.isKeyLimitReached(key, "api_white");

        verify(limitRepository, times(0)).save(limitCaptor.capture());
    }

    @Test
    void whenUserLimitNotReachedTheRemainingIsDecremented() {
        var user = User.builder().build();
        var limit = Limit.builder()
                .setLimit(10L)
                .remainingLimit(9L)
                .build();
        when(limitRepository.findByUserAndApiGroup(user, "api_white")).thenReturn(List.of(limit));

        limitService.isUserLimitReached(user, "api_white");

        verify(limitRepository).save(limitCaptor.capture());
        assertEquals(10L, limitCaptor.getValue().getSetLimit());
        assertEquals(8L, limitCaptor.getValue().getRemainingLimit());
    }

    @Test
    void whenUserLimitReachedTheRemainingIsNotDecremented() {
        var user = User.builder().build();
        var limit = Limit.builder()
                .setLimit(10L)
                .remainingLimit(0L)
                .build();
        when(limitRepository.findByUserAndApiGroup(user, "api_white")).thenReturn(List.of(limit));

        limitService.isUserLimitReached(user, "api_white");

        verify(limitRepository, times(0)).save(limitCaptor.capture());
    }

    @Test
    void getTotalLimitForKeyCallsRepo() {
        var key = Key.builder().build();
        var limit = Limit.builder().setLimit(10L).build();
        when(limitRepository.findByKeyAndApiGroup(key, "api_white")).thenReturn(List.of(limit));

        String result = limitService.getTotalLimit(key, "api_white");

        assertEquals("10", result);
    }

    @Test
    void getTotalLimitForUserCallsRepo() {
        var user = User.builder().build();
        var limit = Limit.builder().setLimit(10L).build();
        when(limitRepository.findByUserAndApiGroup(user, "api_white")).thenReturn(List.of(limit));

        String result = limitService.getTotalLimit(user, "api_white");

        assertEquals("10", result);
    }

    @Test
    void getRemainingLimitForKeyCallsRepo() {
        var key = Key.builder().build();
        var limit = Limit.builder().remainingLimit(1L).build();
        when(limitRepository.findByKeyAndApiGroup(key, "api_white")).thenReturn(List.of(limit));

        String result = limitService.getRemainingLimit(key, "api_white");

        assertEquals("1", result);
    }

    @Test
    void getRemainingLimitForUserCallsRepo() {
        var user = User.builder().build();
        var limit = Limit.builder().remainingLimit(1L).build();
        when(limitRepository.findByUserAndApiGroup(user, "api_white")).thenReturn(List.of(limit));

        String result = limitService.getRemainingLimit(user, "api_white");

        assertEquals("1", result);
    }
}