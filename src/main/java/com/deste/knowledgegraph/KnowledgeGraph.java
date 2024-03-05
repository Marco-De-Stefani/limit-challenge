package com.deste.knowledgegraph;

import com.deste.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "KNOWLEDGE_GRAPH")
public class KnowledgeGraph {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
//    @OneToMany(mappedBy = "knowledgeGraph", fetch = FetchType.LAZY)
//    private Set<Key> keys;
}