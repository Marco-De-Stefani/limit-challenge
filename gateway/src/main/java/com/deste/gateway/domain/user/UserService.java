package com.deste.gateway.domain.user;

import com.deste.gateway.domain.limit.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepo;
    private final LimitService limitService;

    public boolean isUserLimitReached(String email) {
        List<User> users = userRepo.findByEmail(email);
        if (users != null && users.size() == 1) {
            User user = users.get(0);
            return limitService.isUserLimitReached(user);
        }
        return false;
    }

    public boolean isValidUser(String email) {
        List<User> users = userRepo.findByEmail(email);
        if (users != null && users.size() == 1) return true;
        return false;
    }

    public String getConsumedFor(String email) {
        List<User> users = userRepo.findByEmail(email);
        if (users.size() == 1) {
            return limitService.getRemainingLimit(users.get(0));
        }
        return String.valueOf(0L);
    }
}
