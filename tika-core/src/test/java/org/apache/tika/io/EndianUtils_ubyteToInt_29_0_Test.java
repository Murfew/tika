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

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_ubyteToInt_29_0_Test {

    @Test
    public void testUbyteToInt() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte b = (byte) 0x7F;
        Method method = EndianUtils.class.getDeclaredMethod("ubyteToInt", byte.class);
        method.setAccessible(true);
        int result = (int) method.invoke(endianUtils, b);
        assertEquals(127, result);
        b = (byte) 0xFF;
        result = (int) method.invoke(endianUtils, b);
        assertEquals(255, result);
        b = (byte) 0x00;
        result = (int) method.invoke(endianUtils, b);
        assertEquals(0, result);
    }
}
