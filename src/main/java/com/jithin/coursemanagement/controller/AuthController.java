package com.jithin.coursemanagement.controller;

import com.jithin.coursemanagement.dto.ErrorMessage;
import com.jithin.coursemanagement.dto.JsonTokenResponse;
import com.jithin.coursemanagement.dto.LoginRequest;
import com.jithin.coursemanagement.dto.RegisterRequest;
import com.jithin.coursemanagement.exceptions.RoleInvalidIdException;
import com.jithin.coursemanagement.models.CUser;
import com.jithin.coursemanagement.models.Role;
import com.jithin.coursemanagement.repository.AuthRepository;
import com.jithin.coursemanagement.services.RoleService;
import com.jithin.coursemanagement.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AuthRepository repository;
    @Autowired
    private RoleService roleService;

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request)
    {
        Optional<CUser> cUserByEmail = repository.findCUserByEmail(request.getEmail());
        if (cUserByEmail.isPresent())
            return ResponseEntity.badRequest().body(new ErrorMessage("already user exist with this mail "+request.getEmail()));

        Role role = roleService.findByName("ROLE_" + request.getRoleName())
                .orElseThrow(
                        () -> new RoleInvalidIdException("no role with this name " + request.getRoleName())
                );

        CUser user =new CUser();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        repository.save(user);
        return ResponseEntity.ok(new ErrorMessage("user created successfully"));

    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@Valid@RequestBody LoginRequest request)
    {
       try{
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
           );
           SecurityContextHolder.getContext().setAuthentication(authentication);
           UserDetails principal = (UserDetails)authentication.getPrincipal();

           HashMap<String, Object> roleMap = new HashMap<>();
           String roleNames = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
           roleMap.put("roles", roleNames);
           roleMap.put("username",principal.getUsername());
          String token= jwtTokenUtil.generateToken(principal.getUsername(), roleMap);
          return ResponseEntity.ok(new JsonTokenResponse(token));


       }catch (Exception e)
       {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage(e.getLocalizedMessage()));
       }


    }



}
