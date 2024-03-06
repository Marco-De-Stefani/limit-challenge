package com.deste.gateway.domain.knowledgegraph;

import com.deste.gateway.domain.limit.LimitService;
import com.deste.gateway.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class KeyServiceTest {
    @InjectMocks
    private KeyService keyService;
    @Mock
    private KeyRepository keyRepo;
    @Mock
    private LimitService limitService;

    @Test
    void isKeyLimitReachedCallsRepoWithCorrectKey() {
        var key = Key.builder().build();
        when(keyRepo.findByName("keyName")).thenReturn(List.of(key));
        when(limitService.isKeyLimitReached(key, "api_white")).thenReturn(true);

        boolean result = keyService.isKeyLimitReached("keyName", "api_white");
        assertTrue(result);
    }

    @Test
    void isValidKeyCallsRepoAndChecksIfUserHasKnowledgeGraph() {
        User user = User.builder().email("mario@rossi.it").build();
        var key = Key.builder()
                .knowledgeGraph(KnowledgeGraph.builder()
                        .user(user)
                        .build())
                .build();
        when(keyRepo.findByName("keyName")).thenReturn(List.of(key));

        boolean result = keyService.isValidKey("keyName");
        assertTrue(result);
    }

    @Test
    void getConsumedForCallsRepoAndReturns() {
        var key = Key.builder().build();
        when(keyRepo.findByName("keyName")).thenReturn(List.of(key));
        when(limitService.getRemainingLimit(key, "api_white")).thenReturn("1");

        String result = keyService.getConsumedFor("keyName", "api_white");
        assertEquals("1",result);
    }

    @Test
    void getTotalForCallsRepoAndReturns() {
        var key = Key.builder().build();
        when(keyRepo.findByName("keyName")).thenReturn(List.of(key));
        when(limitService.getTotalLimit(key, "api_white")).thenReturn("1");

        String result = keyService.getTotalFor("keyName", "api_white");
        assertEquals("1",result);
    }

}