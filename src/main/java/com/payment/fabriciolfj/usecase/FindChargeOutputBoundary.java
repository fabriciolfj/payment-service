package com.payment.fabriciolfj.usecase;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;

public interface FindChargeOutputBoundary {

    ChargeRender process(final Charge charge);
}
