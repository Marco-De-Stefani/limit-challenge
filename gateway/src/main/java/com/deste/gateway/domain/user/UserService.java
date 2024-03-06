package com.deste.gateway.domain.user;

import com.deste.gateway.domain.knowledgegraph.Key;
import com.deste.gateway.domain.limit.Limit;
import com.deste.gateway.domain.limit.LimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepo;
    private final LimitRepository limitRepo;

    public boolean isUserLimitReached(String email) {
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
            List<Limit> limits = limitRepo.findByUser(users.get(0));
            if (limits != null && limits.get(0) != null) return String.valueOf(limits.get(0).getRemainingLimit());
        }
        return String.valueOf(0L);
    }
}
