package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.client.ClientRequest;
import com.abel.sistema_gestion.dto.client.ClientResponse;
import com.abel.sistema_gestion.dto.client.ClientUpdateRequest;
import com.abel.sistema_gestion.dto.vendorSale.ClientVendorSaleResponse;
import com.abel.sistema_gestion.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientMapper {
    public Client mapToClientRequest(ClientRequest request) {
        return Client.builder()
                .name(request.getName())
                .cp(request.getCp())
                .phone(request.getPhone())
                .address(request.getAddress())
                .email(request.getEmail())
                .betweenStreets(request.getBetweenStreets())
                .location(request.getLocation())
                .province(request.getProvince())
                .dni(request.getDni())
                .activo(true)
                //.vendorId(request.getVendorId())
                .build();
    }

    public List<ClientResponse> mapToClientsResponse(List<Client> clients) {
        return clients.stream()
                .map(this::mapToClientResponse)
                .collect(Collectors.toList());
    }
    public ClientResponse mapToClientResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .address(client.getAddress())
                .phone(client.getPhone())
                .cp(client.getCp())
                .VendorId(client.getVendor().getId())
                .email(client.getEmail())
                .betweenStreets(client.getBetweenStreets())
                .location(client.getLocation())
                .province(client.getProvince())
                .dni(client.getDni())
                .activo(client.isActivo())
                .build();
    }

    public void mapToClientUpdateRequest(Client client, ClientUpdateRequest request) {
        client.setName(request.getName());
        client.setCp(request.getCp());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());
        //client.setVendorId(request.getVendorId());
        client.setEmail(request.getEmail());
        client.setBetweenStreets(request.getBetweenStreets());
        client.setLocation(request.getLocation());
        client.setEmail(request.getEmail());
        client.setDni(request.getDni());
    }

    public ClientVendorSaleResponse mapToClientVendorSaleResponse(Client client) {
        return ClientVendorSaleResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .address(client.getAddress())
                .phone(client.getPhone())
                .cp(client.getCp())
                .VendorId(client.getVendor().getId())
                .email(client.getEmail())
                .betweenStreets(client.getBetweenStreets())
                .location(client.getLocation())
                .province(client.getProvince())
                .dni(client.getDni())
                .build();
    }

    public Page<ClientResponse> mapToClientPageResponse(Page<Client> clients) {
        return clients.map(this::mapToClientResponse);
    }
}
