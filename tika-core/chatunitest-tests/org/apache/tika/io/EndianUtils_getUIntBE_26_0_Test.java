package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class EndianUtils_getUIntBE_26_0_Test {

    @Test
    public void testGetUIntBE() throws Exception {
        byte[] data = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 };
        long result = EndianUtils.getUIntBE(data);
        assertEquals(1L, result);
    }

    @Test
    public void testGetUIntBEWithOffset() throws Exception {
        byte[] data = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 };
        long result = EndianUtils.getUIntBE(data, 4);
        assertEquals(1L, result);
    }

    @Test
    public void testGetUIntBEWithNegativeOffset() throws Exception {
        byte[] data = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUIntBE(data, -1));
    }

    @Test
    public void testGetUIntBEWithOffsetExceedingArrayLength() throws Exception {
        byte[] data = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUIntBE(data, 8));
    }
}
