package com.payment.fabriciolfj.presenter;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;
import com.payment.fabriciolfj.usecase.FindChargeOutputBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class FindChargePresenter implements FindChargeOutputBoundary {

    private final JsonMapper jsonMapper;

    @Override
    public ChargeRender process(final Charge charge) {
        final var response = new ChargeDetailResponse(
                charge.getId().toString(),
                charge.getMoney().value(),
                charge.getDescribe(),
                charge.getStatus().name(),
                charge.getCreatedOn(),
                charge.getExpired());

        return ChargeRender.builder()
                .key(charge.getId())
                .body(jsonMapper.writeValueAsString(response))
                .status(HttpStatus.OK.value())
                .build();
    }
}
