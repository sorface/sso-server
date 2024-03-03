package by.sorface.sso.web.utils.json;

import by.sorface.sso.web.utils.json.mask.MaskerFields;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

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
        return maskJson(object);
    }

    public LazyObjectSerializable lazyStringify(final Object object) {
        return new LazyObjectSerializable(() -> Json.stringify(object));
    }

    public LazyObjectSerializable lazyStringifyWithMasking(final Object object) {
        return new LazyObjectSerializable(() -> Json.stringifyWithMasking(object));
    }

    private String maskJson(final Object object) {
        try {
            final String json = stringify(object);

            final JsonNode root = new ObjectMapper()
                    .readTree(json);

            final var jsonNodeStack = new Stack<JsonNode>();
            {
                jsonNodeStack.push(root);
            }

            while (!jsonNodeStack.isEmpty()) {
                final JsonNode currentRoot = jsonNodeStack.pop();

                final Iterator<Map.Entry<String, JsonNode>> jsonNodeIterator = currentRoot.fields();

                while (jsonNodeIterator.hasNext()) {
                    final Map.Entry<String, JsonNode> jsonNodeEntry = jsonNodeIterator.next();

                    final String fieldName = jsonNodeEntry.getKey();
                    final JsonNode fieldValue = jsonNodeEntry.getValue();

                    if (fieldValue instanceof ObjectNode) {
                        jsonNodeStack.push(fieldValue);
                        continue;
                    }

                    if (fieldValue instanceof ArrayNode array) {
                        for (int i = 0; i < array.size(); i++) {
                            final JsonNode arrayElement = array.get(i);
                            jsonNodeStack.push(arrayElement);
                        }
                        continue;
                    }

                    final MaskerFields maskerFields = MaskerFields.findByFieldName(fieldName);

                    if (MaskerFields.UNKNOWN.equals(maskerFields)) {
                        continue;
                    }

                    if (!fieldValue.isTextual()) {
                        continue;
                    }

                    ((ObjectNode) currentRoot).put(fieldName, maskerFields.mask(fieldValue.textValue()));
                }
            }

            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
