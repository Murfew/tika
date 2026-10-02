package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_getUShortBE_19_0_Test {

    @Test
    public void testGetUShortBEWithEmptyArray() throws Exception {
        byte[] data = {};
        int offset = 0;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithNegativeOffset() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        int offset = -1;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index -1 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithOffsetGreaterThanArrayLength() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        int offset = 4;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 4 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithOffsetOneGreaterThanArrayLength() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        int offset = 4;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 4 out of bounds for length 4", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithOffsetOneGreaterThanArrayLengthAndNegativeValue() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        int offset = 4;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 4 out of bounds for length 4", exception.getMessage());
    }
}
