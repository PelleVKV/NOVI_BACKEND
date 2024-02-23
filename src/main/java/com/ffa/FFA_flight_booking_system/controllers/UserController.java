package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.dto.UserDTO;
import com.ffa.FFA_flight_booking_system.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.ffa.FFA_flight_booking_system.services.UserService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET MAPPING, RETRIEVING DATA

    @GetMapping
    public ResponseEntity<ArrayList<User>> getAllUsers() {
        return new ResponseEntity<>(new ArrayList<>(userService.getAllUsers()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @GetMapping("/username")
    public String getUsername(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            return "Current logged-in username: " + username;
        } else {
            return "No user is currently authenicated";
        }
    }

    @GetMapping("/{username}/isAdmin")
    public ResponseEntity<Boolean> isUserAdmin(@PathVariable String username) {
        try {
            UserDTO dto = userService.getUser(username);
            if (dto != null) {
                if (userService.hasAdminAuthority(dto)) {
                    return ResponseEntity.ok().body(true);
                } else {
                    return ResponseEntity.ok().body(false);
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error finding user with authority {}: {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST MAPPING, SENDING DATA

    @PostMapping("/create_multiple_users")
    public ResponseEntity<List<User>> createUsers(@RequestBody List<User> users) {
        return new ResponseEntity<>(userService.createUsers(users), HttpStatus.CREATED);
    }

    @PostMapping(value = "/create_user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        String newUsername = userService.createUser(userDTO);
        userService.addAuthority(newUsername, "USER");

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
        try {
            String authorityName = (String) fields.get("authority");
            userService.addAuthority(username, authorityName);
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // PUT MAPPING, CHANGING DATA

    @PutMapping("/{username}")
    public ResponseEntity<UserDTO> updateUserByUsername(@PathVariable("username") String username, @RequestBody UserDTO updatedUserDTO) {
        try {
            UserDTO updatedUser = userService.updateUserByUsername(username, updatedUserDTO);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            logger.error("Error updating user with username {}: {}", username, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE MAPPING, DELETING DATA

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
