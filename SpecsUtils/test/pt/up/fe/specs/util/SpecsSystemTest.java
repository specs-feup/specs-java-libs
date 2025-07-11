/**
 * Copyright 2019 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for SpecsSystem utility class.
 * 
 * This test class covers system functionality including:
 * - Java version detection
 * - Reflection utilities (invokeAsGetter)
 * - System property access
 * - Field and method invocation
 */
@DisplayName("SpecsSystem Tests")
public class SpecsSystemTest {

    public static final String STATIC_FIELD = "a_static_field";
    private static final int A_NUMBER = 10;

    public static int getStaticNumber() {
        return A_NUMBER;
    }

    public int getNumber() {
        return 20;
    }

    @Nested
    @DisplayName("Java Version Detection")
    class JavaVersionDetection {

        @Test
        @DisplayName("getJavaVersionNumber should return valid version without throwing exception")
        void testJavaVersion_ShouldReturnValidVersion() {
            assertThatCode(() -> {
                double version = SpecsSystem.getJavaVersionNumber();
                System.out.println("Java version: " + version);
            }).doesNotThrowAnyException();
            
            // Java version should be positive and reasonable (at least 8.0)
            assertThat(SpecsSystem.getJavaVersionNumber()).isGreaterThanOrEqualTo(8.0);
        }
    }

    @Nested
    @DisplayName("Reflection Utilities")
    class ReflectionUtilities {

        @Test
        @DisplayName("invokeAsGetter should access static fields correctly")
        void testInvokeAsGetter_StaticField_ReturnsCorrectValue() {
            Object result = SpecsSystem.invokeAsGetter(SpecsSystemTest.class, "STATIC_FIELD");
            assertThat(result).isEqualTo("a_static_field");
        }

        @Test
        @DisplayName("invokeAsGetter should invoke static methods correctly")
        void testInvokeAsGetter_StaticMethod_ReturnsCorrectValue() {
            Object result = SpecsSystem.invokeAsGetter(SpecsSystemTest.class, "staticNumber");
            assertThat(result).isEqualTo(10);
        }

        @Test
        @DisplayName("invokeAsGetter should invoke instance methods correctly")
        void testInvokeAsGetter_InstanceMethod_ReturnsCorrectValue() {
            Object result = SpecsSystem.invokeAsGetter(new SpecsSystemTest(), "number");
            assertThat(result).isEqualTo(20);
        }

        @Test
        @DisplayName("invokeAsGetter should throw exception for non-existent field")
        void testInvokeAsGetter_NonExistentField_ShouldThrowException() {
            assertThatThrownBy(() -> {
                SpecsSystem.invokeAsGetter(SpecsSystemTest.class, "nonExistentField");
            }).isInstanceOf(RuntimeException.class)
              .hasMessageContaining("Could not resolve property 'nonExistentField'");
        }

        @Test
        @DisplayName("invokeAsGetter should throw exception for null class")
        void testInvokeAsGetter_NullClass_ShouldThrowException() {
            assertThatThrownBy(() -> {
                SpecsSystem.invokeAsGetter(null, "someField");
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("invokeAsGetter should throw exception for null field name")
        void testInvokeAsGetter_NullFieldName_ShouldThrowException() {
            assertThatThrownBy(() -> {
                SpecsSystem.invokeAsGetter(SpecsSystemTest.class, null);
            }).isInstanceOf(RuntimeException.class);
        }
    }

}
