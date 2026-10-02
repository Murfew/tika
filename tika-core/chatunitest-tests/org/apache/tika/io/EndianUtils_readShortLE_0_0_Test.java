package org.apache.tika.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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
