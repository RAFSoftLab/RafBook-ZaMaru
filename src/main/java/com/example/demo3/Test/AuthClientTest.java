package com.example.demo3.Test;

import com.example.demo3.Controller.AuthClient;
import okhttp3.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AuthClientTest {

    private static final String BASE_URL = "http://192.168.124.28:8080/api/users/auth/login";

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

    @Mock
    private ResponseBody mockResponseBody;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate_Success() throws IOException {
        String mockToken = "mockJwtToken";
        String mockJsonResponse = new JSONObject().put("token", mockToken).toString();

        Mockito.when(mockResponseBody.string()).thenReturn(mockJsonResponse);
        Mockito.when(mockResponse.body()).thenReturn(mockResponseBody);
        Mockito.when(mockResponse.isSuccessful()).thenReturn(true);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        AuthClient authClient = new AuthClient();
        String token = AuthClient.authenticate("testUser", "testPassword");

        assertNotNull(token);
        assertEquals(mockToken, token);
    }

    @Test
    void testAuthenticate_Failure() throws IOException {
        Mockito.when(mockResponse.isSuccessful()).thenReturn(false);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        AuthClient authClient = new AuthClient();
        String token = AuthClient.authenticate("testUser", "wrongPassword");

        assertNull(token);
    }

    @Test
    void testAuthenticate_Exception() throws IOException {
        Mockito.when(mockCall.execute()).thenThrow(new IOException("Network error"));
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        AuthClient authClient = new AuthClient();
        IOException exception = assertThrows(IOException.class, () -> AuthClient.authenticate("testUser", "testPassword"));

        assertEquals("Network error", exception.getMessage());
    }

    @Test
    void testGetToken() throws IOException{
        String mockToken = "mockJwtToken";
        AuthClient authClient = new AuthClient();
        AuthClient.authenticate("testUser", "testPassword");
        AuthClient.token = mockToken;

        String token = AuthClient.getToken();
        assertEquals(mockToken, token);
    }
}

