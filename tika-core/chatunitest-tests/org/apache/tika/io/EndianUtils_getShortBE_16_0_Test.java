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

public class EndianUtils_getShortBE_16_0_Test {

    @Test
    public void testGetShortBE_withValidData() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34 };
        short expected = 0x1234;
        short result = EndianUtils.getShortBE(data);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortBE_withOffset() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78 };
        short expected = 0x3456;
        short result = EndianUtils.getShortBE(data, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortBE_withEmptyData() throws Exception {
        byte[] data = {};
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.getShortBE(data));
    }

    @Test
    public void testGetShortBE_withOffsetAndEmptyData() throws Exception {
        byte[] data = {};
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.getShortBE(data, 1));
    }

    @Test
    public void testGetShortBE_withOffsetAndSmallData() throws Exception {
        byte[] data = { (byte) 0x12 };
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.getShortBE(data, 1));
    }
}
