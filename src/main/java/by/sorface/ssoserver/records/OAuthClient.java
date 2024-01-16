package by.sorface.ssoserver.records;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthClient {

    private String clientName;

    private Set<String> redirectUrls;

}
