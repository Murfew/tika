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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class EndianUtils_readUShortLE_2_0_Test {

    @Test
    public void testReadUShortLE_withValidData() throws Exception {
        byte[] data = { 0x12, 0x34 };
        InputStream stream = new ByteArrayInputStream(data);
        int result = EndianUtils.readUShortLE(stream);
        assertEquals(0x3412, result);
    }

    @Test
    public void testReadUShortLE_withBufferUnderrun() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.readUShortLE(stream));
    }

    @Test
    public void testReadUShortLE_withNegativeByte() throws Exception {
        byte[] data = { -1, 0 };
        InputStream stream = new ByteArrayInputStream(data);
        assertEquals(255, EndianUtils.readUShortLE(stream));
    }
}
