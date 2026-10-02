package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.apache.tika.exception.TikaException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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
        assertThrows(IOException.class, () -> EndianUtils.readIntBE(inputStream));
    }
}
