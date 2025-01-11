package com.example.demo3.Test;
import com.example.demo3.Controller.ApiChannel;
import com.example.demo3.Model.Channel;
import com.example.demo3.Model.NewChannelDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ApiChannelTest {

    private static final String BASE_URL = "http://192.168.124.28:8080/api/text-channel";
    private ObjectMapper objectMapper;

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetChannels_Success() throws IOException {
        String mockJsonResponse = "[{\"id\":1,\"name\":\"General\"}]";

        ResponseBody responseBody = ResponseBody.create(mockJsonResponse, MediaType.get("application/json"));
        Mockito.when(mockResponse.body()).thenReturn(responseBody);
        Mockito.when(mockResponse.isSuccessful()).thenReturn(true);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        List<Channel> channels = ApiChannel.getChannels();
        assertNotNull(channels);
        assertEquals(1, channels.size());
        assertEquals("General", channels.get(0).getName());
    }

    @Test
    void testGetChannels_Failure() throws IOException {
        Mockito.when(mockResponse.isSuccessful()).thenReturn(false);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        IOException exception = assertThrows(IOException.class, ApiChannel::getChannels);
        assertTrue(exception.getMessage().contains("Unexpected code"));
    }

    @Test
    void testAddChannel_Success() throws IOException {
        NewChannelDTO newChannelDTO = new NewChannelDTO();
        newChannelDTO.setName("NewChannel");
        newChannelDTO.setDescription("Test description");

        Mockito.when(mockResponse.isSuccessful()).thenReturn(true);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        boolean result = ApiChannel.addChannel(newChannelDTO);
        assertTrue(result);
    }

    @Test
    void testAddChannel_Failure() throws IOException {
        NewChannelDTO newChannelDTO = new NewChannelDTO();
        newChannelDTO.setName("NewChannel");
        newChannelDTO.setDescription("Test description");

        Mockito.when(mockResponse.isSuccessful()).thenReturn(false);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        boolean result = ApiChannel.addChannel(newChannelDTO);
        assertFalse(result);
    }
}
