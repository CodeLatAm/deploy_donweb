package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.user.UserBasicInfoResponse;
import com.abel.sistema_gestion.dto.user.UserStatusResponse;
import com.abel.sistema_gestion.dto.user.UserUpdateRequest;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class UserController {


    private final UserService userService;

    @GetMapping("/me")
    public String getAuthenticatedUser() {
        return userService.getAuthenticatedUsername();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserBasicInfoResponse> getInfoBasicUserId(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.getInfoBasicUserId(userId));
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<UserStatusResponse> getStatus(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.getStatus(userId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserBasicInfoResponse>> getAllUsersByAdmin() {
        List<UserBasicInfoResponse> response = userService.getAllUsersByAdmin();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/count")
    public ResponseEntity<Long> getUsersCount() {
        Long quantityUsers = userService.countUsers();
        return ResponseEntity.ok(quantityUsers);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<MessageResponse> update(@PathVariable Integer id, @RequestBody UserUpdateRequest request){
        userService.update(id, request);
        return ResponseEntity.ok(new MessageResponse("Usuario actualizado", HttpStatus.OK));
    }

}
