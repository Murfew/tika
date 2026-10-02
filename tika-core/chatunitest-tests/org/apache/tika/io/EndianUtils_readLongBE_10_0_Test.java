package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.apache.tika.exception.TikaException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.io.InputStream;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class EndianUtils_readLongBE_10_0_Test {

    @Test
    public void testReadLongBE() throws Exception {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        InputStream inputStream = new ByteArrayInputStream(data);
        long expected = 0x0102030405060708L;
        long result = EndianUtils.readLongBE(inputStream);
        assertEquals(expected, result);
    }

    @Test
    public void testReadLongBEWithBufferUnderrun() throws Exception {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Exception exception = assertThrows(EndianUtils.BufferUnderrunException.class, () -> {
            EndianUtils.readLongBE(inputStream);
        });
        assertNotNull(exception);
    }

    @Test
    public void testReadLongBEWithIOException() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Exception exception = assertThrows(IOException.class, () -> {
            EndianUtils.readLongBE(inputStream);
        });
        assertNotNull(exception);
    }
}
