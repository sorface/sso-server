package by.sorface.ssoserver.constants.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum OAuthProvider {

    GOOGLE("google"),

    GITHUB("github"),

    YANDEX("yandex"),

    UNKNOWN("unknown");

    private final String name;

    private static final Map<String, OAuthProvider> PROVIDER_MAP = Arrays.stream(OAuthProvider.values())
            .collect(Collectors.toMap(it -> it.getName().toLowerCase(), Function.identity(), (firstMerge, secondMerge) -> firstMerge));

    public static OAuthProvider findByName(final String name) {
        Assert.notNull(name, "Name must not is null");
        Assert.hasText(name, "Name must not is empty");

        return PROVIDER_MAP.getOrDefault(name, UNKNOWN);
    }

}
