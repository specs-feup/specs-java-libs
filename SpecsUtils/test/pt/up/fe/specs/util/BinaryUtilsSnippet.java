/**
 * Copyright 2012 SPeCS Research Group.
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

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.junit.Test;

public class BinaryUtilsSnippet {

    // @Test
    // public void reverseBytes() {
    // int test = 0x12345678;
    //
    // System.out.println("Reverse:" + Integer.toHexString(ByteUtils.reverse(test)));
    // }

    @Test
    public void writeBinary() {
        // Detect Endian
        ByteOrder b = ByteOrder.nativeOrder();

        System.out.println("ByteOrder:" + b);

        int numBytes = Double.SIZE / 8;
        // int numBytes = Integer.SIZE / 8;
        double[] dArray = new double[100];
        // int[] dArray = new int[100];

        for (int i = 0; i < dArray.length; i++) {
            dArray[i] = i;
        }

        ByteBuffer buffer = ByteBuffer.allocate(dArray.length * numBytes);
        buffer.order(b);

        for (int i = 0; i < dArray.length; i++) {
            buffer.putDouble(dArray[i]);
            // buffer.putInt(dArray[i]);
        }

        File file = new File("D:\\Coding\\Outputs\\MatlabToCTester\\output\\binary.data");

        try (FileOutputStream stream = new FileOutputStream(file);) {
            stream.write(buffer.array(), 0, buffer.position());
        } catch (Exception ex) {
            throw new RuntimeException("", ex);
        }

        SpecsLogs.msgInfo("ARRAY:" + Arrays.toString(buffer.array()));

    }
}
