package com.deste.gateway.knowledgegraph;

import jakarta.persistence.*;

@Entity
public class Key {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "kg_id", nullable = false)
    private KnowledgeGraph knowledgeGraph;
}
