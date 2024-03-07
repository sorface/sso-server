package by.sorface.sso.web.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum UrlPatternEnum {

    API(
            List.of("/api/**")
    ),

    STATIC(
            List.of(
                    "/static/**"
            )
    ),

    FRONTEND(
            List.of(
                    "/login",
                    "/home"
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
