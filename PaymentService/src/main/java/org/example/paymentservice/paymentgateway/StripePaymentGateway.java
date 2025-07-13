package org.example.paymentservice.paymentgateway;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.example.paymentservice.dtos.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentGateway implements PaymentGatewayStrategy{

    @Value("${stripe.key}")
    private String apiKey;

//    //for subscription
//    @Override
//    public PaymentResponseDto getPaymentLink(String name,String emailId, Long orderId, Long amount ) {
//        try {
//            Stripe.apiKey = this.apiKey;
//            Price price = getPrice(amount);
//
//            PaymentLinkCreateParams params =
//                    PaymentLinkCreateParams.builder()
//                            .addLineItem(
//                                    PaymentLinkCreateParams.LineItem.builder()
//                                            .setPrice(price.getId())
//                                            .setQuantity(1L)
//                                            .build()
//                            ).setAfterCompletion(PaymentLinkCreateParams.AfterCompletion.builder()
//                                    .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
//                                    .setRedirect(PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
//                                            .setUrl("http://localhost:9090/payment/callback")
//                                            .build())
//                                    .build())
//                            .build();
//
//            PaymentLink paymentLink = PaymentLink.create(params);
//            PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
//            paymentResponseDto.setPaymentLink(paymentLink.getUrl());
//
//            return paymentResponseDto;
//        }catch (StripeException exception) {
//            throw new RuntimeException(exception);
//        }
//    }
//
//    private Price getPrice(Long amount) {
//        try {
//            PriceCreateParams params =
//                    PriceCreateParams.builder()
//                            .setCurrency("usd")
//                            .setUnitAmount(amount)
//                            .setRecurring(
//                                    PriceCreateParams.Recurring.builder()
//                                            .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
//                                            .build()
//                            )
//                            .setProductData(
//                                    PriceCreateParams.ProductData.builder().setName("Gold Plan").build()
//                            )
//                            .build();
//
//            Price price = Price.create(params);
//            return price;
//        }catch (StripeException exception) {
//            throw new RuntimeException(exception);
//        }
//    }
@Override
public PaymentResponseDto getPaymentLink(String name, String emailId, Long orderId, Long amount) {
    try {
        Stripe.apiKey = this.apiKey;

        // Create a product inline
        Product product = Product.create(
                ProductCreateParams.builder()
                        .setName("Payment for Order ID: " + orderId)
                        .build()
        );

        // Create a one-time price (Stripe expects amount in **cents/paise**)
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency("inr")
                .setUnitAmount(amount) // Must already be in paise (₹1 = 100)
                .setProduct(product.getId())
                .build();

        Price price = Price.create(priceParams);

        // Create payment link with redirect and metadata
        PaymentLinkCreateParams params = PaymentLinkCreateParams.builder()
                .addLineItem(
                        PaymentLinkCreateParams.LineItem.builder()
                                .setPrice(price.getId())
                                .setQuantity(1L)
                                .build()
                )
                .setAfterCompletion(
                        PaymentLinkCreateParams.AfterCompletion.builder()
                                .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                .setRedirect(
                                        PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                .setUrl("http://localhost:9090/payment/stripe/callback") // callback_url
                                                .build()
                                )
                                .build()
                )
                .putMetadata("reference_id", orderId.toString())       // Like notes.reference_id
                .putMetadata("customer_name", name)                   // Custom metadata
                .putMetadata("customer_email", emailId)
                .build();

        PaymentLink paymentLink = PaymentLink.create(params);

        // Build response
        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setPaymentLink(paymentLink.getUrl());
        responseDto.setCurrency("INR");
        responseDto.setOrderId(orderId);
        responseDto.setAmount(amount); // Already in paise
        responseDto.setStatus(paymentLink.getActive() ? "created" : "inactive");

        return responseDto;
    } catch (StripeException exception) {
        throw new RuntimeException(exception);
    }
}

}
