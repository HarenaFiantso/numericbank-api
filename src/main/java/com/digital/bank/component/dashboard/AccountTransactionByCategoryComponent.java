package com.digital.bank.component.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AccountTransactionByCategoryComponent {
    private String category;
    private Double amount;
}
