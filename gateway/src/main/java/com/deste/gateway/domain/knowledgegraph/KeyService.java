package com.deste.gateway.domain.knowledgegraph;

import com.deste.gateway.domain.limit.Limit;
import com.deste.gateway.domain.limit.LimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KeyService {
    private final KeyRepository keyRepo;
    private final LimitRepository limitRepo;


    public boolean isKeyLimitReached(String keyName) {
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
            List<Limit> limits = limitRepo.findByKey(keys.get(0));
            if (limits != null && limits.get(0) != null) return String.valueOf(limits.get(0).getRemainingLimit());
        }
        return String.valueOf(0L);
    }
}
