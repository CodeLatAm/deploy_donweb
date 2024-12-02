package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.vendorSale.ClientVendorSaleResponse;
import com.abel.sistema_gestion.dto.vendorSale.PaymentTypeSalesResponse;
import com.abel.sistema_gestion.dto.vendorSale.VendorSaleResponse;
import com.abel.sistema_gestion.dto.vendorSale.VendorSalesRequest;
import com.abel.sistema_gestion.entity.*;
import com.abel.sistema_gestion.enums.SaleStatus;
import com.abel.sistema_gestion.enums.VendorStatus;
import com.abel.sistema_gestion.exception.*;
import com.abel.sistema_gestion.mapper.VendorSalesMapper;
import com.abel.sistema_gestion.repository.CartItemRepository;
import com.abel.sistema_gestion.repository.VendorSaleRepository;
import com.abel.sistema_gestion.serviceimpl.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
public class VendorSaleServiceImpl implements VendorSaleService {

    private VendorSaleRepository vendorSaleRepository;
    private CartService cartService;
    private ClientService clientService;
    private VendorService vendorService;
    private VendorSalesMapper vendorMapper;
    private CartItemRepository cartItemRepository;
    private ArticleService articleService;
    private UserService userService;

    public VendorSaleServiceImpl(VendorSaleRepository vendorSaleRepository, CartService cartService,
                                 ClientService clientService, VendorService vendorService,
                                 VendorSalesMapper vendorMapper, CartItemRepository cartItemRepository,
                                 ArticleService articleService, UserService userService) {
        this.vendorSaleRepository = vendorSaleRepository;
        this.cartService = cartService;
        this.clientService = clientService;
        this.vendorService = vendorService;
        this.vendorMapper = vendorMapper;
        this.cartItemRepository = cartItemRepository;
        this.articleService = articleService;
        this.userService = userService;
    }
    @Transactional
    @Override
    public MessageResponse createVendorSale(Integer cartId, Integer clientId, VendorSalesRequest request) {
        // Obtener el carrito, cliente y vendedor
        Cart cart = cartService.getCartBy(cartId);
        if(request.getShippingArea().equals("")){
            throw new EmptyVendorSalesException("La zona de envio no puede estar vacia");
        }
        if(request.getPaymentType().equals("")){
            throw new EmptyVendorSalesException("Tipo de pago no puede estar vacia");
        }

        if (cart == null) {
            throw new CartNotFoundException("El carrito no existe");
        }

        List<CartItem> cartItemList = cart.getCartItems();

        if (cartItemList.isEmpty()) {
            throw new EmptyCartException("No puedes despachar el carrito, está vacío");
        }

        Client client = clientService.getClientBy(clientId);
        Vendor vendor = cart.getVendor();

        // Crear una nueva instancia de VendorSales
        VendorSales vendorSales = new VendorSales();
        vendorSales.setVendor(vendor);
        vendorSales.setClient(client);
        vendorSales.setDate(LocalDate.now());
        vendorSales.setShippingArea(request.getShippingArea());
        vendorSales.setPaymentType(request.getPaymentType());
        vendorSales.setSaleStatus(SaleStatus.PENDIENTE);
        // Suponiendo que cada CartItem tiene un método getTotal() que devuelve el total de ese ítem
        vendorSales.setTotal(
                cartItemList.stream()
                        .mapToDouble(CartItem::getTotal)  // Llama al método getTotal() para obtener el total de cada ítem
                        .sum()  // Suma todos los totales de los ítems
        );

        // Generar el número de factura único para consumidores finales (Factura B)
        String invoiceNumber = "B-" + System.currentTimeMillis();  // "B" para Factura B
        vendorSales.setInvoiceNumber(invoiceNumber);  // Asignar el número de factura


        // Crear y asignar una nueva lista de CartItemVendorSale
        List<CartItemVendorSale> cartItemVendorSales = cartItemList.stream()
                .map(this::convertToCartItemVendorSale)
                .collect(Collectors.toList());
        vendorSales.setCartItemVendorSales(cartItemVendorSales);
        // Descontar la cantidad del inventario de cada artículo
        /*for (CartItem cartItem : cartItemList) {
            Article article = cartItem.getArticle();
            int quantityToSubtract = cartItem.getQuantity();
            if (article.getQuantity() < quantityToSubtract ) {

                throw new InsufficientStockException("No hay suficiente stock para el artículo: " + article.getName() +
                        ", otro vendedor a gastado el stock." );

            }
            // Actualizar el stock
            article.setQuantity(article.getQuantity() - quantityToSubtract);
            // Guardar el artículo actualizado
            articleService.save(article);
        }*/
        // Descontar la cantidad del inventario de cada artículo
        Iterator<CartItem> iterator = cartItemList.iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            Article article = cartItem.getArticle();
            int quantityToSubtract = cartItem.getQuantity();

            if (article.getQuantity() < quantityToSubtract) {
                // Eliminar el CartItem si no hay suficiente stock
                iterator.remove();
                cartItemRepository.delete(cartItem);
                throw new InsufficientStockException("No hay suficiente stock para el artículo: " + article.getName() +
                        ", otro vendedor ha agotado el stock.");
            }

            // Actualizar el stock
            article.setQuantity(article.getQuantity() - quantityToSubtract);
            // Guardar el artículo actualizado
            articleService.save(article);
        }


