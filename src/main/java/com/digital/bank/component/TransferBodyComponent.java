package com.digital.bank.component;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class TransferBodyComponent {
    private final Double amount;
    private final Instant transferDatetime;
    private final String reason;
    private final String idDebitAccount;
    private final String idCreditAccount;
}
