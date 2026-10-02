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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.apache.tika.io.EndianUtils.BufferUnderrunException;

public class EndianUtils_readShortLE_0_0_Test {

    private InputStream mockInputStream;

    @BeforeEach
    public void setUp() {
        mockInputStream = mock(InputStream.class);
    }

    @Test
    public void testReadShortLE_Success() throws Exception {
        when(mockInputStream.read()).thenReturn(0x12, 0x34);
        assertEquals(0x3412, EndianUtils.readShortLE(mockInputStream));
    }

    @Test
    public void testReadShortLE_IOException() throws Exception {
        when(mockInputStream.read()).thenThrow(new IOException());
        assertThrows(IOException.class, () -> EndianUtils.readShortLE(mockInputStream));
    }

    @Test
    public void testReadShortLE_BufferUnderrunException() throws Exception {
        when(mockInputStream.read()).thenReturn(-1, -1);
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readShortLE(mockInputStream));
    }

    @Test
    public void testReadShortLE_MixedResults() throws Exception {
        when(mockInputStream.read()).thenReturn(0x12, -1);
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readShortLE(mockInputStream));
    }
}
