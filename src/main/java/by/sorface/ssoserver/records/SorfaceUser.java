package by.sorface.ssoserver.records;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SorfaceUser extends User implements OAuth2User {

    /**
     * Внутренний идентификатор пользователя
     */
    private UUID id;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Отчество пользователя
     */
    private String middleName;

    /**
     * Дата рождения пользователя
     */
    private LocalDate birthday;

    /**
     * Ссылка на изображение пользователя
     */
    private String avatarUrl;

    /**
     * Электронная почта пользователя
     */
    private String email;

    private Map<String, Object> oauthAttributes;

    public SorfaceUser(final String username, final String password, final boolean enabled, final Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauthAttributes;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

}

