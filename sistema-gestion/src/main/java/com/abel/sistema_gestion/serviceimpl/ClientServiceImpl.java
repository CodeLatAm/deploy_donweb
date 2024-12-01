package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.client.ClientRequest;
import com.abel.sistema_gestion.dto.client.ClientResponse;
import com.abel.sistema_gestion.dto.client.ClientUpdateRequest;
import com.abel.sistema_gestion.entity.Client;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.entity.Vendor;
import com.abel.sistema_gestion.exception.ClientAlreadyExistsException;
import com.abel.sistema_gestion.exception.ClientNotFoundException;
import com.abel.sistema_gestion.exception.UserNotFoundException;
import com.abel.sistema_gestion.exception.VendorNotFoundException;
import com.abel.sistema_gestion.mapper.ClientMapper;
import com.abel.sistema_gestion.repository.ClientRepository;
import com.abel.sistema_gestion.serviceimpl.service.ClientService;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import com.abel.sistema_gestion.serviceimpl.service.VendorService;
import com.abel.sistema_gestion.specification.ClientSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientServiceImpl  implements ClientService {

    private  ClientRepository clientRepository;
    private  ClientMapper clientMapper;
    private UserService userService;

    private VendorService vendorService;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper,
                             VendorService vendorService, UserService userService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.vendorService = vendorService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public MessageResponse createClient(ClientRequest request) {
        if(clientRepository.existsByVendorIdAndEmail(request.getVendorId(), request.getEmail())){
            throw new ClientAlreadyExistsException("El Email ya esta registrado");
        }
        if(clientRepository.existsByVendorIdAndDni(request.getVendorId(), request.getDni())){
            throw new ClientAlreadyExistsException("El Dni ya esta registrado");
        }
        Long clientCount = clientRepository.countClientsByVendorId(request.getVendorId());

        Client client = clientMapper.mapToClientRequest(request);
        Vendor vendor = vendorService.findById(request.getVendorId());
        if(vendor.getPlan().equals("FREE") && clientCount >= 10){
            throw new ClientNotFoundException("Has alcanzado el límite de 2 clientes en el plan FREE. Pasa a Premium para agregar más.");
        }
        vendor.addClient(client);
        clientRepository.save(client);
        return MessageResponse.builder()
                .message("Cliente creado")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @Override
    public List<ClientResponse> getAllClientsByVendorId(Integer vendorId) {
        List<Client> clients = clientRepository.findByVendorId(vendorId);
        List<ClientResponse> responses = clientMapper.mapToClientsResponse(clients);
        return responses;
    }

    @Override
    public ClientResponse getClientById(Integer id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> {
            return new ClientNotFoundException("Cliente no encontrado con id: " + id);
        });
        return clientMapper.mapToClientResponse(client);
    }

    @Override
    public void deleteClient(Integer id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> {
                    return new ClientNotFoundException("Cliente con id: " + id + " no encontrado");
                }
        );
        clientRepository.delete(client);
    }
    @Transactional
    @Override
    public ClientResponse updateClient(Integer clientId, ClientUpdateRequest request) {
        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new ClientNotFoundException("Clente no encontrado con id: " + clientId));
        clientMapper.mapToClientUpdateRequest(client, request);
        clientRepository.save(client);
        return clientMapper.mapToClientResponse(client);
    }

    @Override
    public List<ClientResponse> getAllClientsByUserId(Integer userId) {
        //TODO aca podemos traer todos los clientes por userId pero aun no estan las relaciones lo dejo para
        //TODO mas adelante por si sirve

        return null;
    }

    @Override
    public List<ClientResponse> searchByVendorIdAndAttribute(Integer vendorId, String keyword) {
        List<Client> clients = clientRepository.searchByVendorIdAndAttribute(vendorId, keyword);
        return clientMapper.mapToClientsResponse(clients);
    }

    @Override
    public Client getClientBy(Integer clientId) {

        return clientRepository.findById(clientId).orElseThrow(
                () -> new ClientNotFoundException("Cliente no encontrado con id: " + clientId)
        );
    }

    @Override
    public Long getSalesCount(Integer vendorId) {

        return clientRepository.countClientsByVendorId(vendorId);
    }

    @Override
    public Long countDistinctClientsByUserId(Integer userId) {
        User user = userService.getUserByUserId(userId);
        if(user == null){
            throw  new UserNotFoundException("Usuario no encontrado con id: " + userId);
        }
        return clientRepository.countDistinctClientsByUserId(userId);
    }

    @Override
    public Page<ClientResponse> getAllClientsByVendorIdPage(Integer vendorId, Pageable pageable, String filter) {
        Specification<Client> specification = Specification.where(ClientSpecification.byVendorId(vendorId));

        if (filter != null && !filter.isEmpty()) {
            specification = specification.and(ClientSpecification.filterByAttributes(filter));
        }
        Page<Client> clients = clientRepository.findAll(specification, pageable);
        return clientMapper.mapToClientPageResponse(clients);
    }

    @Override
    public void saveAll(List<Client> cliens) {
        clientRepository.saveAll(cliens);
    }

    @Override
    public List<Client> findAllByVendedorAndActivoTrue(Vendor vendor) {
        return clientRepository.findAllByVendorAndActivoTrue(vendor);
    }
}
