package com.payment.fabriciolfj.model;

public sealed interface ChargeProcess permits ChargeProcess.ChargeReplay, ChargeProcess.ChargeCreated {

    record ChargeReplay(ChargeRender render) implements ChargeProcess {}

    record ChargeCreated(ChargeRender render) implements ChargeProcess {}
}

