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

public class EndianUtils_getUShortBE_18_0_Test {

    @Test
    public void testGetUShortBE() throws Exception {
        byte[] data = { 0x01, 0x02 };
        int result = EndianUtils.getUShortBE(data);
        assertEquals(258, result);
    }

    @Test
    public void testGetUShortBEWithOffset() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        int result = EndianUtils.getUShortBE(data, 1);
        assertEquals(514, result);
    }

    @Test
    public void testGetUShortBEWithEmptyArray() throws Exception {
        byte[] data = {};
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortBE(data));
    }

    @Test
    public void testGetUShortBEWithSingleElementArray() throws Exception {
        byte[] data = { 0x01 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortBE(data));
    }

    @Test
    public void testGetUShortBEWithNegativeOffset() throws Exception {
        byte[] data = { 0x01, 0x02 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortBE(data, -1));
    }

    @Test
    public void testGetUShortBEWithOffsetExceedingArrayLength() throws Exception {
        byte[] data = { 0x01, 0x02 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortBE(data, 3));
    }
}
