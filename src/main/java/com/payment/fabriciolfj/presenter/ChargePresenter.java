package com.payment.fabriciolfj.presenter;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;
import com.payment.fabriciolfj.usecase.ChargeOutputBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class ChargePresenter implements ChargeOutputBoundary {

    private final JsonMapper jsonMapper;

    @Override
    public ChargeRender process(Charge charge) {
        final var response = new ChargeResponse(charge.getId().toString());
        final var body = jsonMapper.writeValueAsString(response);

        return ChargeRender.builder()
                .key(charge.getId())
                .body(body)
                .status(HttpStatus.ACCEPTED.value())
                .build();
    }
}
