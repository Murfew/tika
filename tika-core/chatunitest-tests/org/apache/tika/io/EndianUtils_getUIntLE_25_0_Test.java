package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.io.IOException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class EndianUtils_getUIntLE_25_0_Test {

    @Test
    public void testGetUIntLE() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 0;
        long expected = 0x04030201L;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithNegativeNumber() throws Exception {
        byte[] data = { (byte) 0xFF, (byte) 0xFE, (byte) 0xFD, (byte) 0xFC, (byte) 0xFB, (byte) 0xFA, (byte) 0xF9, (byte) 0xF8 };
        int offset = 0;
        long expected = 0xFCFDFFFFL;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithZero() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        int offset = 0;
        long expected = 0x00000000L;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithEdgeCase() throws Exception {
        byte[] data = { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        int offset = 0;
        long expected = 0x01000000L;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUIntLEWithLargeNumber() throws Exception {
        byte[] data = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x80 };
        int offset = 0;
        long expected = 0x80000000L;
        long result = EndianUtils.getUIntLE(data, offset);
        assertEquals(expected, result);
    }
}
