package org.apache.tika.io;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readUIntBE_5_0_Test {

    @InjectMocks
    private EndianUtils endianUtils;

    @Mock
    private InputStream inputStream;

    @Test
    public void testReadUIntBEWithIOException() throws Exception {
        when(inputStream.read()).thenThrow(new IOException());
        assertThrows(IOException.class, () -> endianUtils.readUIntBE(inputStream));
        verify(inputStream, times(1)).read();
    }
}
