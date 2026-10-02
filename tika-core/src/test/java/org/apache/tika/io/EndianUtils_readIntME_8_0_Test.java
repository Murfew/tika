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

public class EndianUtils_readIntME_8_0_Test {

    @Test
    public void testReadIntME_Normal() throws Exception {
        byte[] data = new byte[] { 0x12, 0x34, 0x56, 0x78 };
        InputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.readIntME(inputStream);
        assertEquals(0x34127856, result);
    }

    @Test
    public void testReadIntME_BufferUnderrunException() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Executable executable = () -> EndianUtils.readIntME(inputStream);
        assertThrows(EndianUtils.BufferUnderrunException.class, executable);
    }

    @Test
    public void testReadIntME_IOException() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56 });
        Executable executable = () -> EndianUtils.readIntME(inputStream);
        assertThrows(EndianUtils.BufferUnderrunException.class, executable);
    }
}