        // Asociar la venta con el vendedor y guardar
        vendor.addVendorSales(vendorSales);
        vendorSaleRepository.save(vendorSales);

        // Limpiar el carrito después de guardar la venta
        cartItemList.clear();

        return MessageResponse.builder()
                .message("Venta guardada en despacho")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    private CartItemVendorSale convertToCartItemVendorSale(CartItem item) {
        CartItemVendorSale newItem = new CartItemVendorSale();
        newItem.setArticle(item.getArticle());
        newItem.setQuantity(item.getQuantity());
        newItem.setTotal(item.getTotal());
        return newItem;
    }


    @Override
    public List<VendorSaleResponse> getVendorSale(Integer vendorId) {
        Vendor vendor = vendorService.findById(vendorId);
        User user = userService.getUserByUserId(vendor.getUserId());
        // Ordenar las ventas por fecha descendente
        List<VendorSales> sortedSales = vendor.getVendorSales().stream()
                .sorted(Comparator.comparing(VendorSales::getDate).reversed())  // Comparar por fecha en orden descendente
                .collect(Collectors.toList());

        // Mapear a VendorSaleResponse
        List<VendorSaleResponse> response = vendorMapper.mapToVendorSaleList(sortedSales, user.getCompanyName());

        return response;
    }

    @Override
    public MessageResponse updateProductStatus(Integer id) {
        VendorSales sale = vendorSaleRepository.findById(id).orElseThrow(() -> {
            throw new VendorSalesNotFountException("VndorSales no encontrada con id " + id);
        });
        sale.setSaleStatus(SaleStatus.DESPACHADO);
        vendorSaleRepository.save(sale);
        return new MessageResponse("Venta actualizada", HttpStatus.OK);
    }

    @Override
    public Page<VendorSaleResponse> getVendorSalePage(Integer vendorId, int page, int size) {
        Vendor vendor = vendorService.findById(vendorId);
        User user = userService.getUserByUserId(vendor.getUserId());

        // Paginar las ventas ordenadas por fecha descendente
        List<VendorSales> sortedSales = vendor.getVendorSales().stream()
                .sorted(Comparator.comparing(VendorSales::getDate).reversed())  // Comparar por fecha en orden descendente
                .collect(Collectors.toList());

        // Mapear las ventas a VendorSaleResponse
        List<VendorSaleResponse> response = vendorMapper.mapToVendorSaleList(sortedSales, user.getCompanyName());

        // Paginar la lista de respuestas manualmente
        int start = Math.min(page * size, response.size());
        int end = Math.min((page + 1) * size, response.size());

        // Crear la paginación manual
        Page<VendorSaleResponse> salesPage = new PageImpl<>(response.subList(start, end), PageRequest.of(page, size), response.size());

        return salesPage;
    }

