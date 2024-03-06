package com.deste.gateway.domain.knowledgegraph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeGraphRepository extends JpaRepository<KnowledgeGraph, Long> {
}
