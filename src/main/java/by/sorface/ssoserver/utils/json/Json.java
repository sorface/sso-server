package by.sorface.ssoserver.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Json {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String stringify(final Object object) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public String stringifyWithMasking(final Object object) {
        try {
            return OBJECT_MAPPER
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public LazyObjectSerializable lazyStringify(final Object object) {
        return new LazyObjectSerializable(() -> Json.stringify(object));
    }

    public LazyObjectSerializable lazyStringifyWithMasking(final Object object) {
        return new LazyObjectSerializable(() -> Json.stringifyWithMasking(object));
    }

}
