package com.deste.gateway.configuration;

import com.deste.gateway.domain.knowledgegraph.Key;
import com.deste.gateway.domain.knowledgegraph.KeyRepository;
import com.deste.gateway.domain.knowledgegraph.KnowledgeGraph;
import com.deste.gateway.domain.knowledgegraph.KnowledgeGraphRepository;
import com.deste.gateway.domain.limit.Limit;
import com.deste.gateway.domain.limit.LimitRepository;
import com.deste.gateway.domain.user.User;
import com.deste.gateway.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class PopulateDb implements CommandLineRunner {
    private final UserRepository userRepo;
    private final KeyRepository keyRepo;
    private final KnowledgeGraphRepository kgRepo;
    private final LimitRepository limitRepo;

    @Override
    public void run(String... args) throws Exception {
        User marioRossi = User.builder().email("mario@rossi.it").build();
        User giovanniBianchi = User.builder().email("giovanni@bianchi.it").build();
        marioRossi = userRepo.save(marioRossi);
        giovanniBianchi = userRepo.save(giovanniBianchi);

        KnowledgeGraph kgGiovanniBiachi = KnowledgeGraph.builder().user(giovanniBianchi).build();
        kgGiovanniBiachi = kgRepo.save(kgGiovanniBiachi);
        Key keyGiovanniBianchi = Key.builder().name("keyFake").knowledgeGraph(kgGiovanniBiachi).build();
        keyGiovanniBianchi = keyRepo.save(keyGiovanniBianchi);

        Limit limitForMarioWhite = Limit.builder().user(marioRossi).setLimit(10L).remainingLimit(10L).apiGroup("api_white").build();
        Limit limitForMarioBlack = Limit.builder().user(marioRossi).setLimit(10L).remainingLimit(10L).apiGroup("api_black").build();
        limitRepo.save(limitForMarioWhite);
        limitRepo.save(limitForMarioBlack);

        Limit limitForGiovanniWhite = Limit.builder().user(giovanniBianchi).setLimit(10L).remainingLimit(10L).apiGroup("api_white").build();
        Limit limitForGiovanniBlack = Limit.builder().user(giovanniBianchi).setLimit(10L).remainingLimit(10L).apiGroup("api_black").build();
        limitRepo.save(limitForGiovanniWhite);
        limitRepo.save(limitForGiovanniBlack);

        Limit limitForGiovanniKey = Limit.builder().key(keyGiovanniBianchi).setLimit(2L).remainingLimit(2L).apiGroup("api_white").build();
        limitRepo.save(limitForGiovanniKey);

    }
}
