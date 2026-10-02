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

public class EndianUtils_getShortLE_12_0_Test {

    @Test
    public void testGetShortLE() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34 };
        short expected = 0x3412;
        assertEquals(expected, EndianUtils.getShortLE(data));
    }

    @Test
    public void testGetShortLEWithOffset() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78 };
        short expected = 0x3412;
        assertEquals(expected, EndianUtils.getShortLE(data, 0));
    }

    @Test
    public void testGetShortLEWithNonZeroOffset() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78 };
        short expected = 0x7856;
        assertEquals(expected, EndianUtils.getShortLE(data, 2));
    }

    @Test
    public void testGetShortLEWithEmptyArray() throws Exception {
        byte[] data = {};
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortLE(data));
    }

    @Test
    public void testGetShortLEWithInsufficientData() throws Exception {
        byte[] data = { (byte) 0x12 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortLE(data));
    }

    @Test
    public void testGetShortLEWithNegativeOffset() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortLE(data, -1));
    }

    @Test
    public void testGetShortLEWithOffsetExceedingArrayLength() throws Exception {
        byte[] data = { (byte) 0x12, (byte) 0x34 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortLE(data, 3));
    }
}
