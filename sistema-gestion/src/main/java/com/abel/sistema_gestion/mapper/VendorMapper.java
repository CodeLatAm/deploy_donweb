package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.client.ClientResponse;
import com.abel.sistema_gestion.dto.vendor.VendorProfileResponse;
import com.abel.sistema_gestion.dto.vendor.VendorRequest;
import com.abel.sistema_gestion.dto.vendor.VendorResponse;
import com.abel.sistema_gestion.dto.vendor.VendorUpdateRequest;
import com.abel.sistema_gestion.dto.vendorSale.VendorBasicResponse;
import com.abel.sistema_gestion.entity.Cart;
import com.abel.sistema_gestion.entity.Client;
import com.abel.sistema_gestion.entity.Vendor;
import com.abel.sistema_gestion.enums.Role;
import com.abel.sistema_gestion.enums.UserStatus;
import com.abel.sistema_gestion.enums.VendorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VendorMapper {

    private final PasswordEncoder passwordEncoder;

    public Vendor mapToVendorRequest(VendorRequest request) {
        return Vendor.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .email(request.getEmail())
                .surname(request.getSurname())
                .address(request.getAddress())
                .role(Role.VENDEDOR)
                .password(passwordEncoder.encode(request.getPassword()))
                .clientList(new ArrayList<>())
                .vendorSales(new ArrayList<>())
                .vendorStatus(VendorStatus.ACTIVE)
                .startOfActivity(LocalDate.now())
                .activo(true)
                .build();
    }

    public List<VendorResponse> mapToVendorResponseList(List<Vendor> vendors) {
        return vendors.stream()
                .map(this::mapToVendorResponse)
                .collect(Collectors.toList());
    }

    public VendorResponse mapToVendorResponse(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .email(vendor.getEmail())
                .surname(vendor.getSurname())
                .name(vendor.getName())
                .userId(vendor.getUserId())
                .role(vendor.getRole())
                .startOfActivity(vendor.getStartOfActivity().toString())
                //.clientResponses(this.mapToClientResponseList(vendor.getClientList()))
                .vendorStatus(vendor.getVendorStatus().name())
                .build();
    }

    private List<ClientResponse> mapToClientResponseList(List<Client> clients) {
        return clients.stream()
                .map(this::mapToClientResponse)
                .collect(Collectors.toList());
    }

    private ClientResponse mapToClientResponse(Client client) {
        return ClientResponse.builder()
                .VendorId(client.getVendor().getId())
                .phone(client.getPhone())
                .cp(client.getCp())
                .address(client.getAddress())
                .betweenStreets(client.getBetweenStreets())
                .location(client.getLocation())
                .email(client.getEmail())
                .id(client.getId())
                .address(client.getAddress())
                .name(client.getName())
                .build();
    }


    public void mapToVendorUpdate(Vendor vendor, VendorUpdateRequest request) {
        vendor.setName(request.getName());
        vendor.setSurname(request.getSurname());
        vendor.setEmail(request.getEmail());
    }

    public Vendor mapToVendorUpdateStatus(Vendor vendor, String statusName) {
        if(statusName.equals("Inactivo")){
            vendor.setVendorStatus(VendorStatus.INACTIVE);
        }
        if(statusName.equals("Desactivado")){
            vendor.setVendorStatus(VendorStatus.SUSPENDED);
        }
        if(statusName.equals("Activado")){
            vendor.setVendorStatus(VendorStatus.ACTIVE);
        }

        return vendor;
    }

    public VendorBasicResponse mapToVendorBasicResponse(Vendor vendor) {
        return VendorBasicResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .surname(vendor.getSurname())

                .build();
    }

    public VendorProfileResponse mapToVendorProfileResponse(Vendor vendor) {
            Long sales = (long) vendor.getVendorSales().size();
            Long clients = (long) vendor.getClientList().size();
        return VendorProfileResponse.builder()
                .id(vendor.getId())
                .email(vendor.getEmail())
                .surname(vendor.getSurname())
                .name(vendor.getName())
                .userId(vendor.getUserId())
                .role(vendor.getRole())
                .vendorSales(sales)
                .clients(clients)
                .address(vendor.getAddress())
                .vendorStatus(vendor.getVendorStatus().name())
                .build();
    }

    public Page<VendorResponse> mapToVendorPageResponse(Page<Vendor> vendors) {
        return vendors.map(this::mapToVendorResponse);
    }
}
