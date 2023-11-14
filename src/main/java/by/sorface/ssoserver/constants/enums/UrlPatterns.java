package by.sorface.ssoserver.constants.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum UrlPatterns {

    TECH(
            List.of("/oauth2/introspect")
    ),

    API(
            List.of("/api/**")
    ),

    STATIC(
            List.of("/static/**")
    ),

    FRONTEND(
            List.of(
                    "/",
                    "/signin",
                    "/signup"
            )
    );

    private final List<String> patterns;

    public static String[] toArray() {
        return Arrays.stream(UrlPatterns.values())
                .map(UrlPatterns::getPatterns)
                .flatMap(Collection::stream)
                .toArray(String[]::new);
    }

}
