package com.deste.gateway.domain.knowledgegraph;

import com.deste.gateway.domain.limit.Limit;
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
@Table(name = "\"key\"")
public class Key {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "kg_id", nullable = false)
    private KnowledgeGraph knowledgeGraph;

    @OneToOne
    @JoinColumn(name = "limit_id", referencedColumnName = "id")
    private Limit limit;
}
