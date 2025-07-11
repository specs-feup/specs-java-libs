/**
 * Copyright 2015 SPeCS Research Group.
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

package pt.up.fe.specs.util.classmap;

import static org.assertj.core.api.Assertions.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.SpecsIo;

@DisplayName("Classmap Tests")
public class ClassmapTest {

    @Nested
    @DisplayName("ClassSet Tests")
    class ClassSetTests {

        @Test
        @DisplayName("Should contain objects of specified classes")
        public void testClassSet() {
            ClassSet<Object> set = new ClassSet<>();
            set.add(Integer.class);

            assertThat(set.contains(Integer.MAX_VALUE)).isTrue();
            assertThat(set.contains(Double.MAX_VALUE)).isFalse();

            set.add(Number.class);
            assertThat(set.contains(Double.MAX_VALUE)).isTrue();
        }

        @Test
        @DisplayName("Should work with generic types")
        public void testClassSetV2() {
            ClassSet<InputStream> set = new ClassSet<>();
            set.add(FileInputStream.class);

            assertThat(set.contains(FileInputStream.class)).isTrue();
            assertThat(set.contains(BufferedInputStream.class)).isFalse();

            set.add(InputStream.class);
            assertThat(set.contains(BufferedInputStream.class)).isTrue();
        }
    }

    @Nested
    @DisplayName("ClassMap Tests")
    class ClassMapTests {

        @Test
        @DisplayName("Should map classes to values correctly")
        public void testClassMap() {
            ClassMap<InputStream, String> streamMap = new ClassMap<>();

            streamMap.put(FileInputStream.class, "File");
            streamMap.put(InputStream.class, "Base");

            assertThat(streamMap.get(FileInputStream.class)).isEqualTo("File");
            assertThat(streamMap.get(BufferedInputStream.class)).isEqualTo("Base");
        }
    }

    @Nested
    @DisplayName("FunctionClassMap Tests")
    class FunctionClassMapTests {

        @Test
        @DisplayName("Should apply functions based on class hierarchy")
        public void testFunctionClassMap() {
            FunctionClassMap<InputStream, String> streamMap = new FunctionClassMap<>();

            streamMap.put(FileInputStream.class, stream -> "File:" + stream.getClass());
            streamMap.put(InputStream.class, stream -> "Base:" + stream.getClass());

            assertThatCode(() -> {
                var fileInputStream = new FileInputStream(SpecsIo.getTempFile());
                var bufferedInputStream = new BufferedInputStream(fileInputStream);

                assertThat(streamMap.apply(fileInputStream)).isEqualTo("File:class java.io.FileInputStream");
                assertThat(streamMap.apply(bufferedInputStream)).isEqualTo("Base:class java.io.BufferedInputStream");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("BiFunctionClassMap Tests")
    class BiFunctionClassMapTests {

        @Test
        @DisplayName("Should apply bifunctions with two parameters")
        public void testBiFunctionClassMap() {
            BiFunctionClassMap<InputStream, Integer, String> streamMap = new BiFunctionClassMap<>();

            streamMap.put(FileInputStream.class, (stream, integer) -> "File:" + integer + ":" + stream.getClass());
            streamMap.put(InputStream.class, (stream, integer) -> "Base:" + integer + ":" + stream.getClass());

            assertThatCode(() -> {
                var fileInputStream = new FileInputStream(SpecsIo.getTempFile());
                var bufferedInputStream = new BufferedInputStream(fileInputStream);

                assertThat(streamMap.apply(fileInputStream, 1)).isEqualTo("File:1:class java.io.FileInputStream");
                assertThat(streamMap.apply(bufferedInputStream, 2)).isEqualTo("Base:2:class java.io.BufferedInputStream");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("BiConsumerClassMap Tests")
    class BiConsumerClassMapTests {

        @Test
        @DisplayName("Should apply biconsumers with side effects")
        public void testBiConsumerClassMap() {
            BiConsumerClassMap<InputStream, StringBuilder> streamMap = new BiConsumerClassMap<>();

            streamMap.put(FileInputStream.class, (stream, buffer) -> buffer.append("File:" + stream.getClass() + "\n"));
            streamMap.put(InputStream.class, (stream, buffer) -> buffer.append("Base:" + stream.getClass()));

            assertThatCode(() -> {
                var fileInputStream = new FileInputStream(SpecsIo.getTempFile());
                var bufferedInputStream = new BufferedInputStream(fileInputStream);

                StringBuilder buffer = new StringBuilder();

                streamMap.accept(fileInputStream, buffer);
                assertThat(buffer.toString()).isEqualTo("File:class java.io.FileInputStream\n");
                streamMap.accept(bufferedInputStream, buffer);
                assertThat(buffer.toString()).isEqualTo("File:class java.io.FileInputStream\nBase:class java.io.BufferedInputStream");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("MultiFunction Tests")
    class MultiFunctionTests {

        @Test
        @DisplayName("Should apply multi-level functions")
        public void testMultiFunction() {
            MultiFunction<InputStream, String> streamMap = new MultiFunction<>();

            streamMap.put(FileInputStream.class, stream -> "File:" + stream.getClass());
            streamMap.put(InputStream.class, (map, stream) -> "Base:" + map.getClass() + ":" + stream.getClass());

            assertThatCode(() -> {
                var fileInputStream = new FileInputStream(SpecsIo.getTempFile());
                var bufferedInputStream = new BufferedInputStream(fileInputStream);

                assertThat(streamMap.apply(fileInputStream)).isEqualTo("File:class java.io.FileInputStream");
                assertThat(streamMap.apply(bufferedInputStream)).isEqualTo("Base:class pt.up.fe.specs.util.classmap.MultiFunction:class java.io.BufferedInputStream");
            }).doesNotThrowAnyException();
        }
    }
}
