package com.deste.gateway.domain.knowledgegraph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
    List<Key> findByName(String name);

}
