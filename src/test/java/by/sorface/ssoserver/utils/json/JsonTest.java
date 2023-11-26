package by.sorface.ssoserver.utils.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

class JsonTest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TestObject {

        private String id;

        private String value;

        private String password;

        private String email;

        private String token;

        private List<TestObject> children;

    }

    @Test
    void stringifyMasking() {
        TestObject build = TestObject.builder()
                .id("1")
                .value("name")
                .password("1,43432,WRtffd")
                .email("devpavdeveloper@gmail.com")
                .token("fuehwfihewiughihirehiguhreiuhgiurehgiurehiut387533fjfjkwejkfhewifg76r67234grrbf")
                .children(List.of(
                        TestObject.builder()
                                .id("2")
                                .value("value")
                                .password("passwordq1w2e3r4t5y6")
                                .email("devpavdeveloper@yandex.com")
                                .build()
                )).build();


        String stringifyMasking = Json.stringifyWithMasking(build);

        System.out.println(stringifyMasking);
    }
}