package org.apache.tika.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class EndianUtils_readUShortLE_2_0_Test {

    @Test
    public void testReadUShortLE_withValidData() throws Exception {
        byte[] data = { 0x12, 0x34 };
        InputStream stream = new ByteArrayInputStream(data);
        int result = EndianUtils.readUShortLE(stream);
        assertEquals(0x3412, result);
    }

    @Test
    public void testReadUShortLE_withBufferUnderrun() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.readUShortLE(stream));
    }

    @Test
    public void testReadUShortLE_withNegativeByte() throws Exception {
        byte[] data = { -1, 0 };
        InputStream stream = new ByteArrayInputStream(data);
        assertThrows(EndianUtils.BufferUnderrunException.class, () -> EndianUtils.readUShortLE(stream));
    }
}
