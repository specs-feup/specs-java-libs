package org.suikasoft.jOptions.Datakey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class to demonstrate the enhanced MagicKey functionality.
 * This test shows how the robust design improvements provide reliable type
 * information.
 * 
 * @author Generated Tests
 */
@DisplayName("MagicKey Enhancement Tests")
class MagicKeyEnhancementTest {

    @Test
    @DisplayName("Factory method with explicit type should provide reliable type information")
    void testFactoryMethodWithExplicitType() {
        // given - create MagicKey with explicit type using factory method
        MagicKey<String> stringKey = MagicKey.create("test_string", String.class);
        MagicKey<Integer> integerKey = MagicKey.create("test_integer", Integer.class);
        
        // when
        Class<String> stringClass = stringKey.getValueClass();
        Class<Integer> integerClass = integerKey.getValueClass();
        
        // then - explicit type information is preserved
        assertThat(stringClass).isEqualTo(String.class);
        assertThat(integerClass).isEqualTo(Integer.class);
    }

    @Test
    @DisplayName("Factory method with default value should work correctly")
    void testFactoryMethodWithDefaultValue() {
        // given - create MagicKey with explicit type and default value
        MagicKey<String> keyWithDefault = MagicKey.create("test_default", String.class, "default_value");
        
        // when
        Class<String> valueClass = keyWithDefault.getValueClass();
        
        // then - type and functionality are preserved
        assertThat(valueClass).isEqualTo(String.class);
        assertThat(keyWithDefault.getName()).isEqualTo("test_default");
    }

    @Test
    @DisplayName("Constructor with explicit type should preserve type through copy operations")
    void testExplicitTypePreservedThroughCopy() {
        // given - create MagicKey with explicit type using factory method
        MagicKey<Integer> originalKey = MagicKey.create("original", Integer.class);
        
        // when - create copy with new label
        DataKey<Integer> copiedKey = originalKey.setLabel("New Label");
        
        // then - type information is preserved in copy
        assertThat(originalKey.getValueClass()).isEqualTo(Integer.class);
        assertThat(copiedKey.getValueClass()).isEqualTo(Integer.class);
        assertThat(copiedKey).isInstanceOf(MagicKey.class);
    }

    @Test
    @DisplayName("Backward compatibility - old constructor signature still works")
    void testBackwardCompatibility() {
        // given - create MagicKey using the basic constructor (without explicit type)
        MagicKey<String> basicKey = new MagicKey<>("basic_key");
        
        // when - this will use type inference (may fall back to Object.class)
        Class<?> inferredClass = basicKey.getValueClass();
        
        // then - key is functional even if type inference fails
        assertThat(basicKey.getName()).isEqualTo("basic_key");
        assertThat(inferredClass).isNotNull(); // Could be Object.class due to type erasure
    }

    @Test
    @DisplayName("Explicit type takes precedence over type inference")
    void testExplicitTypePrecedence() {
        // given - create with explicit type using factory method
        MagicKey<String> keyWithExplicitType = MagicKey.create("test", String.class);
        
        // when
        Class<String> valueClass = keyWithExplicitType.getValueClass();
        
        // then - explicit type takes precedence
        assertThat(valueClass).isEqualTo(String.class);
    }
}
