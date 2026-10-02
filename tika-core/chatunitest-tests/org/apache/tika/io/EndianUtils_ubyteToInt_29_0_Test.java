package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

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
