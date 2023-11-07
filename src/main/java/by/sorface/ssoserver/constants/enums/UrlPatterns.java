package by.sorface.ssoserver.constants.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UrlPatterns {

    STATIC("/static/**"),

    LOGIN("/"),

    API("/api/**"),
    ;

    private final String pattern;

    public static String[] toArray() {
        return Arrays.stream(UrlPatterns.values())
                .map(UrlPatterns::getPattern)
                .toArray(String[]::new);
    }

}
