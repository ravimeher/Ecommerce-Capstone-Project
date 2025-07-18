package org.example.paymentservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentLink;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.example.paymentservice.client.OrderServiceClient;
import org.example.paymentservice.client.UserServiceClient;
import org.example.paymentservice.dtos.OrderRequestDto;
import org.example.paymentservice.dtos.PaymentResponseDto;
import org.example.paymentservice.dtos.PaymentStatus;
import org.example.paymentservice.paymentgateway.PaymentGatewayChooser;
import org.example.paymentservice.paymentgateway.PaymentGatewayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentGatewayChooser paymentGatewayChooser;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private OrderServiceClient orderServiceClient;
    public PaymentResponseDto createPaymentLink(String name, String emailId, Long orderId, Long amount) {
        PaymentGatewayStrategy paymentGatewayStrategy = paymentGatewayChooser.getOptimalPaymentGateway();
        return paymentGatewayStrategy.getPaymentLink(name, emailId, orderId, amount);
    }

    public String handlePaymentCallback(Map<String, String> queryParams) throws Exception {
        try {
            String razorpayPaymentLinkId = queryParams.get("razorpay_payment_link_id");
            String razorpayPaymentId = queryParams.get("razorpay_payment_id");
            String razorpayPaymentLinkStatus = queryParams.get("razorpay_payment_link_status");
            System.out.println(razorpayPaymentId + "  " + razorpayPaymentLinkStatus + "  " + razorpayPaymentLinkId);
        } catch (Exception e) {
            throw new Exception("ERROR");
        }
        return null;
    }

    public void processWebhook(String payload) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(payload);
            String event = root.path("event").asText();

            System.out.println("📩 Webhook Event Received: " + event);

            switch (event) {
                case "payment.captured":
                    handleCapturedEvent(root);
                    break;

                case "payment.failed":
                    handleFailedEvent(root);
                    break;

                default:
                    System.out.println("🔕 Ignoring event: " + event);
            }
        } catch (Exception e) {
            System.out.println("❌ Exception while processing webhook: " + e);
        }
    }

    public void handleCapturedEvent(JsonNode root) {
        try {
            JsonNode payment = root.path("payload").path("payment").path("entity");

            // Try to fetch reference_id from top level
            String internalOrderId = payment.path("reference_id").asText();

            // Fallback: Try from notes if top level is missing or empty
            if (internalOrderId == null || internalOrderId.isEmpty()) {
                internalOrderId = payment.path("notes").path("reference_id").asText();
            }

            if (internalOrderId == null || internalOrderId.isEmpty()) {
                System.out.println("❌ reference_id not found in webhook payload: " + payment.toPrettyString());
                return;
            }

            Long orderId = Long.parseLong(internalOrderId);
            System.out.println("✅ Payment captured for Order ID: " + orderId);

            OrderRequestDto requestDto = new OrderRequestDto();
            requestDto.setOrderId(orderId);
            requestDto.setPaymentStatus(PaymentStatus.SUCCESS);

            orderServiceClient.updateOrder(requestDto);

        } catch (Exception e) {
            System.out.println("Error processing payment.captured event" + e);
        }
    }

    public void handleFailedEvent(JsonNode root) {
        try {
            JsonNode payment = root.path("payload").path("payment").path("entity");
            String internalOrderId = payment.path("reference_id").asText();

            Long orderId = Long.parseLong(internalOrderId);


            System.out.println("✅ Payment captured for Order ID: " + orderId);

            OrderRequestDto requestDto = new OrderRequestDto();
            requestDto.setOrderId(orderId);
            requestDto.setPaymentStatus(PaymentStatus.FAILED);

            orderServiceClient.updateOrder(requestDto);

        } catch (Exception e) {
            System.out.println("Error processing payment.captured event"+ e);
        }
    }

    public void handleStripeWebhook(String sigHeader, String payload) throws SignatureVerificationException {

        String webhookSecret = "whsec_XXXXXXXXXXXXXXXX"; // Your Stripe webhook secret

        // ✅ 1. Verify the event signature
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

        // ✅ 2. Deserialize the event object safely
        Optional<StripeObject> deserializedObject = event.getDataObjectDeserializer().getObject();

        if (deserializedObject.isEmpty()) {
            System.out.println("❌ Failed to deserialize event payload.");
            return;
        }

        StripeObject stripeObject = deserializedObject.get();

        // ✅ 3. Handle success and failure events
        switch (event.getType()) {

            case "payment_link.completed":
                if (stripeObject instanceof PaymentLink) {
                    PaymentLink paymentLink = (PaymentLink) stripeObject;
                    updateOrderFromMetadata(paymentLink.getMetadata(), PaymentStatus.SUCCESS);
                }
                break;

            case "payment_link.cancelled":
            case "payment_link.expired":
                if (stripeObject instanceof PaymentLink) {
                    PaymentLink paymentLink = (PaymentLink) stripeObject;
                    updateOrderFromMetadata(paymentLink.getMetadata(), PaymentStatus.FAILED);
                }
                break;

            default:
                System.out.println("ℹ️ Ignored event type: " + event.getType());
        }
    }

    private void updateOrderFromMetadata(Map<String, String> metadata, PaymentStatus status) {
        String orderIdStr = metadata.get("reference_id");
        if (orderIdStr == null) {
            System.out.println("⚠️ Missing reference_id in metadata.");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdStr);
            OrderRequestDto dto = new OrderRequestDto();
            dto.setOrderId(orderId);
            dto.setPaymentStatus(status); // Either "SUCCESS" or "FAILURE"
            orderServiceClient.updateOrder(dto);

            System.out.println("✅ Order " + orderId + " updated with status: " + status);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid reference_id format: " + orderIdStr);
        }
    }
}
