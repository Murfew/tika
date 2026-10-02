package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getUShortLE_14_0_Test {

    @Test
    public void testGetUShortLE() throws Exception {
        byte[] data = { 0x00, 0x01 };
        int result = EndianUtils.getUShortLE(data);
        assertEquals(256, result);
    }

    @Test
    public void testGetUShortLEWithOffset() throws Exception {
        byte[] data = { 0x01, 0x00, 0x02, 0x00 };
        int result = EndianUtils.getUShortLE(data, 1);
        assertEquals(512, result);
    }

    @Test
    public void testGetUShortLEWithNegativeOffset() throws Exception {
        byte[] data = { 0x00, 0x01 };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortLE(data, -1);
        });
    }

    @Test
    public void testGetUShortLEWithTooLargeOffset() throws Exception {
        byte[] data = { 0x00, 0x01 };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortLE(data, 2);
        });
    }
}
