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

public class EndianUtils_getUShortLE_15_0_Test {

    @Test
    public void testGetUShortLE() throws Exception {
        byte[] data = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        int offset = 1;
        int expected = 257;
        int result = EndianUtils.getUShortLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUShortLEWithOffset() throws Exception {
        byte[] data = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        int offset = 3;
        int expected = 772;
        int result = EndianUtils.getUShortLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUShortLEWithZeroOffset() throws Exception {
        byte[] data = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        int offset = 0;
        int expected = 256;
        int result = EndianUtils.getUShortLE(data, offset);
        assertEquals(expected, result);
    }
}