    @Override
    public Page<VendorSaleResponse> getFilteredVendorSalePage(Integer vendorId, int page, int size, String search, Integer dateFilter) {
        Vendor vendor = vendorService.findById(vendorId);
        User user = userService.getUserByUserId(vendor.getUserId());

        // Convertir el parámetro de búsqueda a minúsculas para comparaciones insensibles a mayúsculas
        String searchLower = (search != null) ? search.toLowerCase() : "";

        // Filtrar las ventas con búsqueda parcial
        Stream<VendorSales> salesStream = vendor.getVendorSales().stream();

        // Aplicar el filtro solo si se proporciona el parámetro 'search'
        if (!searchLower.isEmpty()) {
            salesStream = salesStream.filter(sale ->
                    // Buscar coincidencias en los primeros tres caracteres del nombre del cliente
                    (sale.getClient().getName() != null && sale.getClient().getName().toLowerCase().startsWith(searchLower)) ||
                            // Buscar coincidencias en los primeros tres caracteres del DNI
                            (sale.getClient().getDni() != null && sale.getClient().getDni().toLowerCase().startsWith(searchLower)) ||
                            // Buscar coincidencias en la fecha (formato de fecha como string)
                            (sale.getDate() != null && sale.getDate().toString().startsWith(searchLower)) ||
                            // Filtrar por el nombre del producto dentro de cartItemVendorSales
                            sale.getCartItemVendorSales().stream().anyMatch(cartItem ->
                                    cartItem.getArticle().getName() != null && cartItem.getArticle().getName().toLowerCase().startsWith(searchLower))
            );
        }

        // Filtro por fecha (dateFilter corresponde al valor seleccionado en el frontend)
        LocalDate now = LocalDate.now();
        switch (dateFilter) {
            case 1: // Este mes
                salesStream = salesStream.filter(sale ->
                        sale.getDate().getMonth().equals(now.getMonth()) && sale.getDate().getYear() == now.getYear());
                break;
            case 2: // Mes pasado
                salesStream = salesStream.filter(sale ->
                        sale.getDate().getMonth().equals(now.minusMonths(1).getMonth()) && sale.getDate().getYear() == now.getYear());
                break;
            case 3: // Este año
                salesStream = salesStream.filter(sale -> sale.getDate().getYear() == now.getYear());
                break;
            case 4: // 2023
                salesStream = salesStream.filter(sale -> sale.getDate().getYear() == 2023);
                break;
            case 5: // 2022
                salesStream = salesStream.filter(sale -> sale.getDate().getYear() == 2022);
                break;
            default:
                // No aplicar filtro de fechas, "Todas"
                break;
        }

        // Ordenar por fecha de venta descendente
        List<VendorSales> filteredSales = salesStream
                .sorted(Comparator.comparing(VendorSales::getDate).reversed())
                .collect(Collectors.toList());

        // Mapear las ventas a VendorSaleResponse
        List<VendorSaleResponse> response = vendorMapper.mapToVendorSaleList(filteredSales, user.getCompanyName());

        // Paginar la lista de respuestas manualmente
        int start = Math.min(page * size, response.size());
        int end = Math.min((page + 1) * size, response.size());

        // Crear la paginación manual
        Page<VendorSaleResponse> salesPage = new PageImpl<>(response.subList(start, end), PageRequest.of(page, size), response.size());

        return salesPage;
    }
    //NUevo
    @Override
    public Page<VendorSaleResponse> getVendorsSalesPageFilter(Integer vendorId, int page, int size, String search) {
        Vendor vendor = vendorService.findById(vendorId);
        User user = userService.getUserByUserId(vendor.getUserId());
        PageRequest pageRequest = PageRequest.of(page, size);  // Crear la paginación con número de página y tamaño

        // Obtener los resultados de ventas paginados, filtrados por el término de búsqueda si está presente
        Page<VendorSales> salesPage = vendorSaleRepository.findByVendorIdAndSearch(vendorId, search, pageRequest);

        return vendorMapper.mapToVendorSalesPage(salesPage, user.getCompanyName());
    }

    @Override
    public Page<VendorSaleResponse> getVendorSalesByFilterDate(Integer vendorId, Integer year, LocalDate startDate, LocalDate endDate, int page, int size) {
        log.info("En servicio getVendorSalesByFilterDate:");
        Vendor vendor = vendorService.findById(vendorId);
        log.info("Vendedor:" + vendor.toString());
        User user = userService.getUserByUserId(vendor.getUserId());
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorSales> sales;

        // Aplicar los filtros
        if (year != null) {
            log.info("Year es :" + year);
            // Filtrar por año
            sales = vendorSaleRepository.findByVendorAndYear(vendor, year, pageable);
        } else if (startDate != null && endDate != null) {
            log.info("StartDate es: " + startDate + " y endDate es: " + endDate);
            // Filtrar por rango de fechas
            sales = vendorSaleRepository.findByVendorAndDateBetween(vendor, startDate, endDate, pageable);
        } else {
            log.info("Todas las ventas");
            // Devolver todas las ventas del vendedor
           sales = vendorSaleRepository.findByVendor(vendor, pageable);
        }
        return vendorMapper.mapToVendorSalesPage(sales, user.getCompanyName());
    }

