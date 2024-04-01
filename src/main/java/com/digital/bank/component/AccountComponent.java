package com.digital.bank.component;

import com.digital.bank.model.Balance;
import com.digital.bank.model.Transaction;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Builder
@Getter
@Setter
public class AccountComponent {
    private final String idAccount;
    private final String firstName;
    private final String lastName;
    private final Date birthDate;
    private final Double monthlySalary;
    private final Boolean overDrafted;
    private final List<TransactionComponent> transactions;
    private final Balance balance;
    private final DebtComponent debt;
}
