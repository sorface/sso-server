package by.sorface.sso.web.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.function.Consumer;

@UtilityClass
public class NullUtils {

    public <T> void setIfNonNull(final T value, final Consumer<T> consumer) {
        if (Objects.nonNull(value)) {
            consumer.accept(value);
        }
    }

}
