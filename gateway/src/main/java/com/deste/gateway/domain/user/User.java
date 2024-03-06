package com.deste.gateway.domain.user;


import com.deste.gateway.domain.knowledgegraph.KnowledgeGraph;
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
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
}
