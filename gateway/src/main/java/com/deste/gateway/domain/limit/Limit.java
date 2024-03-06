package com.deste.gateway.domain.limit;

import com.deste.gateway.domain.knowledgegraph.Key;
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
@Table(name = "\"limit\"")
public class Limit {
    @GeneratedValue
    @Id
    private Long id;

    @OneToOne(mappedBy = "limit")
    private User user;

    @OneToOne(mappedBy = "limit")
    private Key key;

    private Long setLimit;

    private Long remainingLimit;

}
