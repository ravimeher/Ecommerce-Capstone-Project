package org.example.paymentservice.util;

import com.razorpay.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class RazorpaySignatureVerifier {

    public static boolean verifyPaymentLinkSignature(String paymentLinkId, String paymentId, String actualSignature, String secret) {
        try {
            // Step 1: Create the payload string (concatenated values)
            String payload = paymentLinkId + '|' + paymentId;

            // Step 2: Generate expected signature using Razorpay's utility
            String expectedSignature = RazorpaySignatureVerifier.calculateHMAC(payload, secret);

            // Step 3: Compare expected with actual signature
            return expectedSignature.equals(actualSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String calculateHMAC(String data, String secret) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes());
        return new String(Base64.getEncoder().encode(hmacBytes));
    }

}
