package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.entity.Vendor;
import com.abel.sistema_gestion.entity.VendorStatusHistory;
import com.abel.sistema_gestion.enums.VendorStatus;

public interface VendorStatusHistoryService {
    VendorStatusHistory createVendorStatusHistory(Vendor vendor);
}
