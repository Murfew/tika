package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.io.IOException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getIntBE_22_0_Test {

    @Test
    public void testGetIntBE_withValidData() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55 };
        int result = EndianUtils.getIntBE(data);
        assertEquals(0x11223344, result);
    }

    @Test
    public void testGetIntBE_withOffset() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55 };
        int result = EndianUtils.getIntBE(data, 4);
        assertEquals(0x55, result);
    }

    @Test
    public void testGetIntBE_withEmptyData() throws Exception {
        byte[] data = {};
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.getIntBE(data));
    }

    @Test
    public void testGetIntBE_withTooSmallData() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x11 };
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.getIntBE(data));
    }

    @Test
    public void testGetIntBE_withNegativeOffset() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55 };
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.getIntBE(data, -1));
    }

    @Test
    public void testGetIntBE_withOffsetExceedingDataLength() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55 };
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.getIntBE(data, 8));
    }
}
