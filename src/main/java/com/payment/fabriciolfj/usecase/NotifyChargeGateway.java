package com.payment.fabriciolfj.usecase;

import com.payment.fabriciolfj.model.Charge;

public interface NotifyChargeGateway {

    void process(final Charge charge);
}
