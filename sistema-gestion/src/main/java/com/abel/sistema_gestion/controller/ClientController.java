package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.client.ClientRequest;
import com.abel.sistema_gestion.dto.client.ClientResponse;
import com.abel.sistema_gestion.dto.client.ClientUpdateRequest;
import com.abel.sistema_gestion.serviceimpl.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/client")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createClient(@Valid @RequestBody ClientRequest request){
        MessageResponse response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all/{vendorId}")
    public ResponseEntity<List<ClientResponse>> getAllClientsByVendorId(@PathVariable Integer vendorId){
        List<ClientResponse> responses = clientService.getAllClientsByVendorId(vendorId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Integer id){
        ClientResponse response = clientService.getClientById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id){
            clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Integer id,@Valid @RequestBody ClientUpdateRequest request){
        ClientResponse response = clientService.updateClient(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all/userId/{userId}")
    public ResponseEntity<List<ClientResponse>> getAllClientByUserId(@PathVariable Integer userId){
        List<ClientResponse> responses = clientService.getAllClientsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ClientResponse>> filterClients(@RequestParam Integer vendorId,
                                                              @RequestParam String keyword){
        String prefix = keyword.length() > 3 ? keyword.substring(0, 3) : keyword;
        List<ClientResponse> responses = clientService.searchByVendorIdAndAttribute(vendorId, keyword);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/vendor/{vendorId}/clients-count")
    public ResponseEntity<Long> getSalesCount(@PathVariable Integer vendorId) {
        Long salesCount = clientService.getSalesCount(vendorId);
        return ResponseEntity.ok(salesCount);
    }

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @GetMapping("/count-by-user/{userId}")
    public ResponseEntity<Long> countDistinctClientsByUserId(@PathVariable Integer userId) {
        Long clientCount = clientService.countDistinctClientsByUserId(userId);
        return new ResponseEntity<>(clientCount, HttpStatus.OK);
    }

    @GetMapping("/all/{vendorId}/filter")
    public ResponseEntity<Page<ClientResponse>> getAllClientsByVendorIdPage(
            @PathVariable Integer vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<ClientResponse> clients = clientService.getAllClientsByVendorIdPage(vendorId,pageable,filter);
        return ResponseEntity.ok(clients);
    }



}
