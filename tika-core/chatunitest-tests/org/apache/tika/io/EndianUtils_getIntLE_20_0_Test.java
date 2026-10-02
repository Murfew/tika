package org.apache.tika.io;

import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getIntLE_20_0_Test {

    @Test
    public void testGetIntLE_withValidData() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        int result = EndianUtils.getIntLE(data);
        assertEquals(0x04030201, result);
    }

    @Test
    public void testGetIntLE_withOffset() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08 };
        int result = EndianUtils.getIntLE(data, 4);
        assertEquals(0x08070605, result);
    }

    @Test
    public void testGetIntLE_withEmptyData() throws Exception {
        byte[] data = {};
        Exception exception = assertThrows(EndianUtils.BufferUnderrunException.class, () -> {
            EndianUtils.getIntLE(data);
        });
        assertEquals("Buffer underrun", exception.getMessage());
    }

    @Test
    public void testGetIntLE_withNegativeOffset() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        Exception exception = assertThrows(EndianUtils.BufferUnderrunException.class, () -> {
            EndianUtils.getIntLE(data, -1);
        });
        assertEquals("Buffer underrun", exception.getMessage());
    }

    @Test
    public void testGetIntLE_withInsufficientData() throws Exception {
        byte[] data = { (byte) 0x01 };
        Exception exception = assertThrows(EndianUtils.BufferUnderrunException.class, () -> {
            EndianUtils.getIntLE(data);
        });
        assertEquals("Buffer underrun", exception.getMessage());
    }
}
