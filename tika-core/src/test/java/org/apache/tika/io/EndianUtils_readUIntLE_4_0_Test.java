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
import org.junit.jupiter.api.function.Executable;

public class EndianUtils_readUIntLE_4_0_Test {

    @Test
    public void testReadUIntLE() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(data);
        long result = EndianUtils.readUIntLE(inputStream);
        assertEquals(0x04030201L, result);
    }

    @Test
    public void testReadUIntLEWithBufferUnderrun() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Executable executable = () -> EndianUtils.readUIntLE(inputStream);
        assertThrows(EndianUtils.BufferUnderrunException.class, executable);
    }

    @Test
    public void testReadUIntLEWithNegativeValues() throws Exception {
        byte[] data = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        InputStream inputStream = new ByteArrayInputStream(data);
        long result = EndianUtils.readUIntLE(inputStream);
        assertEquals(0xFFFFFFFFL, result);
    }

    @Test
    public void testReadUIntLEWithMixedValues() throws Exception {
        byte[] data = { 0x01, (byte) 0xFF, 0x02, 0x03 };
        InputStream inputStream = new ByteArrayInputStream(data);
        long result = EndianUtils.readUIntLE(inputStream);
        assertEquals(0x0302FF01L, result);
    }
}
