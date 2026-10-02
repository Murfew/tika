/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class EndianUtils_getIntLE_21_0_Test {

    @Test
    public void testGetIntLE() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        int offset = 0;
        int expectedResult = 0x04030201;
        int result = EndianUtils.getIntLE(data, offset);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetIntLEWithNegativeValues() throws Exception {
        byte[] data = { (byte) 0xFF, (byte) 0xFE, (byte) 0xFD, (byte) 0xFC };
        int offset = 0;
        int expectedResult = 0xFCFD_FEFF;
        int result = EndianUtils.getIntLE(data, offset);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetIntLEWithOffset() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08 };
        int offset = 2;
        int expectedResult = 0x06050403;
        int result = EndianUtils.getIntLE(data, offset);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetIntLEWithEmptyArray() throws Exception {
        byte[] data = {};
        int offset = 0;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntLE(data, offset));
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }

    @Test
    public void testGetIntLEWithNegativeOffset() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        int offset = -1;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntLE(data, offset));
        assertEquals("Index -1 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void testGetIntLEWithOffsetGreaterThanArrayLength() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        int offset = 4;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntLE(data, offset));
        assertEquals("Index 4 out of bounds for length 4", exception.getMessage());
    }
}
