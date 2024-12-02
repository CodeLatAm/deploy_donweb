package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.vendor.VendorProfileResponse;
import com.abel.sistema_gestion.dto.vendor.VendorRequest;
import com.abel.sistema_gestion.dto.vendor.VendorResponse;
import com.abel.sistema_gestion.dto.vendor.VendorUpdateRequest;
import com.abel.sistema_gestion.entity.*;
import com.abel.sistema_gestion.enums.SaleStatus;
import com.abel.sistema_gestion.enums.UserStatus;
import com.abel.sistema_gestion.enums.VendorStatus;
import com.abel.sistema_gestion.exception.VendorAlreadyExistsException;
import com.abel.sistema_gestion.exception.VendorNotFoundException;
import com.abel.sistema_gestion.mapper.VendorMapper;
import com.abel.sistema_gestion.repository.ClientRepository;
import com.abel.sistema_gestion.repository.VendorRepository;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import com.abel.sistema_gestion.serviceimpl.service.VendorService;
import com.abel.sistema_gestion.serviceimpl.service.VendorStatusHistoryService;
import com.abel.sistema_gestion.specification.VendorSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    private final UserService userService;

    private final VendorStatusHistoryService statusHistoryService;

   private final ClientRepository clientRepository;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper, VendorStatusHistoryService statusHistoryService,
                             UserService userService, ClientRepository clientRepository) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
        this.statusHistoryService = statusHistoryService;
        this.userService = userService;
        this.clientRepository = clientRepository;

    }

    @Transactional
    @Override
    public MessageResponse createVendor(VendorRequest request) {
        //TODO modificar la app permite unico email por vendedor para todos los users
        if(vendorRepository.existsByEmail(request.getEmail())){
            throw new VendorAlreadyExistsException("El vendedor ya existe");
        }
        if(request.getPlan().equals("FREE") && vendorRepository.countByUserIdAndPlan(request.getUserId(),request.getPlan()) >= 1){
            throw new VendorNotFoundException("Ya tienes un vendedor en el plan FREE. Pasa a Premium para crear más.");
        }
        User user =  userService.getUserByUserId(request.getUserId());
        Vendor vendor = vendorMapper.mapToVendorRequest(request);
        vendor.setUserStatus(user.getUserStatus());
        vendor.setPlan(user.getUserStatus().name());
        Cart cart = new Cart();
        vendor.setCart(cart);
        cart.setVendor(vendor);
        vendorRepository.save(vendor);
        return MessageResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("Vendedor creado")
                .build();
    }

    @Override
    public List<VendorResponse> getAllVendorsByUserId(Integer userId) {
        if(vendorRepository.count() > 0 && !vendorRepository.existsByUserId(userId)){
            throw new VendorNotFoundException("El User con userId: " + userId + " no esta registrado");
        }

        List<Vendor> vendors = vendorRepository.findByUserId(userId);
        //List<Vendor> vendorsActive = vendorRepository.findAllByUserIdAndActivoTrue(userId);
        return vendorMapper.mapToVendorResponseList(vendors);

    }

    @Override
    public Vendor findById(Integer vendorId) {
        return vendorRepository.findById(vendorId).orElseThrow(() -> new VendorNotFoundException("El vendedor no existe"));
    }

    @Override
    public MessageResponse vendorUpdate(VendorUpdateRequest request) {
        Optional<Vendor> vendor = vendorRepository.findByUserIdAndId(request.getUserId(), request.getId());
        if(vendor.isEmpty()){
            throw new VendorNotFoundException("Vendedor no encontrado");
        }
        vendorMapper.mapToVendorUpdate(vendor.get(), request);
        vendorRepository.save(vendor.get());
        return new MessageResponse("Vendedor actualizado", HttpStatus.OK);
    }

    @Transactional
    @Override
    public void deletedVendor(Integer id) {
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new VendorNotFoundException("Vendedor not fount"));
        vendorRepository.delete(vendor);
    }

    @Override
    public VendorResponse getAllVendorsByUserIdAndVendorId(Integer userId, Integer id) {
        Vendor vendor = vendorRepository.findByUserIdAndId(userId, id).orElseThrow(() -> new VendorNotFoundException("Vendedor not fount"));
        return vendorMapper.mapToVendorResponse(vendor);

    }

    @Transactional
    @Override
    public MessageResponse modifySellerStatus(Integer id, Integer userId, String statusName) {
        Vendor vendor = vendorRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new VendorNotFoundException("Vendedor no encontrado"));

        switch (statusName) {
            case "Inactivo":
                vendor.setVendorStatus(VendorStatus.INACTIVE);
                break;
            case "Desactivado":
                vendor.setVendorStatus(VendorStatus.SUSPENDED);
                break;
            case "Activado":
                vendor.setVendorStatus(VendorStatus.ACTIVE);
                break;
            default:
                throw new IllegalArgumentException("Estado inválido: " + statusName);
        }

        vendorRepository.save(vendor);
        System.out.println("Vendor guardado con nuevo estado: " + vendor.getVendorStatus());

        // Este método es crucial, si falla, la transacción no se completará
        statusHistoryService.createVendorStatusHistory(vendor);
        System.out.println("Historial de estado creado");

        return new MessageResponse("Estado actualizado", HttpStatus.OK);
    }

    @Override
    public Integer quantityToBeShipped(Integer vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendedor no encontrado con id: " + vendorId));

        return (int) vendor.getVendorSales().stream()
                .filter(v -> v.getSaleStatus() == SaleStatus.PENDIENTE)  // Filtrar las ventas con estatus "PENDIENTE"
                .count();
    }

    @Override
    public Integer quantityToBeShippedDispatched(Integer vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendedor no encontrado con id: " + vendorId));

        return (int) vendor.getVendorSales().stream()
                .filter(v -> v.getSaleStatus() == SaleStatus.DESPACHADO)  // Filtrar las ventas con estatus "Despachado"
                .count();
    }

    @Override
    public Integer getCountVendorsByUserId(Integer userId) {
        return vendorRepository.getCountVendorsByUserId(userId);
    }

    @Override
    public Integer countDistinctClientsByUserId(Integer userId) {
        if(!vendorRepository.existsById(userId)){
            return 0;
        }
        return vendorRepository.countDistinctClientsByUserId(userId);
    }

    @Override
    public VendorProfileResponse getInfoVendor(Integer vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow(
                () -> {
                    return new VendorNotFoundException("Vendedor no encontrado con id: " + vendorId);
                });
        return vendorMapper.mapToVendorProfileResponse(vendor);
    }

    @Override
    public Page<VendorResponse> getAllVendorsByUserIdPage(Integer userId, Pageable pageable, String filter) {
        User user = userService.getUserByUserId(userId);
        Specification<Vendor> specification = Specification.where(VendorSpecification.byUserIdAndActivo(userId))
                .and(VendorSpecification.filterByAttributes(filter));

        if(filter != null && !filter.isEmpty()) {
            specification = specification.and(VendorSpecification.filterByAttributes(filter));
        }
        Page<Vendor> vendors = vendorRepository.findAll(specification, pageable);
        System.out.print("Servicio getAllVendorsByUserIdPage ");
        return vendorMapper.mapToVendorPageResponse(vendors);
    }

    @Override
    public void updateUserStatusVendors(Integer userId) {
        List<Vendor> vendors = vendorRepository.findAllByUserId(userId);
        vendors.forEach(this::actualizarEstados);
        vendorRepository.saveAll(vendors);
    }

    private void actualizarEstados(Vendor vendor) {
        vendor.setPlan("PREMIUM");
        vendor.setUserStatus(UserStatus.PREMIUM);
        vendor.setActivo(true);
        //TODO buscar los clientes dados de activo false y activarlos nuevamente si hay
        // Activar solo los clientes inactivos
        vendor.getClientList().stream()
                .filter(client -> !client.isActivo())
                .forEach(client -> client.setActivo(true));
    }

    @Override
    public void updateUserStatusFreeVendors(Integer userId) {
    List<Vendor> vendors = vendorRepository.findAllByUserId(userId);

    vendors.forEach(vendor -> {
        vendor.setUserStatus(UserStatus.FREE);
        vendor.setPlan("FREE");
    });

    vendorRepository.saveAll(vendors);
    vendors.forEach(this::limitarClientesYVendedores);
}

    //TODO PROBAR
    @Transactional
    @Override
    public void actualizarPlan(Integer vendedorId, String nuevoPlan) {
        Vendor vendedor = vendorRepository.findById(vendedorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendedor no encontrado"));

        vendedor.setPlan(nuevoPlan);
        vendorRepository.save(vendedor);

        if ("FREE".equals(nuevoPlan)) {
            this.limitarClientesYVendedores(vendedor);
        }
    }

    private void limitarClientesYVendedores(Vendor vendedor) {
        List<Vendor> vendedores = vendorRepository.findAllByUserIdAndActivoTrue(vendedor.getUserId());
        if (vendedores.size() > 1) {
            vendedores.subList(1, vendedores.size()).forEach(v -> v.setActivo(false));
        }

        List<Client> clientes = clientRepository.findAllByVendorAndActivoTrue(vendedor);
        if (clientes.size() > 2) {
            clientes.subList(2, clientes.size()).forEach(c -> c.setActivo(false));
        }

        vendorRepository.saveAll(vendedores);
        clientRepository.saveAll(clientes);
    }
}
