package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getIntLE_21_0_Test {

    @Test
    public void testGetIntLE() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        int offset = 0;
        int expectedResult = 0x04030201;
        int result = EndianUtils.getIntLE(data, offset);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetIntLEWithNegativeValues() throws Exception {
        byte[] data = { (byte) 0xFF, (byte) 0xFE, (byte) 0xFD, (byte) 0xFC };
        int offset = 0;
        int expectedResult = 0xFCFD_FEFF;
        int result = EndianUtils.getIntLE(data, offset);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetIntLEWithOffset() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08 };
        int offset = 2;
        int expectedResult = 0x08070605;
        int result = EndianUtils.getIntLE(data, offset);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetIntLEWithEmptyArray() throws Exception {
        byte[] data = {};
        int offset = 0;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntLE(data, offset));
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }

    @Test
    public void testGetIntLEWithNegativeOffset() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        int offset = -1;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntLE(data, offset));
        assertEquals("Index -1 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void testGetIntLEWithOffsetGreaterThanArrayLength() throws Exception {
        byte[] data = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
        int offset = 4;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getIntLE(data, offset));
        assertEquals("Index 4 out of bounds for length 4", exception.getMessage());
    }
}
