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

public class EndianUtils_getShortLE_13_0_Test {

    @Test
    public void testGetShortLE() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34 };
        int offset = 0;
        short expected = (short) 0x3412;
        short result = EndianUtils.getShortLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortLEWithOffset() throws Exception {
        byte[] data = { (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC };
        int offset = 2;
        short expected = (short) 0xBC9A;
        short result = EndianUtils.getShortLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortLEWithEmptyArray() throws Exception {
        byte[] data = {};
        int offset = 0;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortLE(data, offset));
    }

    @Test
    public void testGetShortLEWithOffsetOutOfBounds() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34 };
        int offset = 2;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortLE(data, offset));
    }
}
