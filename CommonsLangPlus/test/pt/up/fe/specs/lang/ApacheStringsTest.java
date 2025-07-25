package pt.up.fe.specs.lang;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ApacheStrings} utility class.
 * 
 * Tests HTML escaping functionality using Apache Commons Text integration.
 * 
 * @author Generated Tests
 */
class ApacheStringsTest {

    @Test
    void testEscapeHtml_SimpleText() {
        // Given
        String input = "Hello World";

        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then
        assertThat(result).isEqualTo("Hello World");
    }

    @Test
    void testEscapeHtml_BasicHtmlEntities() {
        // Given
        String input = "<script>alert('XSS');</script>";

        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then
        assertThat(result).isEqualTo("&lt;script&gt;alert('XSS');&lt;/script&gt;");
    }

    @ParameterizedTest
    @CsvSource({
            "'&', '&amp;'",
            "'<', '&lt;'",
            "'>', '&gt;'",
            "'\"', '&quot;'"
    })
    void testEscapeHtml_SpecialCharacters(String input, String expected) {
        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testEscapeHtml_SingleQuote() {
        // Given
        String input = "'";

        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then - Single quote is NOT escaped by default in HTML4
        assertThat(result).isEqualTo("'");
    }

    @Test
    void testEscapeHtml_ComplexHtmlStructure() {
        // Given
        String input = "<div class=\"container\"><p>Hello & \"goodbye\"</p></div>";

        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then
        assertThat(result).isEqualTo(
                "&lt;div class=&quot;container&quot;&gt;&lt;p&gt;Hello &amp; &quot;goodbye&quot;&lt;/p&gt;&lt;/div&gt;");
    }

    @Test
    void testEscapeHtml_AlreadyEscapedEntities() {
        // Given
        String input = "&lt;script&gt;";

        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then
        assertThat(result).isEqualTo("&amp;lt;script&amp;gt;");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testEscapeHtml_NullAndEmpty(String input) {
        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then
        assertThat(result).isEqualTo(input);
    }

    @Test
    void testEscapeHtml_UnicodeCharacters() {
        // Given
        String input = "Hello 世界 & café";

        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then - Unicode characters like é are escaped as entities in HTML4
        assertThat(result).isEqualTo("Hello 世界 &amp; caf&eacute;");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "<img src=\"javascript:alert('XSS')\" />",
            "<iframe src=\"http://evil.com\"></iframe>",
            "<script type=\"text/javascript\">/* malicious code */</script>"
    })
    void testEscapeHtml_XSSPrevention(String input) {
        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then - should escape angle brackets and quotes
        assertThat(result).doesNotContain("<");
        assertThat(result).doesNotContain(">");
        assertThat(result).contains("&lt;");
        assertThat(result).contains("&gt;");
        if (input.contains("\"")) {
            assertThat(result).contains("&quot;");
        }
    }

    @Test
    void testEscapeHtml_OnclickAttribute() {
        // Given
        String input = "onclick=\"alert('click')\"";

        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then - should escape quotes but not single quotes
        assertThat(result).isEqualTo("onclick=&quot;alert('click')&quot;");
        assertThat(result).doesNotContain("\"");
        assertThat(result).contains("&quot;");
    }

    @Test
    void testEscapeHtml_LargeString() {
        // Given
        StringBuilder inputBuilder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            inputBuilder.append("<div>Content ").append(i).append(" & \"test\"</div>");
        }
        String input = inputBuilder.toString();

        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).doesNotContain("<div>");
        assertThat(result).contains("&lt;div&gt;");
        assertThat(result).contains("&amp;");
        assertThat(result).contains("&quot;");
    }

    @Test
    void testEscapeHtml_NumbersAndWhitespace() {
        // Given
        String input = "  123 < 456 > 789  ";

        // When
        String result = ApacheStrings.escapeHtml(input);

        // Then
        assertThat(result).isEqualTo("  123 &lt; 456 &gt; 789  ");
    }
}
