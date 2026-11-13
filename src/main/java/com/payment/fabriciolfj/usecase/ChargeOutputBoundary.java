package com.payment.fabriciolfj.usecase;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;

public interface ChargeOutputBoundary {

    ChargeRender process(final Charge charge);
}
