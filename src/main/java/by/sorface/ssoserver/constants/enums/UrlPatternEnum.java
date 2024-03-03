package by.sorface.ssoserver.constants.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum UrlPatternEnum {

    TECH(
            List.of("/oauth2/introspect")
    ),

    API(
            List.of("/api/**")
    ),

    STATIC(
            List.of(
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/webjars/**",
                    "**/favicon.ico",
                    "*.json"
            )
    ),

    FRONTEND(
            List.of(
                    "/login"
            )
    );

    private final List<String> patterns;

    public static String[] toArray() {
        return Arrays.stream(UrlPatternEnum.values())
                .map(UrlPatternEnum::getPatterns)
                .flatMap(Collection::stream)
                .toArray(String[]::new);
    }

}
