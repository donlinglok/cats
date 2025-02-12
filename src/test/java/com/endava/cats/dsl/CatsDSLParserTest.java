package com.endava.cats.dsl;

import com.endava.cats.dsl.api.Parser;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;

@QuarkusTest
class CatsDSLParserTest {

    private CatsDSLParser catsDSLParser;

    @BeforeEach
    public void setup() {
        catsDSLParser = new CatsDSLParser();
    }

    @Test
    void shouldReturnSameValue() {
        String initial = "test";
        String actual = catsDSLParser.parseAndGetResult(initial, null);

        Assertions.assertThat(actual).isEqualTo(initial);
    }

    @Test
    void shouldParseAsDate() {
        String initial = "T(java.time.OffsetDateTime).now().plusDays(2)";
        String actual = catsDSLParser.parseAndGetResult(initial, Map.of());
        OffsetDateTime actualDate = OffsetDateTime.parse(actual);
        Assertions.assertThat(actualDate).isAfter(OffsetDateTime.now(ZoneId.systemDefault()).plusDays(1));
    }

    @Test
    void shouldIgnoreAsMethodInvalid() {
        String initial = "T(java.time.OffsetDateTime).nowMe().plusDays(2)";
        String actual = catsDSLParser.parseAndGetResult(initial, Map.of());

        Assertions.assertThat(actual).isEqualTo(initial);
    }

    @Test
    void shouldParseValueFromJson() {
        String json = "{\n" +
                "    \"filed1\": \"113\",\n" +
                "    \"expiry\": \"2018-06-25\",\n" +
                "    \"match\": \"NOT\"\n" +
                "  }";
        String initial = "T(java.time.LocalDate).now().isAfter(T(java.time.LocalDate).parse(expiry.toString()))";
        String actual = catsDSLParser.parseAndGetResult(initial, Map.of(Parser.RESPONSE, json));

        Assertions.assertThat(actual).isEqualTo("true");
    }

    @Test
    void shouldGetSystemVariable() {
        String actual = catsDSLParser.parseAndGetResult("$$PATH", null);
        Assertions.assertThat(actual).isNotBlank();
    }

    @Test
    void shouldGetNotFoundSystemVariable() {
        String actual = catsDSLParser.parseAndGetResult("$$cats", null);
        Assertions.assertThat(actual).isEqualTo("not_found_$$cats");
    }
}
