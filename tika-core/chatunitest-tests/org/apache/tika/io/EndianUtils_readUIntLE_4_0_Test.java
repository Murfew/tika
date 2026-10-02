package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.apache.tika.exception.TikaException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class EndianUtils_readUIntLE_4_0_Test {

    @Test
    public void testReadUIntLE() throws Exception {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(data);
        long result = EndianUtils.readUIntLE(inputStream);
        assertEquals(0x04030201L, result);
    }

    @Test
    public void testReadUIntLEWithBufferUnderrun() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Executable executable = () -> EndianUtils.readUIntLE(inputStream);
        assertThrows(EndianUtils.BufferUnderrunException.class, executable);
    }

    @Test
    public void testReadUIntLEWithNegativeValues() throws Exception {
        byte[] data = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        InputStream inputStream = new ByteArrayInputStream(data);
        long result = EndianUtils.readUIntLE(inputStream);
        assertEquals(0xFFFFFFFFL, result);
    }

    @Test
    public void testReadUIntLEWithMixedValues() throws Exception {
        byte[] data = { 0x01, (byte) 0xFF, 0x02, 0x03 };
        InputStream inputStream = new ByteArrayInputStream(data);
        long result = EndianUtils.readUIntLE(inputStream);
        assertEquals(0x030201FFL, result);
    }
}
