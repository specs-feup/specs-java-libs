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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import pt.up.fe.specs.util.SpecsIo;

public class ClassmapTest {

    @Test
    public void testClassSet() {
        ClassSet<Object> set = new ClassSet<>();
        set.add(Integer.class);

        assertTrue(set.contains(Integer.MAX_VALUE));
        assertFalse(set.contains(Double.MAX_VALUE));

        set.add(Number.class);
        assertTrue(set.contains(Double.MAX_VALUE));

        // fail("Not yet implemented");
    }

    @Test
    public void testClassSetV2() {
        ClassSet<InputStream> set = new ClassSet<>();
        set.add(FileInputStream.class);

        assertTrue(set.contains(FileInputStream.class));
        assertFalse(set.contains(BufferedInputStream.class));

        set.add(InputStream.class);
        assertTrue(set.contains(BufferedInputStream.class));
    }

    @Test
    public void testClassMap() {
        ClassMap<InputStream, String> streamMap = new ClassMap<>();

        streamMap.put(FileInputStream.class, "File");
        streamMap.put(InputStream.class, "Base");

        assertEquals("File", streamMap.get(FileInputStream.class));
        assertEquals("Base", streamMap.get(BufferedInputStream.class));
    }

    @Test
    public void testFunctionClassMap() {
        FunctionClassMap<InputStream, String> streamMap = new FunctionClassMap<>();

        streamMap.put(FileInputStream.class, stream -> "File:" + stream.getClass());
        streamMap.put(InputStream.class, stream -> "Base:" + stream.getClass());

        try {
            var fileInputStream = new FileInputStream(SpecsIo.getTempFile());
            var bufferedInputStream = new BufferedInputStream(fileInputStream);

            assertEquals("File:class java.io.FileInputStream", streamMap.apply(fileInputStream));
            assertEquals("Base:class java.io.BufferedInputStream", streamMap.apply(bufferedInputStream));
        } catch (Exception e) {
            fail("Exception: " + e);
        }
    }

    @Test
    public void testBiFunctionClassMap() {
        BiFunctionClassMap<InputStream, Integer, String> streamMap = new BiFunctionClassMap<>();

        streamMap.put(FileInputStream.class, (stream, integer) -> "File:" + integer + ":" + stream.getClass());
        streamMap.put(InputStream.class, (stream, integer) -> "Base:" + integer + ":" + stream.getClass());

        try {
            var fileInputStream = new FileInputStream(SpecsIo.getTempFile());
            var bufferedInputStream = new BufferedInputStream(fileInputStream);

            assertEquals("File:1:class java.io.FileInputStream", streamMap.apply(fileInputStream, 1));
            assertEquals("Base:2:class java.io.BufferedInputStream", streamMap.apply(bufferedInputStream, 2));
        } catch (Exception e) {
            fail("Exception: " + e);
        }
    }

    @Test
    public void testBiConsumerClassMap() {
        BiConsumerClassMap<InputStream, StringBuilder> streamMap = new BiConsumerClassMap<>();

        streamMap.put(FileInputStream.class, (stream, buffer) -> buffer.append("File:" + stream.getClass() + "\n"));
        streamMap.put(InputStream.class, (stream, buffer) -> buffer.append("Base:" + stream.getClass()));

        try {
            var fileInputStream = new FileInputStream(SpecsIo.getTempFile());
            var bufferedInputStream = new BufferedInputStream(fileInputStream);

            StringBuilder buffer = new StringBuilder();

            streamMap.accept(fileInputStream, buffer);
            assertEquals("File:class java.io.FileInputStream\n", buffer.toString());
            streamMap.accept(bufferedInputStream, buffer);
            assertEquals("File:class java.io.FileInputStream\nBase:class java.io.BufferedInputStream",
                    buffer.toString());
        } catch (Exception e) {
            fail("Exception: " + e);
        }
    }

    @Test
    public void testMultiFunction() {
        MultiFunction<InputStream, String> streamMap = new MultiFunction<>();

        streamMap.put(FileInputStream.class, stream -> "File:" + stream.getClass());
        streamMap.put(InputStream.class, (map, stream) -> "Base:" + map.getClass() + ":" + stream.getClass());

        try {
            var fileInputStream = new FileInputStream(SpecsIo.getTempFile());
            var bufferedInputStream = new BufferedInputStream(fileInputStream);

            assertEquals("File:class java.io.FileInputStream", streamMap.apply(fileInputStream));
            assertEquals("Base:class pt.up.fe.specs.util.classmap.MultiFunction:class java.io.BufferedInputStream",
                    streamMap.apply(bufferedInputStream));
        } catch (Exception e) {
            fail("Exception: " + e);
        }
    }

}
