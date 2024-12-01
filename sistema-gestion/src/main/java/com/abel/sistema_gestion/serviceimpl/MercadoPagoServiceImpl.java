package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.mercadoPago.SubscriptionRequest;
import com.abel.sistema_gestion.entity.Payment;
import com.abel.sistema_gestion.entity.PaymentLink;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.enums.LinkStatus;
import com.abel.sistema_gestion.enums.UserStatus;
import com.abel.sistema_gestion.repository.PaymentLinkRepository;
import com.abel.sistema_gestion.serviceimpl.service.MercadoPagoService;
import com.abel.sistema_gestion.serviceimpl.service.PaymentService;
import com.abel.sistema_gestion.serviceimpl.service.UserService;
import com.abel.sistema_gestion.serviceimpl.service.VendorService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.resources.preference.PreferenceItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class MercadoPagoServiceImpl implements MercadoPagoService {

    @Value("${mercado.pago.access-token}")
    private String accessToken;
    @Value("${server.url}")
    private String serverUrl;

    private final PaymentService paymentService;

    private final UserService userService;

    private final PaymentLinkRepository paymentLinkRepository;

    private final VendorService vendorService;



    @Transactional
    @Override
    public String createSubscriptionPayment(SubscriptionRequest request) throws MPException, MPApiException {
        User user = userService.getUserByUserId(request.getUserId());
        // Configura el token de acceso
        MercadoPagoConfig.setAccessToken(accessToken);
        log.info("Entrando al método createSubscriptionPayment() en MercadoPagoServiceImpl");

        // Crea un cliente para manejar las preferencias de suscripción
        PreferenceClient client = new PreferenceClient();

        PreferenceItemRequest preference = createPreferenceItemRequest(request);

        // Información del usuario que va a pagar
        PreferencePayerRequest payerRequest = createPreferencePayerRequest(request);

        // URLs de redireccionamiento (cuando el pago se compl  ete, falle, o quede pendiente)
        PreferenceBackUrlsRequest backUrlsRequest = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:4200/main/plan/payments")  // URL de éxito
                .failure("http://localhost:4200/main/plan/payments")  // URL de fallo
                .pending("http://localhost:4200/main/plan/payments")  // URL de pendiente
                .build();

        // Métodos de pago excluidos
        List<PreferencePaymentMethodRequest> excludedPaymentMethods = new ArrayList<>();
        List<PreferencePaymentTypeRequest> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add(PreferencePaymentTypeRequest.builder().id("ticket").build());  // Excluye pagos con tickets

        // Métodos de pago permitidos
        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                .excludedPaymentMethods(excludedPaymentMethods)
                .excludedPaymentTypes(excludedPaymentTypes)
                .installments(3)  // Permitir hasta 3 cuotas
                .build();

        // Construir la preferencia de pago
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(Collections.singletonList(preference)) // Solo un ítem: la suscripción
                .payer(payerRequest)
                .backUrls(backUrlsRequest)
                .purpose("wallet_purchase") // El propósito es "subscription"
                .metadata(Map.of(
                        "user_id", user.getId().toString() // Agrega metadata con el ID del usuario para referencia
                ))
                .marketplace("Control de Ventas ")
                .notificationUrl(serverUrl + "/mercado-pago/notify/payment?source_news=webhooks")  // URL de notificación)
                .binaryMode(true)
                .expires(false)  // No expira
                .paymentMethods(paymentMethods)
                .build();

        // Crear la preferencia
        Preference preferenceResponse = client.create(preferenceRequest);

        // Guardar logs o manejar la respuesta (opcional)
        this.createLogsForPaymentOrder(preferenceResponse);

        // Devolver la URL para redirigir al usuario a pagar
        return preferenceResponse.getInitPoint();
    }

    private PreferenceItemRequest createPreferenceItemRequest(SubscriptionRequest request) {

        return PreferenceItemRequest.builder()
                .currencyId("ARS")
                .title(request.getTitle())
                .description(request.getDescription())
                .quantity(1)
                .unitPrice(request.getPrice())
                .build();
    }

    private PreferencePayerRequest createPreferencePayerRequest(SubscriptionRequest request) {
        User user = userService.getUserByUserId(request.getUserId());
        return PreferencePayerRequest.builder()
                .name(user.getName())
                .email(user.getUsername())
                .build();
    }


    private void createLogsForPaymentOrder(Preference preference) {
        log.info("Info de la preferencia del pago:");
        log.info("ID: {}", preference.getId());
        log.info("Items:");
        for (PreferenceItem item : preference.getItems()) {
            log.info("  - ID: {}", item.getId());
            // Loguear otros campos de PreferenceItem según sea necesario
        }
        log.info("Payer: {}", preference.getPayer().toString());
        log.info("Client ID: " + preference.getClientId());
        log.info("Payment Methods: " + preference.getPaymentMethods().toString());
        log.info("Back URLs: " + preference.getBackUrls());
        log.info("Shipments: " + preference.getShipments());
        log.info("Notification URL: " + preference.getNotificationUrl());
        log.info("Statement Descriptor: " + preference.getStatementDescriptor());
        log.info("External Reference: " + preference.getExternalReference().toString());
        log.info("Expires: " + preference.getExpires());
        log.info("Date of Expiration: " + preference.getDateOfExpiration());
        log.info("Expiration Date From: " + preference.getExpirationDateFrom());
        log.info("Expiration Date To: " + preference.getExpirationDateTo());
        log.info("Collector ID: " + preference.getCollectorId());
        log.info("Marketplace: " + preference.getMarketplace());
        log.info("Marketplace Fee: " + preference.getMarketplaceFee());
        log.info("Additional Info: " + preference.getAdditionalInfo());
        log.info("Auto Return: " + preference.getAutoReturn());
        log.info("Operation Type: " + preference.getOperationType());
        log.info("Differential Pricing: " + preference.getDifferentialPricing());
        log.info("Processing Modes: " + preference.getProcessingModes());
        log.info("Binary Mode: " + preference.getBinaryMode());
        log.info("Taxes: " + preference.getTaxes());
        log.info("Tracks: " + preference.getTracks());
        log.info("Metadata: " + preference.getMetadata());
        log.info("Init Point: " + preference.getInitPoint());
        log.info("Sandbox Init Point: " + preference.getSandboxInitPoint());
        log.info("Date Created: " + preference.getDateCreated());
    }

    private com.mercadopago.resources.payment.Payment getPaymentById(String paymentId) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(accessToken);
        PaymentClient paymentClient = new PaymentClient();
        return paymentClient.get(Long.parseLong(paymentId));
    }

    private boolean isValidNotificationData(Map<String, Object> data) {
        return data.containsKey("type") && data.containsKey("action") && data.containsKey("data");
    }

    private String extractPaymentId(Map<String, Object> data) {
        Object dataObject = data.get("data");
        if (dataObject instanceof Map) {
            Map<String, Object> dataValues = (Map<String, Object>) dataObject;
            return (String) dataValues.get("id");
        }
        return null;
    }

    @Transactional
    @Override
    public boolean processNotificationWebhook(Map<String, Object> data) {
        log.info("Metodo processNotificationWebhook():"  + data.toString());
        try {
            if (isValidNotificationData(data)) {
                String paymentId = extractPaymentId(data);
                log.info("paymentId: " + paymentId);

                if (paymentId != null) {
                    com.mercadopago.resources.payment.Payment payment = getPaymentById(paymentId);
                    this.createLogsProcessNotificationWebhook(payment);

                    if (isPaymentApproved(payment)) {
                        String userEmail = payment.getPayer().getEmail();
                        log.info("UserEmail: " + userEmail);

                        User user = userService.getUserByEmail(userEmail);
                        user.setPremium(true);
                        user.setUserStatus(UserStatus.PREMIUM);

                        vendorService.updateUserStatusVendors(user.getId());
                        user.setPremiumExpirationDate(LocalDate.now().plusMonths(1));
                        userService.save(user);
                        // Extraer el preferenceId del objeto payment
                        String preferenceId = payment.getExternalReference(); // Asegúrate de que este método esté disponible
                        log.info("PreferenceId: " + preferenceId);
                        // Actualizar el estado de pago en PaymentLink
                        log.info("Buscando PaymentLink con referenceId: " + preferenceId);
                        Optional<PaymentLink> linkOpt = paymentLinkRepository.findByPaymentId(preferenceId);
                        if (linkOpt.isPresent()) {
                            PaymentLink link = linkOpt.get();
                            link.setPaid(true);
                            link.setLinkStatus(LinkStatus.PAGADO);
                            paymentLinkRepository.save(link);
                            log.info("El enlace de pago ha sido marcado como usado para el userId: " + user.getId());
                        }

                        this.createPaymentPreference(payment, user.getId());
                        return true; // Pago aprobado y guardado con éxito
                    } else if (isPaymentCancelled(payment)) {
                        log.warn("Pago cancelado");
                    } else if (isPaymentPending(payment)) {
                        log.warn("Pago pendiente");
                    } else if (isPaymentRejected(payment)) {
                        log.warn("Pago rechazado");
                    }
                }
            }
        } catch (MPException | MPApiException e) {
            log.error("Error al procesar el webhook de MercadoPago", e);
        } catch (Exception e) {
            log.error("Error inesperado al procesar el webhook", e);
        }
        return false; // Devuelve false si el procesamiento falló en algún punto
    }

    private void createPaymentPreference(com.mercadopago.resources.payment.Payment mercadoPagoPayment, Integer userId) {
        Payment paymentEntity = new Payment();
        paymentEntity.setPaymentId(mercadoPagoPayment.getId().toString());
        paymentEntity.setAmount(mercadoPagoPayment.getTransactionAmount());
        paymentEntity.setCurrency(mercadoPagoPayment.getCurrencyId());
        paymentEntity.setStatus(mercadoPagoPayment.getStatus());
        paymentEntity.setDateCreated(mercadoPagoPayment.getDateCreated().toInstant().atOffset(ZoneOffset.UTC));
        paymentEntity.setDateApproved(mercadoPagoPayment.getDateApproved() != null
                ? mercadoPagoPayment.getDateApproved().toInstant().atOffset(ZoneOffset.UTC)
                : null);
        paymentEntity.setDateLastUpdated(mercadoPagoPayment.getDateLastUpdated().toInstant().atOffset(ZoneOffset.UTC));
        paymentEntity.setDescription(mercadoPagoPayment.getDescription());
        paymentEntity.setSubscription(true);
        paymentEntity.setUserId(userId);

        try {
            paymentService.save(paymentEntity);
        } catch (Exception e) {
            log.error("Error al guardar el pago en la base de datos", e);
        }
    }

    private boolean isPaymentApproved(com.mercadopago.resources.payment.Payment payment) {
        return "approved".equals(payment.getStatus()) && "accredited".equals(payment.getStatusDetail());
    }

    private boolean isPaymentRejected(com.mercadopago.resources.payment.Payment payment) {
        return "rejected".equals(payment.getStatus());
    }

    private boolean isPaymentCancelled(com.mercadopago.resources.payment.Payment payment) {
        return "cancelled".equals(payment.getStatus());
    }

    private boolean isPaymentPending(com.mercadopago.resources.payment.Payment payment) {
        return "pending".equals(payment.getStatus());
    }

    private void createLogsProcessNotificationWebhook(com.mercadopago.resources.payment.Payment payment) {
        log.info("Metodo processNotificationWebhook():");
        log.info("Información del pago:");
        log.info("ID: " + payment.getId());//este
        log.info("Fecha de creación: " + payment.getDateCreated());//este
        log.info("Fecha de aprobación: " + payment.getDateApproved());//este
        log.info("Última fecha de actualización: " + payment.getDateLastUpdated());
        log.info("Fecha de expiración: " + payment.getDateOfExpiration());
        log.info("Fecha de liberación del dinero: " + payment.getMoneyReleaseDate());
        log.info("Esquema de liberación de dinero: " + payment.getMoneyReleaseSchema());
        log.info("Tipo de operación: " + payment.getOperationType());
        log.info("ID del emisor: " + payment.getIssuerId());
        log.info("ID del método de pago: " + payment.getPaymentMethodId());
        log.info("ID del tipo de pago: " + payment.getPaymentTypeId());
        log.info("Estado del pago: " + payment.getStatus());//este
        log.info("Detalles del estado: " + payment.getStatusDetail());//este
        log.info("ID de la moneda: " + payment.getCurrencyId());//este
        log.info("Descripción: " + payment.getDescription());
        log.info("Modo en vivo: " + payment.isLiveMode());
        log.info("ID del patrocinador: " + payment.getSponsorId());
        log.info("Código de autorización: " + payment.getAuthorizationCode());
        log.info("ID del integrador: " + payment.getIntegratorId());
        log.info("ID de la plataforma: " + payment.getPlatformId());
        log.info("ID de la corporación: " + payment.getCorporationId());
        log.info("ID del colector: " + payment.getCollectorId());
        log.info("Datos del pagador:");
        log.info("Nombre: " + payment.getPayer().getFirstName());
        log.info("Nombre: " + payment.getPayer().getLastName());
        log.info("Correo electrónico: " + payment.getPayer().getEmail());
        log.info("Metadatos: " + payment.getMetadata());
        log.info("Información adicional: " + payment.getAdditionalInfo());
        log.info("Orden asociada: " + payment.getOrder());
        log.info("Referencia externa: " + payment.getExternalReference());
        log.info("Monto de la transacción: " + payment.getTransactionAmount());//este
        log.info("Monto de la transacción devuelto: " + payment.getTransactionAmountRefunded());
        log.info("Monto del cupón: " + payment.getCouponAmount());
        log.info("ID de diferenciación de precios: " + payment.getDifferentialPricingId());
        log.info("Cuotas: " + payment.getInstallments());//este
        log.info("Detalles de la transacción: " + payment.getTransactionDetails());
        log.info("Detalles de la tarifa: " + payment.getFeeDetails());
        log.info("Capturado: " + payment.isCaptured());
        log.info("Modo binario: " + payment.isBinaryMode());
        log.info("ID de llamada para autorización: " + payment.getCallForAuthorizeId());
        log.info("Descriptor de declaración: " + payment.getStatementDescriptor());
        log.info("Tarjeta: " + payment.getCard().toString());//este
        log.info("URL de notificación: " + payment.getNotificationUrl());
        log.info("URL de callback: " + payment.getCallbackUrl());
        log.info("Modo de procesamiento: " + payment.getProcessingMode());
        log.info("ID de la cuenta del comerciante: " + payment.getMerchantAccountId());
        log.info("Número del comerciante: " + payment.getMerchantNumber());
        log.info("Código de cupón: " + payment.getCouponCode());
        log.info("Monto neto: " + payment.getNetAmount());
        log.info("ID de la opción del método de pago: " + payment.getPaymentMethodOptionId());
        log.info("Impuestos: " + payment.getTaxes());
        log.info("Monto de impuestos: " + payment.getTaxesAmount());
        log.info("Moneda contraria: " + payment.getCounterCurrency());
        log.info("Monto de envío: " + payment.getShippingAmount());
        log.info("ID de POS: " + payment.getPosId());
        log.info("ID de tienda: " + payment.getStoreId());
        log.info("Esquema de deducción: " + payment.getDeductionSchema());
        log.info("Reembolsos: " + payment.getRefunds());
        log.info("Punto de interacción: " + payment.getPointOfInteraction());
        log.info("Método de pago: " + payment.getPaymentMethod().toString());//este
        log.info("Información de 3DS: " + payment.getThreeDSInfo());
        log.info("Metadatos internos: " + payment.getInternalMetadata());
    }



    // Método para generar un paymentId único usando userId, fecha y UUID
    private String generateUniquePaymentId(Integer userId) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8); // Usa una parte del UUID para hacerlo más corto
        return "PAY-" + userId + "-" + date + "-" + uuid;
    }

    // Guarda el enlace de pago en la base de datos
    private void savePaymentLink(Integer userId, String paymentLink, String preferenceId) {
        PaymentLink link = new PaymentLink();
        link.setUserId(Long.valueOf(userId));
        link.setPaymentUrl(paymentLink);
        link.setCreatedDate(LocalDateTime.now());
        link.setPaymentId(preferenceId);
        link.setPaid(false);
        link.setLinkStatus(LinkStatus.PENDIENTE);
        paymentLinkRepository.save(link);
    }

    private String createCheckoutProPayment(User user, String uniquePaymentId) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(accessToken);
        PreferenceClient client = new PreferenceClient();
        // Métodos de pago excluidos
        List<PreferencePaymentMethodRequest> excludedPaymentMethods = new ArrayList<>();
        List<PreferencePaymentTypeRequest> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add(PreferencePaymentTypeRequest.builder().id("ticket").build());  // Excluye pagos con tickets

        // Métodos de pago permitidos
        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                .excludedPaymentMethods(excludedPaymentMethods)
                .excludedPaymentTypes(excludedPaymentTypes)
                .installments(3)  // Permitir hasta 3 cuotas
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .externalReference(uniquePaymentId)
                .items(Collections.singletonList(
                        PreferenceItemRequest.builder()
                                .title("Cuota mensual")
                                .description("Plan Premium")
                                .quantity(1)
                                .currencyId("ARS")
                                .unitPrice(BigDecimal.valueOf(1000.0)) // Monto mensual
                                .build()
                ))
                .payer(PreferencePayerRequest.builder()
                        .email(user.getUsername())
                        .name(user.getName())
                        .build()
                )
                .paymentMethods(paymentMethods)
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:4200/main/plan/monthly")
                        .failure("http://localhost:4200/main/plan/monthly")
                        .pending("http://localhost:4200/main/plan/monthly")

                        .build()
                )
                .purpose("wallet_purchase") // El propósito es "subscription"

                .metadata(Map.of(
                        "user_id", user.getId().toString() // Agrega metadata con el ID del usuario para referencia
                ))
                .marketplace("Control de Ventas ")
                .notificationUrl(serverUrl + "/mercado-pago/notify/payment?source_news=webhooks")  // URL de notificación)
                .binaryMode(true)
                .expires(false)  // No expira

                .build();

        Preference preferenceResponse = client.create(preferenceRequest);
        this.createLogsForPaymentOrder(preferenceResponse);
        return preferenceResponse.getInitPoint();
    }

    //@Scheduled(cron = "0 0 1 * * ?") // Ejecuta el primer día de cada mes
   // @Scheduled(cron = "*/10 * * * * ?") // Ejecuta cada 10 segundos
    public MessageResponse checkAndExpirePremiumUsers() {
        List<User> premiumUsers = userService.getPremiumUsers();
        premiumUsers.stream()
                .filter(user -> user.getId() >= 2)
                .forEach(user -> {
            if (hasSubscriptionExpired(user)) {
                // Cambiar el estado a no premium y actualizar en la base de datos
                user.setPremium(false);
                user.setUserStatus(UserStatus.FREE);
                vendorService.updateUserStatusFreeVendors(user.getId());
                user.setPremiumExpirationDate(null); // Opcional: borrar la fecha de expiración
                userService.save(user);
                log.info("Usuario {} ha sido cambiado a no premium.", user.getUsername());
            }
        });
        return MessageResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Metodo chequear usuarios premium ejecutado correctamente")
                .build();
    }

    // Método para verificar si la suscripción ha expirado
    private boolean hasSubscriptionExpired(User user) {
        LocalDate expirationDate = user.getPremiumExpirationDate();
        return expirationDate != null && LocalDate.now().isAfter(expirationDate);
    }

   // @Scheduled(cron = "0 0 1 * * ?") // Ejecuta el primer día de cada mes
    //@Scheduled(cron = "0 * * * * ?") // Ejecuta cada minuto
    //@Scheduled(cron = "*/10 * * * * ?") // Ejecuta cada 10 segundos
    public MessageResponse generateMonthlyPaymentLink() {
        List<User> premiumUsers = userService.getPremiumUsers();
        premiumUsers.stream()
                .filter(user -> user.getId() >= 2 )
                .forEach( user -> {
                    try {
                        // Generar un paymentId único para el usuario
                        String uniquePaymentId = generateUniquePaymentId(user.getId());
                        // Crear la preferencia de pago
                        String paymentLink = createCheckoutProPayment(user, uniquePaymentId);
                        //TODO Agregar notificacion al user q tiene una factura para abonar por email o alguna otra forma
                        // Guardar el enlace de pago y el uniquePaymentId en la base de datos
                        savePaymentLink(user.getId(), paymentLink, uniquePaymentId);
                        log.info("Enlace de pago generado y guardado para usuario: {}", user.getUsername());
                    }catch (Exception e){
                        log.error("Error al generar enlace de pago para usuario: {}", user.getUsername(), e);
                    }
                });

        if(premiumUsers.isEmpty()){
            return MessageResponse.builder().message("No hay usuarios Premium todavia")
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .build();
        }else {
            return MessageResponse.builder()
                    .message("Metodo generar link de pago mensual ejecutado correctamente")
                    .httpStatus(HttpStatus.OK)
                    .build();
        }


    }

}
