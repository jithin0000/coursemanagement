package com.jithin.coursemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jithin.coursemanagement.dto.RoleRequest;
import com.jithin.coursemanagement.exceptions.RoleInvalidIdException;
import com.jithin.coursemanagement.models.Role;
import com.jithin.coursemanagement.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @MockBean
    RoleService roleService;
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/api/role/create")
                        .content(mapper.writeValueAsBytes(new Role()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).
                andExpect(status().is(400));
    }


    @Test
    void shouldReturnRoleAfterCreated() throws Exception {
        when(roleService.create(any(Role.class))).thenReturn(roles().get(0));
        mockMvc.perform(post("/api/role/create")
                        .content(mapper.writeValueAsBytes(new RoleRequest("name")))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).
                andExpect(status().is(201))
                .andExpect(jsonPath("$.name", is("name 0")));
        ArgumentCaptor<Role> roleCapture = ArgumentCaptor.forClass(Role.class);
        verify(roleService, times(1)).create(roleCapture.capture());
    }

    @Test
    void shouldReturnRoles() throws Exception {
        when(roleService.getAll()).thenReturn(roles());
        mockMvc.perform(get("/api/role/all")).
                andExpect(status().is(200)).
                andExpect(jsonPath("$", hasSize(5)));
        verify(roleService, times(1)).getAll();

    }

    @Test
    void shouldGetRoleById() throws Exception {
        when(roleService.findById(anyLong())).thenReturn(Optional.of(roles().get(0)));
        mockMvc.perform(get("/api/role/12")).
                andExpect(status().is(200)).
                andExpect(jsonPath("$.name", is("name 0")));
        ArgumentCaptor<Long> idCap = ArgumentCaptor.forClass(Long.class);
        verify(roleService, times(1)).findById(idCap.capture());

    }

    @Test
    void shouldGetRoleByIdReturnNotFound() throws Exception {
        when(roleService.findById(anyLong())).thenThrow(RoleInvalidIdException.class);
        mockMvc.perform(get("/api/role/12")).
                andExpect(status().is(404));
        ArgumentCaptor<Long> idCap = ArgumentCaptor.forClass(Long.class);
        verify(roleService, times(1)).findById(idCap.capture());

    }

    private List<Role> roles() {
        List<Role> rs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Role r = new Role();
            r.setName("name " + i);
            rs.add(r);
        }
        return rs;
    }
}













