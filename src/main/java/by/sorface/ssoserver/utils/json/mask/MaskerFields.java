package by.sorface.ssoserver.utils.json.mask;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public
enum MaskerFields implements Masker {

    PASSWORDS(Set.of("password", "pass", "pwd")) {
        @Override
        public String mask(final String value) {
            if (Objects.isNull(value)) {
                return null;
            }

            return value.replaceAll(".", "*").substring(0, Math.min(10, value.length()));
        }

    },

    EMAILS(Set.of("email", "mail")) {
        @Override
        public String mask(final String value) {
            if (Objects.isNull(value)) {
                return null;
            }

            return value.replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*");
        }

    },

    UNKNOWN(Set.of()) {
        @Override
        public String mask(String value) {
            return null;
        }

    };

    private final Set<String> fieldNames;

    public static MaskerFields findByFieldName(final String fieldName) {
        if (Objects.isNull(fieldName)) {
            return UNKNOWN;
        }

        return Arrays.stream(MaskerFields.values())
                .filter(maskerFields -> maskerFields.getFieldNames().stream()
                        .anyMatch(field -> field.equalsIgnoreCase(fieldName)))
                .findFirst()
                .orElse(UNKNOWN);
    }

}
