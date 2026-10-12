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

import org.junit.jupiter.api.Test;


class EndianUtilsManualTest {

    @Test
    void testReadIntBEWithIncompleteStream() {
        byte[] data = {0x01, 0x02, 0x03};

        assertThrows(
            EndianUtils.BufferUnderrunException.class,
            () -> EndianUtils.readIntBE(
                new ByteArrayInputStream(data))
        );
}
@Test
void testReadIntBEWithZeroBytes() throws Exception {
    byte[] data = {0, 0, 0, 0};

    int result = EndianUtils.readIntBE(
            new ByteArrayInputStream(data));

    assertEquals(0, result);
}

@Test
void testReadIntMEWithZeroBytes() throws Exception {
    byte[] data = {0, 0, 0, 0};

    int result = EndianUtils.readIntME(
            new ByteArrayInputStream(data));

    assertEquals(0, result);
}

@Test
void testReadIntBEWithDistinctBytes() throws Exception {
    byte[] data = {1, 2, 3, 4};

    int result = EndianUtils.readIntBE(
            new ByteArrayInputStream(data));

    assertEquals(0x01020304, result);
}

@Test
void testReadIntMEWithDistinctBytes() throws Exception {
    byte[] data = {1, 2, 3, 4};

    int result = EndianUtils.readIntME(
            new ByteArrayInputStream(data));

    assertEquals(0x02010403, result);
}


    @Test
    void testReadIntMEWithIncompleteStream() {
        byte[] data = {0x01, 0x02, 0x03};

        assertThrows(
            EndianUtils.BufferUnderrunException.class,
            () -> EndianUtils.readIntME(
                new ByteArrayInputStream(data))
        );
    }

    @Test
    void testReadLongBEWithIncompleteStream() {
        byte[] data = {
            0x01, 0x02, 0x03, 0x04,
            0x05, 0x06, 0x07
        };

        assertThrows(
            EndianUtils.BufferUnderrunException.class,
            () -> EndianUtils.readLongBE(
                new ByteArrayInputStream(data))
        );
    }
}
