package com.deste.gateway.user;


import com.deste.gateway.knowledgegraph.KnowledgeGraph;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
//user is a reserved keyword
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<KnowledgeGraph> knowledgeGraphs;

}