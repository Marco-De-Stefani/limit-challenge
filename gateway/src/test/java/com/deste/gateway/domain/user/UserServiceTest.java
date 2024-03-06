package com.deste.gateway.domain.user;

import com.deste.gateway.domain.knowledgegraph.Key;
import com.deste.gateway.domain.knowledgegraph.KnowledgeGraph;
import com.deste.gateway.domain.limit.LimitService;
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
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LimitService limitService;

    @Test
    void isKeyLimitReachedCallsRepoWithCorrectKey() {
        var user = User.builder().build();
        when(userRepository.findByEmail("email")).thenReturn(List.of(user));
        when(limitService.isUserLimitReached(user, "api_white")).thenReturn(true);

        boolean result = userService.isUserLimitReached("email", "api_white");
        assertTrue(result);
    }

    @Test
    void isValidKeyCallsRepoAndChecksIfUserHasKnowledgeGraph() {
        User user = User.builder().email("mario@rossi.it").build();
        when(userRepository.findByEmail("email")).thenReturn(List.of(user));

        boolean result = userService.isValidUser("email");
        assertTrue(result);
    }

    @Test
    void getConsumedForCallsRepoAndReturns() {
        var user = User.builder().build();
        when(userRepository.findByEmail("email")).thenReturn(List.of(user));
        when(limitService.getRemainingLimit(user, "api_white")).thenReturn("1");

        String result = userService.getConsumedFor("email", "api_white");
        assertEquals("1",result);
    }

    @Test
    void getTotalForCallsRepoAndReturns() {
        var user = User.builder().build();
        when(userRepository.findByEmail("email")).thenReturn(List.of(user));
        when(limitService.getTotalLimit(user, "api_white")).thenReturn("1");

        String result = userService.getTotalFor("email", "api_white");
        assertEquals("1",result);
    }
    
}