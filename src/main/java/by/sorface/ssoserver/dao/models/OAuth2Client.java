package by.sorface.ssoserver.dao.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "T_OAUTH2CLIENT")
public class OAuth2Client extends BaseEntity {

    @Column(name = "C_CLIENTID", nullable = false)
    private String clientId;

    @Column(name = "C_CLIENTSECRET")
    private String clientSecret;

    @Column(name = "C_CLIENTNAME", nullable = false)
    private String clientName;

    @Column(name = "C_REDIRECTURIS")
    private String redirectUris;

    @Column(name = "C_CLIENTIDISSUEDAT", nullable = false)
    private LocalDateTime clientIdIssueAt;

    @Column(name = "C_CLIENTSECRETEXPIRESAT")
    private LocalDateTime clientSecretExpiresAt;

}
