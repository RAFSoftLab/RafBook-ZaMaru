package com.example.demo3.Test;

import com.example.demo3.Controller.ApiClientUser;
import com.example.demo3.Model.NewUserDTO;
import com.example.demo3.Model.Person;
import com.example.demo3.repository.MainRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class ApiClientUserTest {

    private static final String BASE_URL = "http://192.168.124.28:8080/api/users";
    private ApiClientUser apiClientUser;
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
        apiClientUser = new ApiClientUser();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetUsers_Success() throws IOException {

        String mockJsonResponse = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"role\":[\"USER\"]}]";


        ResponseBody responseBody = ResponseBody.create(mockJsonResponse, MediaType.get("application/json"));


        Mockito.when(mockResponse.body()).thenReturn(responseBody);
        Mockito.when(mockResponse.isSuccessful()).thenReturn(true);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);


        List<Person> users = ApiClientUser.getUsers();


        assertNotNull(users);
        assertEquals(1, users.size());


        Person user = users.get(0);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertTrue(user.getRole().contains("USER"));
    }

    @Test
    void testAddUser_Success() throws IOException {

        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setFirstName("testUser");
        newUserDTO.setLastName("Test Lastname");
        newUserDTO.setEmail("testuser@example.com");
        newUserDTO.setPassword("testPassword");
        newUserDTO.setMacAddress("00:14:22:01:23:45");
        newUserDTO.setRole("ADMIN");


        String json = objectMapper.writeValueAsString(newUserDTO);


        Mockito.when(mockResponse.isSuccessful()).thenReturn(true);
        Mockito.when(mockResponse.body()).thenReturn(ResponseBody.create(json, MediaType.get("application/json")));
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);


        boolean result = ApiClientUser.addUser(newUserDTO);


        assertTrue(result);
    }

    @Test
    void testDeleteUser_Success() throws IOException {
        List<String> roles = Arrays.asList("ADMIN", "USER");
        Person user = new Person("Ivana","Jankovic","ivj123","ivanajankovic1302@+@gmail.com",roles);

        Mockito.when(mockResponse.isSuccessful()).thenReturn(true);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        boolean result = ApiClientUser.deleteUser(user);
        assertTrue(result);
    }

    @Test
    void testUpdateUser_Success() throws IOException {
        List<String> roles = Arrays.asList("ADMIN", "USER");
        Person user = new Person("Ivana","Jankovic","ivj123","ivanajankovic1302@+@gmail.com",roles);
        String json = objectMapper.writeValueAsString(user);

        Mockito.when(mockResponse.isSuccessful()).thenReturn(true);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        Mockito.when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        boolean result = ApiClientUser.updateUser(user);
        assertTrue(result);
    }
}

