package com.digital.bank.endpoint.rest.mapper;

import com.digital.bank.component.AccountComponent;
import com.digital.bank.model.Account;
import com.digital.bank.model.Balance;
import com.digital.bank.model.Debt;
import com.digital.bank.model.Transaction;
import com.digital.bank.repository.BalanceRepository;
import com.digital.bank.repository.DebtRepository;
import com.digital.bank.repository.TransactionRepository;
import java.sql.SQLException;
import java.util.List;

import com.digital.bank.service.DebtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountMapper {

  private final TransactionRepository transactionRepository;
  private final BalanceRepository balanceRepository;
  private final TransactionMapper transactionMapper;
  private final DebtService debtService;
  private final DebtMapper debtMapper;

  public AccountComponent toComponent(Account account) {

    try {
      List<Transaction> lastTransactions =
          this.transactionRepository.getTransactionsOfAccount(account.getIdAccount(), 5);
      Balance currentBalance =
          this.balanceRepository.getCurrentBalanceOfAccount(account.getIdAccount());
      Debt currentDebt = this.debtService.getCurrentDebt(account.getIdAccount());

      return AccountComponent.builder()
          .idAccount(account.getIdAccount())
          .firstName(account.getFirstName())
          .lastName(account.getLastName())
          .birthDate(account.getBirthDate())
          .monthlySalary(account.getMonthlySalary())
          .overDrafted(account.getOverDrafted())
          .transactions(lastTransactions.stream().map(this.transactionMapper::toComponent).toList())
          .balance(currentBalance)
          .debt(currentDebt != null ? debtMapper.toComponent(currentDebt) : null)
          .build();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
