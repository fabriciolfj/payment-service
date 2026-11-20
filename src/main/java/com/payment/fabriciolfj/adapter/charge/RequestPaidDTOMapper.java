package com.payment.fabriciolfj.adapter.charge;

import com.payment.fabriciolfj.model.Charge;

public class RequestPaidDTOMapper {

    private RequestPaidDTOMapper() { }

    public static RequestPaidDTO toDTO(final Charge charge, String endpoint) {
        return RequestPaidDTO.builder()
                .endpoint(endpoint)
                .paymentId(charge.getId().toString())
                .value(charge.getMoney().value())
                .build();
    }
}
