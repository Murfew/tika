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

public class EndianUtils_getShortBE_16_0_Test {

    @Test
    public void testGetShortBE_withValidData() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34 };
        short expected = 0x1234;
        short result = EndianUtils.getShortBE(data);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortBE_withOffset() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78 };
        short expected = 0x3456;
        short result = EndianUtils.getShortBE(data, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortBE_withEmptyData() throws Exception {
        byte[] data = {};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data));
    }

    @Test
    public void testGetShortBE_withOffsetAndEmptyData() throws Exception {
        byte[] data = {};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data, 1));
    }

    @Test
    public void testGetShortBE_withOffsetAndSmallData() throws Exception {
        byte[] data = { (byte) 0x12 };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data, 1));
    }
}
