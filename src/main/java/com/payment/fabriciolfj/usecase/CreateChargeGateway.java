package com.payment.fabriciolfj.usecase;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;

public interface CreateChargeGateway {

    /**
     * @return o render informado quando a charge foi criada, ou o render ja gravado quando e um replay da idempotency key.
     */
    ChargeRender process(final Charge charge, final ChargeRender render);
}
