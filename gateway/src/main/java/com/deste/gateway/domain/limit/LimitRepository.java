package com.deste.gateway.domain.limit;

import com.deste.gateway.domain.knowledgegraph.Key;
import com.deste.gateway.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    List<Limit> findByKey(Key key);

    List<Limit> findByUser(User user);
}
