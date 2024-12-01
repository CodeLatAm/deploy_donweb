package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.vendor.VendorProfileResponse;
import com.abel.sistema_gestion.dto.vendor.VendorRequest;
import com.abel.sistema_gestion.dto.vendor.VendorResponse;
import com.abel.sistema_gestion.dto.vendor.VendorUpdateRequest;
import com.abel.sistema_gestion.serviceimpl.service.VendorService;
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
@RequestMapping(value = "vendors")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class VendorController {

    private final VendorService vendorService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createVendor(@Valid @RequestBody VendorRequest request){
        MessageResponse response = vendorService.createVendor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<VendorResponse>> getAllVendorsByUserId(@PathVariable Integer userId){
        List<VendorResponse> responses = vendorService.getAllVendorsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @GetMapping("/all/{userId}/filter")
    public ResponseEntity<Page<VendorResponse>> getAllVendorsByUserIdPage(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorResponse> vendorResponses = vendorService.getAllVendorsByUserIdPage(userId,pageable,filter);
        return ResponseEntity.ok(vendorResponses);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> vendorUpdate(@Valid @RequestBody VendorUpdateRequest request){
        return ResponseEntity.ok(vendorService.vendorUpdate(request));
    }

    @DeleteMapping("/deleted/{id}")
    public ResponseEntity<MessageResponse> deletedVendor(@PathVariable Integer id){
        vendorService.deletedVendor(id);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Vendedor eliminado", HttpStatus.OK));
    }

    @GetMapping("/{userId}/{id}")
    public ResponseEntity<VendorResponse> getVendorByUserIdAndVendorId(@PathVariable Integer userId, @PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(vendorService.getAllVendorsByUserIdAndVendorId(userId, id));
    }

    @PutMapping("/status/{id}/{userId}")
    public ResponseEntity<MessageResponse> modifySellerStatus(@Valid
                                                              @PathVariable(required = true) Integer id,
                                                              @PathVariable(required = true) Integer userId,
                                                              @RequestParam(required = true) String statusName){
        MessageResponse response = vendorService.modifySellerStatus(id, userId, statusName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/shipped/{vendorId}")
    public ResponseEntity<Integer> quantityToBeShipped(@PathVariable Integer vendorId){
        return ResponseEntity.ok(vendorService.quantityToBeShipped(vendorId));
    }

    @GetMapping("/shipped/{vendorId}/dispatched")
    public ResponseEntity<Integer> quantityToBeShippedDispatched(@PathVariable Integer vendorId){
        return ResponseEntity.ok(vendorService.quantityToBeShippedDispatched(vendorId));
    }

    @GetMapping("/user/{userId}/count")
    public Integer getCountVendorsByUserId(@PathVariable Integer userId){
        return vendorService.getCountVendorsByUserId(userId);
    }

    @GetMapping("/clients/count/{userId}")
    public ResponseEntity<Integer> countDistinctClientsByUserId(@PathVariable Integer userId) {
        Integer count = vendorService.countDistinctClientsByUserId(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/profile/{vendorId}")
    public ResponseEntity<VendorProfileResponse> getInfoVendor(@PathVariable Integer vendorId){
        return ResponseEntity.ok(vendorService.getInfoVendor(vendorId));
    }

    @PutMapping("/{vendedorId}/plan")
    public ResponseEntity<String> actualizarPlan(
            @PathVariable Integer vendedorId,
            @RequestParam String nuevoPlan) {
        try {
            vendorService.actualizarPlan(vendedorId, nuevoPlan);
            return ResponseEntity.ok("El plan ha sido actualizado a " + nuevoPlan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
