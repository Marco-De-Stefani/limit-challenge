package com.deste.gateway.domain.limit;

import com.deste.gateway.domain.knowledgegraph.Key;
import com.deste.gateway.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LimitService {
    private final LimitRepository limitRepo;

    public void resetAllRates() {
        var limits = limitRepo.findAll();
        limits.forEach(limit -> limit.setRemainingLimit(limit.getSetLimit()));
        limitRepo.saveAll(limits);
    }

    public boolean isKeyLimitReached(Key key, String apiGroup) {
        var limits = limitRepo.findByKeyAndApiGroup(key, apiGroup);
        return decrementIfRemaining(limits);
    }

    public boolean isUserLimitReached(User user, String apiGroup) {
        var limits = limitRepo.findByUserAndApiGroup(user, apiGroup);
        return decrementIfRemaining(limits);
    }

    private boolean decrementIfRemaining(List<Limit> limits) {
        if (limits != null && limits.size() == 1) {
            var limit = limits.get(0);
            if (limit.getRemainingLimit() - 1 < 0) {
                return true;
            }
            limit.setRemainingLimit(limit.getRemainingLimit() - 1);
            limitRepo.save(limit);
        }
        return false;
    }

    public String getRemainingLimit(Key key, String apiGroup) {
        List<Limit> limits = limitRepo.findByKeyAndApiGroup(key, apiGroup);
        if (limits != null && limits.get(0) != null)
            return String.valueOf(limits.get(0).getRemainingLimit());
        return null;
    }

    public String getRemainingLimit(User user, String apiGroup) {
        var limits = limitRepo.findByUserAndApiGroup(user, apiGroup);
        if (limits != null && limits.get(0) != null)
            return String.valueOf(limits.get(0).getRemainingLimit());
        return null;
    }

    public String getTotalLimit(User user, String apiGroup) {
        var limits = limitRepo.findByUserAndApiGroup(user, apiGroup);
        if (limits != null && limits.get(0) != null)
            return String.valueOf(limits.get(0).getSetLimit());
        return null;
    }

    public String getTotalLimit(Key key, String apiGroup) {
        var limits = limitRepo.findByKeyAndApiGroup(key, apiGroup);
        if (limits != null && limits.get(0) != null)
            return String.valueOf(limits.get(0).getSetLimit());
        return null;
    }
}
