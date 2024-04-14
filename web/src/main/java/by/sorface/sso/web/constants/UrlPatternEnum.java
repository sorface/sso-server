package by.sorface.sso.web.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum UrlPatternEnum {

    OPTION_REQUEST(List.of("/**")),

    API_ACCOUNT(List.of("/api/accounts/current"));

    private final List<String> patterns;

    public static String[] toArray(UrlPatternEnum... urlPatterns) {
        return Arrays.stream(urlPatterns).map(UrlPatternEnum::getPatterns).flatMap(Collection::stream).toList().toArray(String[]::new);
    }
}
