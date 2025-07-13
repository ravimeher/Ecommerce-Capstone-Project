package org.example.paymentservice.paymentgateway;

import com.razorpay.PaymentLink;
import org.example.paymentservice.dtos.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Component
public class RazorPayPaymentGateway implements PaymentGatewayStrategy{

    @Autowired
    private RazorpayClient razorpayClient;

    @Override
    public PaymentResponseDto getPaymentLink(String name,String emailId, Long orderId, Long amount) {
        try {
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");
        long expiresBy = System.currentTimeMillis() / 1000 + 16 * 60;
        paymentLinkRequest.put("expire_by", expiresBy);

        paymentLinkRequest.put("reference_id",orderId.toString());
        paymentLinkRequest.put("description","Payment for orderId "+ orderId);

        JSONObject customer = new JSONObject();
        customer.put("name",name);
        customer.put("email",emailId);

        paymentLinkRequest.put("customer",customer);

        JSONObject notify = new JSONObject();
        notify.put("email",true);

        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);

        JSONObject notes = new JSONObject();
        notes.put("reference_id", orderId.toString());

        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","http://localhost:9090/payment/callback");
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);

        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setPaymentLink(payment.get("short_url").toString());
        responseDto.setCurrency(payment.get("currency").toString());
        responseDto.setOrderId(orderId);
        Number amountValue = (Number) payment.get("amount");
        responseDto.setAmount(amountValue.longValue());
        responseDto.setStatus(payment.get("status").toString());

        return responseDto;
        } catch (RazorpayException e) {
             throw new RuntimeException(e);
        }
    }
}
