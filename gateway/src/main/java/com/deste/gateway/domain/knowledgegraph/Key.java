package com.deste.gateway.domain.knowledgegraph;

import com.deste.gateway.domain.limit.Limit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"key\"")
public class Key {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "kg_id", nullable = false)
    private KnowledgeGraph knowledgeGraph;

//    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
//    private Set<Limit> limits;
}