    @Override
    public List<ClientVendorSaleResponse> getSuggestedVendorSales(Integer vendorId, String search) {
        // Obtener el vendor por ID
        Vendor vendor = vendorService.findById(vendorId);

        // Convertir el parámetro de búsqueda a minúsculas para comparaciones insensibles a mayúsculas
        String searchLower = (search != null) ? search.toLowerCase() : "";

        // Filtrar las ventas basadas en el nombre o DNI del cliente
        List<VendorSales> filteredSales = vendor.getVendorSales().stream()
                .filter(sale ->
                        (sale.getClient().getName() != null && sale.getClient().getName().toLowerCase().contains(searchLower)) ||
                                (sale.getClient().getDni() != null && sale.getClient().getDni().toLowerCase().contains(searchLower)) ||
                                (sale.getInvoiceNumber() != null && sale.getInvoiceNumber().toLowerCase().contains(searchLower))
                ).collect(Collectors.toList());

        // Mapear los clientes de las ventas filtradas a ClientVendorSaleResponse
        List<ClientVendorSaleResponse> clientResponses = filteredSales.stream()
                .map(sale -> {
                    Client client = sale.getClient();
                    ClientVendorSaleResponse response = new ClientVendorSaleResponse();
                    response.setId(client.getId());
                    response.setName(client.getName());
                    response.setAddress(client.getAddress());
                    response.setPhone(client.getPhone());
                    response.setCp(client.getCp());
                    response.setEmail(client.getEmail());
                    response.setBetweenStreets(client.getBetweenStreets());
                    response.setLocation(client.getLocation());
                    response.setDni(client.getDni());
                    response.setVendorId(vendorId);
                    response.setProvince(client.getProvince());
                    return response;
                }).collect(Collectors.toList());

        return clientResponses;
    }

    @Override
    public Map<String, Long> getSalesCountByVendorId(Integer vendorId) {
       /* if(!vendorSaleRepository.existsByVendorId(vendorId)){
            throw new VendorSalesNotFountException("Venta no encontrada con vendorId: " + vendorId);
        }*/
        List<Object[]> results = vendorSaleRepository.countSalesByYearAndVendorId(vendorId);

        Map<String, Long> salesCountByYear = new HashMap<>();
        for (Object[] result : results) {
            String year = String.valueOf(result[0]); // Asume que result[0] es el año
            Long count = (Long) result[1]; // Asume que result[1] es el conteo de ventas
            salesCountByYear.put(year, count);
        }
        log.info("Datos del back en servicio getSalesCountByVendorId = " + salesCountByYear);
        return salesCountByYear;
    }

    @Override
    public Map<String, Long> getSalesCountByShippingArea(Integer vendorId) {
       /* if(!vendorSaleRepository.existsByVendorId(vendorId)){
            throw new VendorSalesNotFountException("Venta no encontrada con vendorId: " + vendorId);
        }*/
        List<Object[]> results = vendorSaleRepository.findSalesCountByShippingArea(vendorId);
        Map<String, Long> salesCountMap = new HashMap<>();

        for (Object[] result : results) {
            String shippingArea = (String) result[0];
            Long count = (Long) result[1];
            salesCountMap.put(shippingArea, count);
        }

        return salesCountMap;
    }

    @Override
    public Long getSalesCount(Integer vendorId) {
        return vendorSaleRepository.countSalesByVendorId(vendorId);
    }

    @Override
    public Map<String, Long> getSalesCountByYearAndUserId(Integer userId) {

        List<Object[]> results = vendorSaleRepository.countSalesByYearAndUserId(userId);

        Map<String, Long> salesCountByYear = new HashMap<>();
        for (Object[] result : results) {
            String year = String.valueOf(result[0]); // El año
            Long count = (Long) result[1]; // El conteo de ventas
            salesCountByYear.put(year, count);
        }

        return salesCountByYear;
    }
    @Override
    public Map<String, Long> getSalesCountByShippingAreaAndUserId(Integer userId) {
        List<Object[]> results = vendorSaleRepository.countSalesByShippingAreaAndUserId(userId);

        Map<String, Long> salesCountByArea = new HashMap<>();
        for (Object[] result : results) {
            String area = (String) result[0]; // Área de envío
            Long count = (Long) result[1]; // Conteo de ventas
            salesCountByArea.put(area, count);
        }

        return salesCountByArea;
    }

    @Override
    public Long getCountSalesByUserId(Integer userId) {
        User user = userService.getUserByUserId(userId);
        if(user == null){
            throw new UserNotFoundException("User no encontrado con id: " + userId);
        }
        return vendorSaleRepository.countSalesByUserId(userId);
    }

    @Override
    public List<PaymentTypeSalesResponse> getSalesCountByPaymentType(Integer userId) {
        User user = userService.getUserByUserId(userId);
        List<Object[]> salesData = vendorSaleRepository.findSalesCountByPaymentType(userId);
        List<PaymentTypeSalesResponse> result = new ArrayList<>();

        // Convertimos el resultado en un DTO más legible
        for (Object[] row : salesData) {
            String paymentType = (String) row[0];
            Long totalSales = (Long) row[1];  // Nota: COUNT() devuelve Long
            result.add(new PaymentTypeSalesResponse(paymentType, totalSales));
        }

        return result;
    }
}
