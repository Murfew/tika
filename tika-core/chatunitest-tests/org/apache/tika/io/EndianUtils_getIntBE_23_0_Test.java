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

public class EndianUtils_getIntBE_23_0_Test {

    @Test
    public void testGetIntBEWithNegativeNumber() throws Exception {
        byte[] data = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFE, 0x00, 0x00, 0x00, 0x00 };
        int offset = 0;
        int result = EndianUtils.getIntBE(data, offset);
        assertEquals(-2, result);
    }

    @Test
    public void testGetIntBEWithZero() throws Exception {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        int offset = 0;
        int result = EndianUtils.getIntBE(data, offset);
        assertEquals(0, result);
    }
}
