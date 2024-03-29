package com.digital.bank.service;

import com.digital.bank.component.TransactionComponent;
import com.digital.bank.endpoint.rest.mapper.TransactionMapper;
import com.digital.bank.model.Account;
import com.digital.bank.model.Balance;
import com.digital.bank.repository.AccountRepository;
import com.digital.bank.repository.BalanceRepository;
import com.digital.bank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {
  private final AccountRepository repository;
  private final BalanceRepository balanceRepository;
  private final TransactionRepository transactionRepository;
  private final TransactionMapper transactionMapper;

  public List<Account> getAllAccounts() {
    try {
      return this.repository.findAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Account> createOrUpdateAccounts(List<Account> accounts) {
    try {
      List<Account> saved = this.repository.saveAll(accounts);

      for (Account account : saved) {
        if (balanceRepository.getCurrentBalanceOfAccount(account.getIdAccount()) == null)
          balanceRepository.save(
              Balance.builder()
                  .amount(0.0)
                  .balanceDatetime(Instant.now())
                  .idAccount(account.getIdAccount())
                  .build());
      }

      return saved;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Account getAccountById(String id) {
    try {
      return this.repository.getById(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Account deleteAccountById(String id) {
    try {
      return this.repository.delete(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<TransactionComponent> getAllTransactionsOfAccount(String idAccount) {
    try {
      return this.transactionRepository.getTransactionsOfAccount(idAccount).stream()
          .map(this.transactionMapper::toComponent)
          .toList();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
