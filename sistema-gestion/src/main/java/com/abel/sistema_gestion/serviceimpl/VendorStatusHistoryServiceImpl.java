package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.entity.Vendor;
import com.abel.sistema_gestion.entity.VendorStatusHistory;

import com.abel.sistema_gestion.repository.VendorStatusHistoryRepository;
import com.abel.sistema_gestion.serviceimpl.service.VendorStatusHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;


import java.time.LocalDateTime;

@Service
public class VendorStatusHistoryServiceImpl implements VendorStatusHistoryService {

    private VendorStatusHistoryRepository vendorStatusHistoryRepository;

    public VendorStatusHistoryServiceImpl(VendorStatusHistoryRepository vendorStatusHistoryRepository) {
        this.vendorStatusHistoryRepository = vendorStatusHistoryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public VendorStatusHistory createVendorStatusHistory(Vendor vendor) {
        VendorStatusHistory history = new VendorStatusHistory();
        history.setVendorStatus(vendor.getVendorStatus());
        history.setChangedAt(LocalDateTime.now());
        history.setVendor(vendor);
        vendorStatusHistoryRepository.save(history);
        return history;
    }
}
