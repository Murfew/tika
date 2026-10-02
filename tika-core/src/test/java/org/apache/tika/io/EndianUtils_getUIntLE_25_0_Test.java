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

import org.junit.jupiter.api.Test;

public class EndianUtils_getUIntLE_25_0_Test {

    @Test
    public void testGetUIntLE() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 0;
        long expected = 0x04030201L;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithNegativeNumber() throws Exception {
        byte[] data = { (byte) 0xFF, (byte) 0xFE, (byte) 0xFD, (byte) 0xFC, (byte) 0xFB, (byte) 0xFA, (byte) 0xF9, (byte) 0xF8 };
        int offset = 0;
        long expected = 0xFCFDFEFFL;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithZero() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        int offset = 0;
        long expected = 0x00000000L;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithEdgeCase() throws Exception {
        byte[] data = { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        int offset = 0;
        long expected = 1L;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithLargeNumber() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, (byte) 0x80, 0x00, 0x00, 0x00, 0x00 };
        int offset = 0;
        long expected = 0x80000000L;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }
}
