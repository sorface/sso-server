package by.sorface.sso.web.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum UrlPatternEnum {

    STATIC(
            List.of(
                    "/static/**"
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
