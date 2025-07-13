package org.example.paymentservice.controllers;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentLink;
import com.stripe.net.Webhook;
import org.example.paymentservice.client.UserServiceClient;
import org.example.paymentservice.dtos.OrderRequestDto;
import org.example.paymentservice.dtos.PaymentRequestDto;
import org.example.paymentservice.dtos.PaymentResponseDto;
import org.example.paymentservice.dtos.UserDto;
import org.example.paymentservice.exceptions.UserNotAuthenticatedException;
import org.example.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> initiatePayment(@RequestBody PaymentRequestDto requestDto, @RequestHeader("Authorization") String token) {
        UserDto user = userServiceClient.validateToken(token);
        if(user == null){
            throw new UserNotAuthenticatedException("User Token is not valid. Login Again");
        }
        PaymentResponseDto responseDto;
        try {
            responseDto = paymentService.createPaymentLink(user.getName(), user.getEmail(), requestDto.getOrderId(), requestDto.getAmount());
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e.getMessage());
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handlePaymentCallback(
            @RequestParam("razorpay_payment_id") String paymentId,
            @RequestParam("razorpay_payment_link_id") String linkId,
            @RequestParam("razorpay_payment_link_reference_id") String referenceId,
            @RequestParam("razorpay_payment_link_status") String status,
            @RequestParam("razorpay_signature") String signature
    ) {
        try {
            // Prepare parameters for signature verification
//            Map<String, String> attributes = new HashMap<>();
//            attributes.put("razorpay_payment_link_id", linkId);
//            attributes.put("razorpay_payment_id", paymentId);
//            attributes.put("razorpay_signature", signature);

            // Verify signature
//            RazorpayClient razorpay = new RazorpayClient("YOUR_KEY", "YOUR_SECRET");
//            Utils.verifyPaymentLinkSignature(attributes);

            // Update Order Status
            Long orderId = Long.parseLong(referenceId);
            String paymentStatus = status.equalsIgnoreCase("paid") ? "SUCCESS" : "FAILURE";

//            OrderRequestDto dto = new OrderRequestDto();
//            dto.setOrderId(orderId);
//            dto.setPaymentStatus(paymentStatus);
//            userServiceClient.updateOrder(dto);

            return ResponseEntity.ok("Payment Callback Processed Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }



    @PostMapping("/webhook")
    public ResponseEntity<String> handlePaymentCallback(
            @RequestBody String payload
    ) {
        try {
            System.out.println("✅ Webhook received: " + payload);

            // Temporarily skip signature verification
            paymentService.processWebhook(payload);

            return ResponseEntity.ok("Webhook processed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/stripe/callback")
    public ResponseEntity<String> handleStripeRedirectCallback() {
        // No request params are sent by Stripe here
        return ResponseEntity.ok("Thanks for your payment. You'll receive an update shortly.");
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody String payload
    ) {
        try {

            paymentService.handleStripeWebhook(sigHeader,payload);
            // You can handle other events like payment_intent.failed here
            return ResponseEntity.ok("Webhook handled");
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook");
        }
    }

}
