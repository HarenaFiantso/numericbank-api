package com.digital.bank.component.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class AccountTotalIncomeExpenseComponent {
    private LocalDate transactionDate;
    private Double income;
    private Double expense;
}
