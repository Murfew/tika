package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.apache.tika.exception.TikaException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readUShortBE_3_0_Test {

    @Test
    public void testReadUShortBE() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = { 0x12, 0x34 };
        InputStream inputStream = new ByteArrayInputStream(data);
        int result = (int) EndianUtils.class.getDeclaredMethod("readUShortBE", InputStream.class).invoke(endianUtils, inputStream);
        assertEquals(0x1234, result);
    }

    @Test
    public void testReadUShortBEWithBufferUnderrun() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = { 0x12 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Exception exception = assertThrows(EndianUtils.BufferUnderrunException.class, () -> {
            EndianUtils.class.getDeclaredMethod("readUShortBE", InputStream.class).invoke(endianUtils, inputStream);
        });
    }

    @Test
    public void testReadUShortBEWithIOException() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Exception exception = assertThrows(IOException.class, () -> {
            EndianUtils.class.getDeclaredMethod("readUShortBE", InputStream.class).invoke(endianUtils, inputStream);
        });
    }
}
