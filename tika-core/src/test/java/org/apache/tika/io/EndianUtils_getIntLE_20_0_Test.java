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

public class EndianUtils_getIntLE_20_0_Test {

    @Test
    public void testGetIntLE_withValidData() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        int result = EndianUtils.getIntLE(data);
        assertEquals(0x04030201, result);
    }

    @Test
    public void testGetIntLE_withOffset() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08 };
        int result = EndianUtils.getIntLE(data, 4);
        assertEquals(0x08070605, result);
    }

    @Test
    public void testGetIntLE_withEmptyData() throws Exception {
        byte[] data = {};
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            EndianUtils.getIntLE(data);
        });
    }

    @Test
    public void testGetIntLE_withNegativeOffset() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            EndianUtils.getIntLE(data, -1);
        });
    }

    @Test
    public void testGetIntLE_withInsufficientData() throws Exception {
        byte[] data = { (byte) 0x01 };
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            EndianUtils.getIntLE(data);
        });
    }
}
