package com.kepler.model.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "darts_users")
public class User {
    @Id
    private String id;
    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "max_scopes")
    private Integer maxScopes;
}
