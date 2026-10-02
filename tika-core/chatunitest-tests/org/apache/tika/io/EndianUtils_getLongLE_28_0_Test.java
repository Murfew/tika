package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getLongLE_28_0_Test {

    @Test
    public void testGetLongLE() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0 };
        int offset = 0;
        long expected = 0xF0DEBC9A78563412L;
        long result = EndianUtils.getLongLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetLongLEWithNegativeOffset() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0 };
        int offset = -1;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getLongLE(data, offset));
    }

    @Test
    public void testGetLongLEWithOffsetGreaterThanDataLength() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0 };
        int offset = 8;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getLongLE(data, offset));
    }

    @Test
    public void testGetLongLEWithNullData() throws Exception {
        byte[] data = null;
        int offset = 0;
        assertThrows(NullPointerException.class, () -> EndianUtils.getLongLE(data, offset));
    }
}
