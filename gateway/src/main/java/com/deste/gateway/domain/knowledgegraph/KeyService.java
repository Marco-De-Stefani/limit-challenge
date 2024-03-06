package com.deste.gateway.domain.knowledgegraph;

import com.deste.gateway.domain.limit.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KeyService {
    private final KeyRepository keyRepo;
    private final LimitService limitService;


    public boolean isKeyLimitReached(String keyName) {
        List<Key> keys = keyRepo.findByName(keyName);
        if (keys != null && keys.size() == 1) {
            Key key = keys.get(0);
            return limitService.isKeyLimitReached(key);
        }
        return false;
    }

    public boolean isValidKey(String keyName) {
        List<Key> keys = keyRepo.findByName(keyName);
        if (keys != null && keys.size() == 1) {
            Key key = keys.get(0);
            return key.getKnowledgeGraph() != null && key.getKnowledgeGraph().getUser() != null;
        }
        return false;
    }

    public String getConsumedFor(String keyName) {
        List<Key> keys = keyRepo.findByName(keyName);
        if (keys.size() == 1) {
            return limitService.getRemainingLimit(keys.get(0));
        }
        return String.valueOf(0L);
    }
}
