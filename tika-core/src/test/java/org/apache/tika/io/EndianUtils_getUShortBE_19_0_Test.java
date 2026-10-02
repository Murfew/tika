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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_getUShortBE_19_0_Test {

    @Test
    public void testGetUShortBEWithEmptyArray() throws Exception {
        byte[] data = {};
        int offset = 0;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithNegativeOffset() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        int offset = -1;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index -1 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithOffsetGreaterThanArrayLength() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        int offset = 4;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 4 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithOffsetOneGreaterThanArrayLength() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        int offset = 4;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 4 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithOffsetOneGreaterThanArrayLengthAndNegativeValue() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        int offset = 4;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 4 out of bounds for length 4", exception.getMessage());
    }
}
