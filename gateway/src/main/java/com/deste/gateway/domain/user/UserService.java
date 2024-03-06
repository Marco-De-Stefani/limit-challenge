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

    public boolean isUserLimitReached(String email, String apiGroup) {
        List<User> users = userRepo.findByEmail(email);
        if (users != null && users.size() == 1) {
            User user = users.get(0);
            return limitService.isUserLimitReached(user, apiGroup);
        }
        return false;
    }

    public boolean isValidUser(String email) {
        List<User> users = userRepo.findByEmail(email);
        if (users != null && users.size() == 1) return true;
        return false;
    }

    public String getConsumedFor(String email, String apiGroup) {
        List<User> users = userRepo.findByEmail(email);
        if (users.size() == 1) {
            User user = users.get(0);
            long total = Long.parseLong(limitService.getTotalLimit(user, apiGroup));
            long remaining = Long.parseLong(limitService.getRemainingLimit(user, apiGroup));
            long consumed = total - remaining;
            return String.valueOf(consumed);
        }
        return String.valueOf(0L);
    }

    public String getTotalFor(String email, String apiGroup) {
        List<User> users = userRepo.findByEmail(email);
        if (users.size() == 1) {
            return limitService.getTotalLimit(users.get(0), apiGroup);
        }
        return String.valueOf(0L);
    }
}
