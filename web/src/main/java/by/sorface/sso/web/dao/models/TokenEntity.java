package by.sorface.sso.web.dao.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "T_REGISTRYTOKEN")
public class TokenEntity extends BaseEntity {

    @Column(name = "C_HASH")
    private String hash;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_USERID", referencedColumnName = "C_ID")
    private UserEntity user;

}
