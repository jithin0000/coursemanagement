package com.jithin.coursemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jithin.coursemanagement.dto.StudentRegisterRequest;
import com.jithin.coursemanagement.exceptions.UnAuthenticatedUser;
import com.jithin.coursemanagement.models.CUser;
import com.jithin.coursemanagement.models.Role;
import com.jithin.coursemanagement.models.UserProfile;
import com.jithin.coursemanagement.services.RoleService;
import com.jithin.coursemanagement.services.UserProfileService;
import com.jithin.coursemanagement.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileControllerTest {

    public static final String API_PROFILE = "/api/profile";
    public static final String VALID_GMAIL_COM = "valid@gmail.com";
    public static final String VALID_USERNAME1 = "valid username";
    private final ObjectMapper mapper = new ObjectMapper();
    private final String VALID_USERNAME = "validUsername@gmail.com";
    @MockBean
    UserProfileService userProfileService;
    @MockBean
    Principal principal;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private RoleService roleService;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldValidateUserProfileRequestBody() throws Exception {

        mockMvc.perform(post(API_PROFILE + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new StudentRegisterRequest()))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturn401IfNoToken() throws Exception {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());
        mockMvc.perform(post(API_PROFILE + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validStudentRegisterRequest()))
                )
                .andExpect(status().isUnauthorized());

        ArgumentCaptor<String> em = ArgumentCaptor.forClass(String.class);
        verify(userService, times(1)).findUserByEmail(em.capture());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldCheckUserWithEmailAlreadyExist() throws Exception {

        when(principal.getName()).thenReturn(VALID_USERNAME);
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(fakeUser()));

        mockMvc.perform(post(API_PROFILE + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(validStudentRegisterRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        verify(userService, times(2)).findUserByEmail(ac.capture());

    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturn400IfRoleNameDoesNotExist() throws Exception {

        when(principal.getName()).thenReturn(VALID_USERNAME);
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(fakeUser()));
        when(userService.findUserByEmail(VALID_GMAIL_COM)).thenReturn(Optional.empty());
        when(roleService.findByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post(API_PROFILE + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(validStudentRegisterRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> rc = ArgumentCaptor.forClass(String.class);
        verify(userService, times(2)).findUserByEmail(ac.capture());
        verify(roleService, times(1)).findByName(rc.capture());

    }

    @Test
    @WithMockUser(username = "admin")
    void shouldRegisterUser() throws Exception {
        when(principal.getName()).thenReturn(VALID_USERNAME);
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(fakeUser()));
        when(userService.findUserByEmail(VALID_GMAIL_COM)).thenReturn(Optional.empty());
        when(roleService.findByName(anyString())).thenReturn(Optional.of(new Role()));
        when(userProfileService.create(any(UserProfile.class))).thenReturn(fakeUserProfiles().get(0));

        mockMvc.perform(post(API_PROFILE + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(validStudentRegisterRequest()))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(VALID_USERNAME1)));
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        verify(userService, times(2)).findUserByEmail(ac.capture());
        ArgumentCaptor<String> rc = ArgumentCaptor.forClass(String.class);
        verify(roleService, times(1)).findByName(rc.capture());
        ArgumentCaptor<UserProfile> uc = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileService, times(1)).create(uc.capture());
    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnUnAuthorizedIfNoTokenGetProfiles() throws Exception {
        try {
            when(userService.findUserByEmail(anyString())).thenThrow(UnAuthenticatedUser.class);
            mockMvc.perform(get(API_PROFILE + "/all").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());

            ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
            verify(userService, times(1)).findUserByEmail(ac.capture());

        } catch (Exception e) {
            System.out.println("exception is " + e.getLocalizedMessage());
        }

    }

    @Test
    @WithMockUser(username = "admin")
    void shouldReturnGetProfiles() throws Exception {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(fakeUser()));
        when(userProfileService.getProfileOfUser(anyLong())).thenReturn(fakeUserProfiles());
        mockMvc.perform(get(API_PROFILE + "/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(5)));

        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        verify(userService, times(1)).findUserByEmail(ac.capture());
        ArgumentCaptor<Long> lc = ArgumentCaptor.forClass(Long.class);
        verify(userProfileService, times(1)).getProfileOfUser(lc.capture());

    }


    @Test
    @WithMockUser(username = "admin")
    void shouldReturnUserProfileBy401Id() throws Exception {
        try {
            when(userService.findUserByEmail(anyString())).thenThrow(UnAuthenticatedUser.class);
            mockMvc.perform(get(API_PROFILE + "/detail").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());

            ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
            verify(userService, times(1)).findUserByEmail(ac.capture());

        } catch (Exception e) {
            System.out.println("exception is " + e.getLocalizedMessage());
        }

    }
    @Test
    @WithMockUser(username = "admin")
    void shouldReturnUserProfileById() throws Exception {
        try {
            when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(fakeUser()));
            mockMvc.perform(get(API_PROFILE + "/detail").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
            verify(userService, times(1)).findUserByEmail(ac.capture());
        } catch (Exception e) {
            System.out.println("exception is " + e.getLocalizedMessage());
        }

    }


    private List<UserProfile> fakeUserProfiles() {
        List<UserProfile> userProfiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserProfile u = new UserProfile();
            u.setUsername(VALID_USERNAME1);
            u.setId((long) i);
            u.setUser(new CUser());
            userProfiles.add(u);
        }
        return userProfiles;
    }

    private StudentRegisterRequest validStudentRegisterRequest() {
        StudentRegisterRequest student = new StudentRegisterRequest();
        student.setEmail(VALID_GMAIL_COM);
        student.setUsername(VALID_USERNAME1);
        student.setPassword("valid@gmail.com");
        student.setPhoneNumber("123456");
        return student;
    }

    private CUser fakeUser() {
        CUser user = new CUser();
        user.setId(10L);
        user.setEmail(VALID_USERNAME);
        return user;
    }
}