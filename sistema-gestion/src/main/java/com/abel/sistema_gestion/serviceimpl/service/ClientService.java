package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.client.ClientRequest;
import com.abel.sistema_gestion.dto.client.ClientResponse;
import com.abel.sistema_gestion.dto.client.ClientUpdateRequest;
import com.abel.sistema_gestion.entity.Client;
import com.abel.sistema_gestion.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    MessageResponse createClient(ClientRequest request);


    //List<ClientResponse> getAllClientsByUserId(Integer userId);

    ClientResponse getClientById(Integer id);

    void deleteClient(Integer clientId);

    ClientResponse updateClient(Integer clientId, ClientUpdateRequest request);

    List<ClientResponse> getAllClientsByVendorId(Integer vendorId);

    List<ClientResponse> getAllClientsByUserId(Integer userId);

    List<ClientResponse> searchByVendorIdAndAttribute(Integer vendorId, String keyword);

    Client getClientBy(Integer clientId);

    Long getSalesCount(Integer vendorId);

    Long countDistinctClientsByUserId(Integer userId);

    Page<ClientResponse> getAllClientsByVendorIdPage(Integer vendorId, Pageable pageable, String filter);

    void saveAll(List<Client> clientes);

    List<Client> findAllByVendedorAndActivoTrue(Vendor vendedor);
}
