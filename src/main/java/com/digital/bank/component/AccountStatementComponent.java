package com.digital.bank.component;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AccountStatementComponent {
    private final LocalDate transactionDate;
    private final String reference;
    private final String reason;
    private final Double credit;
    private final Double debit;
    private final Double balance;
}
