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

public class EndianUtils_getUByte_30_0_Test {

    @Test
    public void testGetUByte() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 2;
        short expected = 0x03;
        short result = EndianUtils.getUByte(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUByteWithNegativeOffset() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = -1;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUByte(data, offset));
    }

    @Test
    public void testGetUByteWithTooLargeOffset() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = data.length;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUByte(data, offset));
    }

    @Test
    public void testGetUByteWithEmptyArray() throws Exception {
        byte[] data = {};
        int offset = 0;
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUByte(data, offset));
    }

    @Test
    public void testGetUByteWithNullArray() throws Exception {
        byte[] data = null;
        int offset = 0;
        assertThrows(NullPointerException.class, () -> EndianUtils.getUByte(data, offset));
    }
}
