package by.sorface.sso.web.records;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class IntrospectionPrincipal {

    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthday;
    private String avatarUrl;
    private String username;
    private String email;
    private List<String> authorities;

    public static IntrospectionPrincipal build(final SfPrincipal authorizedUser) {
        if (Objects.isNull(authorizedUser)) {
            return null;
        }

        final List<String> authorities = Optional.ofNullable(authorizedUser.getAuthorities())
                .stream()
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return IntrospectionPrincipal.builder()
                .id(authorizedUser.getId())
                .firstName(authorizedUser.getFirstName())
                .lastName(authorizedUser.getLastName())
                .middleName(authorizedUser.getMiddleName())
                .birthday(authorizedUser.getBirthday())
                .avatarUrl(authorizedUser.getAvatarUrl())
                .username(authorizedUser.getUsername())
                .email(authorizedUser.getEmail())
                .authorities(authorities)
                .build();
    }

}
