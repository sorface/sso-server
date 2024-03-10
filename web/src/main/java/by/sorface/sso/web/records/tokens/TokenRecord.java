package by.sorface.sso.web.records.tokens;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class TokenRecord {

    private Boolean active;

    private String sub;

    private List<String> aud;

    private Instant nbf;

    private List<String> scopes;

    private URL iss;

    private Instant exp;

    private Instant iat;

    private String jti;

    private String clientId;

    private String tokenType;

    private IntrospectionPrincipal principal;

}
