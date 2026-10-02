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

public class EndianUtils_getIntBE_22_0_Test {

    @Test
    public void testGetIntBE_withValidData() throws Exception {
        byte[] data = { 0x11, 0x22, 0x33, 0x44, 0x55 };
        int result = EndianUtils.getIntBE(data);
        assertEquals(0x11223344, result);
    }

    @Test
    public void testGetIntBE_withOffset() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55 };
        int result = EndianUtils.getIntBE(data, 4);
        assertEquals(0x22334455, result);
    }

    @Test
    public void testGetIntBE_withEmptyData() throws Exception {
        byte[] data = {};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data));
    }

    @Test
    public void testGetIntBE_withTooSmallData() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00 };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data));
    }

    @Test
    public void testGetIntBE_withNegativeOffset() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55 };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data, -1));
    }

    @Test
    public void testGetIntBE_withOffsetExceedingDataLength() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55 };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data, 8));
    }
}
