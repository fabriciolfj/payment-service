package com.payment.fabriciolfj.usecase;

import com.payment.fabriciolfj.model.Charge;
import com.payment.fabriciolfj.model.ChargeRender;
import com.payment.fabriciolfj.model.ChargeProcess;

public interface CreateChargeGateway {

    ChargeProcess process(final Charge charge, final ChargeRender render);
}
