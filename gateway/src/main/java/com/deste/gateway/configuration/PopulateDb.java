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
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopulateDb implements CommandLineRunner {
    private final UserRepository userRepo;
    private final KeyRepository keyRepo;
    private final KnowledgeGraphRepository kgRepo;
    private final LimitRepository limitRepo;

    @Override
    public void run(String... args) throws Exception {
        User marioRossi = User.builder().id(1L).email("mario@rossi.it").build();
        User giovanniBianchi = User.builder().id(2L).email("giovanni@bianchi.it").build();
        userRepo.save(marioRossi);
        userRepo.save(giovanniBianchi);

        KnowledgeGraph kgGiovanniBiachi = KnowledgeGraph.builder().id(1L).user(giovanniBianchi).build();
        Key keyGiovanniBianchi = Key.builder().id(1L).name("keyFake").knowledgeGraph(kgGiovanniBiachi).build();
        kgRepo.save(kgGiovanniBiachi);
        keyRepo.save(keyGiovanniBianchi);

        Limit limitForMario = Limit.builder().user(marioRossi).setLimit(10L).remainingLimit(10L).build();
        limitRepo.save(limitForMario);
        Limit limitForGiovanni = Limit.builder().user(giovanniBianchi).setLimit(10L).remainingLimit(10L).build();
        limitRepo.save(limitForGiovanni);

        Limit limitForGiovanniKey = Limit.builder().key(keyGiovanniBianchi).setLimit(2L).remainingLimit(2L).build();
        limitRepo.save(limitForGiovanniKey);

        marioRossi.setLimit(limitForMario);
        giovanniBianchi.setLimit(limitForGiovanni);
        keyGiovanniBianchi.setLimit(limitForGiovanniKey);

        userRepo.save(marioRossi);
        userRepo.save(giovanniBianchi);
        keyRepo.save(keyGiovanniBianchi);
    }
}
