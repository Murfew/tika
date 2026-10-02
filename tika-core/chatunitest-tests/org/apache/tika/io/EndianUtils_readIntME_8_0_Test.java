package org.apache.tika.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;

public class EndianUtils_readIntME_8_0_Test {

    @Test
    public void testReadIntME_Normal() throws Exception {
        byte[] data = new byte[] { 0x12, 0x34, 0x56, 0x78 };
        InputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.readIntME(inputStream);
        assertEquals(0x34127856, result);
    }

    @Test
    public void testReadIntME_BufferUnderrunException() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Executable executable = () -> EndianUtils.readIntME(inputStream);
        assertThrows(EndianUtils.BufferUnderrunException.class, executable);
    }

    @Test
    public void testReadIntME_IOException() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x12, 0x34, 0x56 });
        Executable executable = () -> EndianUtils.readIntME(inputStream);
        assertThrows(IOException.class, executable);
    }
}
