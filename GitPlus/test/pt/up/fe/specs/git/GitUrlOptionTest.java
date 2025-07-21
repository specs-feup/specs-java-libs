package pt.up.fe.specs.git;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Unit tests for {@link GitUrlOption} enum.
 * Tests enum constants, string provider implementation, and option string
 * conversion.
 * 
 * @author Generated Tests
 */
class GitUrlOptionTest {

    @Test
    void testEnumConstants() {
        // Test that all expected enum constants exist
        GitUrlOption[] values = GitUrlOption.values();

        assertThat(values).hasSize(2);
        assertThat(values).contains(GitUrlOption.COMMIT, GitUrlOption.FOLDER);
    }

    @Test
    void testGetStringForCommit() {
        // Test COMMIT option returns correct lowercase string
        GitUrlOption commit = GitUrlOption.COMMIT;

        assertThat(commit.getString()).isEqualTo("commit");
    }

    @Test
    void testGetStringForFolder() {
        // Test FOLDER option returns correct lowercase string
        GitUrlOption folder = GitUrlOption.FOLDER;

        assertThat(folder.getString()).isEqualTo("folder");
    }

    @Test
    void testStringProviderImplementation() {
        // Test that GitUrlOption properly implements StringProvider interface
        for (GitUrlOption option : GitUrlOption.values()) {
            assertThat(option).isInstanceOf(StringProvider.class);

            // Verify getString() returns non-null, non-empty string
            String result = option.getString();
            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            assertThat(result).isLowerCase();
        }
    }

    @Test
    void testValueOfWithValidNames() {
        // Test valueOf() with valid enum names
        assertThat(GitUrlOption.valueOf("COMMIT")).isEqualTo(GitUrlOption.COMMIT);
        assertThat(GitUrlOption.valueOf("FOLDER")).isEqualTo(GitUrlOption.FOLDER);
    }

    @Test
    void testValueOfWithInvalidName() {
        // Test valueOf() with invalid enum name throws exception
        assertThatThrownBy(() -> GitUrlOption.valueOf("INVALID"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testEnumNamesMatchStringValues() {
        // Test that enum names match their string values (lowercase)
        for (GitUrlOption option : GitUrlOption.values()) {
            String expectedString = option.name().toLowerCase();
            assertThat(option.getString()).isEqualTo(expectedString);
        }
    }

    @Test
    void testEnumEquality() {
        // Test enum equality and identity
        assertThat(GitUrlOption.COMMIT).isSameAs(GitUrlOption.COMMIT);
        assertThat(GitUrlOption.FOLDER).isSameAs(GitUrlOption.FOLDER);
        assertThat(GitUrlOption.COMMIT).isNotSameAs(GitUrlOption.FOLDER);
    }

    @Test
    void testEnumToString() {
        // Test toString() returns enum name
        assertThat(GitUrlOption.COMMIT.toString()).isEqualTo("COMMIT");
        assertThat(GitUrlOption.FOLDER.toString()).isEqualTo("FOLDER");
    }

    @Test
    void testEnumOrdinal() {
        // Test ordinal values are consistent
        assertThat(GitUrlOption.COMMIT.ordinal()).isEqualTo(0);
        assertThat(GitUrlOption.FOLDER.ordinal()).isEqualTo(1);
    }
}
