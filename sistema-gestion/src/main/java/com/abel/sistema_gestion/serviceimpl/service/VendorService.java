package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.vendor.*;
import com.abel.sistema_gestion.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface VendorService {
    MessageResponse createVendor(VendorRequest request);

    List<VendorResponse> getAllVendorsByUserId(Integer userId);

    Vendor findById(Integer vendorId);


    MessageResponse vendorUpdate(VendorUpdateRequest request);

    void deletedVendor(Integer id);

    VendorResponse getAllVendorsByUserIdAndVendorId(Integer userId, Integer vendorId);

    MessageResponse modifySellerStatus(Integer id, Integer userId, String statusName);

    Integer quantityToBeShipped(Integer vendorId);

    Integer quantityToBeShippedDispatched(Integer vendorId);

    Integer getCountVendorsByUserId(Integer userId);

    Integer countDistinctClientsByUserId(Integer userId);

    VendorProfileResponse getInfoVendor(Integer vendorId);

    Page<VendorResponse> getAllVendorsByUserIdPage(Integer userId, Pageable pageable, String filter);

    void updateUserStatusVendors(Integer userId);

    void updateUserStatusFreeVendors(Integer userId);

    void actualizarPlan(Integer vendedorId, String nuevoPlan);
}
