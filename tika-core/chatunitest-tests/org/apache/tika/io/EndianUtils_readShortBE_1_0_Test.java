package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.tika.exception.TikaException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readShortBE_1_0_Test {

    @Mock
    private InputStream mockInputStream;

    @InjectMocks
    private EndianUtils endianUtils;

    @Test
    public void testReadShortBE_IOException() throws Exception {
        // Arrange
        when(mockInputStream.read()).thenThrow(new IOException("Mock IOException"));
        // Act & Assert
        assertThrows(IOException.class, () -> {
            endianUtils.readShortBE(mockInputStream);
        });
        verify(mockInputStream, times(1)).read();
    }
}
