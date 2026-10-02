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

public class EndianUtils_readIntBE_7_0_Test {

    @Test
    public void testReadIntBE() throws Exception {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.readIntBE(inputStream);
        assertEquals(0x01020304, result);
    }

    @Test
    public void testReadIntBEBufferUnderrun() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.readIntBE(inputStream));
    }

    @Test
    public void testReadIntBEIOException() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.readIntBE(inputStream));
    }
}
