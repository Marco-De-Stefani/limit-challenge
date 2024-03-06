package com.deste.gateway.domain.knowledgegraph;

import com.deste.gateway.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "knowledge_graph")
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