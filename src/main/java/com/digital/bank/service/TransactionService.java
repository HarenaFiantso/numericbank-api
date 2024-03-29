package com.digital.bank.service;

import com.digital.bank.model.*;
import com.digital.bank.model.type.TransactionType;
import com.digital.bank.repository.*;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
  private final TransactionRepository repository;
  private final BalanceRepository balanceRepository;
  private final AccountRepository accountRepository;
  private final DebtRepository debtRepository;
  private final InterestRateRepository interestRateRepository;

  public TransactionService(
      TransactionRepository repository,
      BalanceRepository balanceRepository,
      AccountRepository accountRepository,
      DebtRepository debtRepository,
      InterestRateRepository interestRateRepository) {
    this.repository = repository;
    this.balanceRepository = balanceRepository;
    this.accountRepository = accountRepository;
    this.debtRepository = debtRepository;
    this.interestRateRepository = interestRateRepository;
  }

  public List<Transaction> getAllTransactions() {
    try {
      return this.repository.findAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Transaction> createOrUpdateTransactions(List<Transaction> transactions) {
    try {
      List<Transaction> result = new ArrayList<>();
      for (Transaction transaction : transactions) {
        if (transaction.getTransactionType() == TransactionType.EXPENSE) {
          Double currentAccountBalanceAmount =
              this.balanceRepository
                  .getCurrentBalanceOfAccount(transaction.getIdAccount())
                  .getAmount();

          Account currentAccount = this.accountRepository.getById(transaction.getIdAccount());

          if (currentAccountBalanceAmount < transaction.getAmount()) {
            if (currentAccount.getOverDrafted()) {
              Double overDraftedBalance =
                  currentAccountBalanceAmount + currentAccount.getMonthlySalary() / 3;
              this.balanceRepository.save(
                  Balance.builder()
                      .amount(overDraftedBalance)
                      .balanceDatetime(Instant.now())
                      .idAccount(currentAccount.getIdAccount())
                      .build());
              this.giveOverdraftDebtToAccount(currentAccount);

              this.accountRepository.save(
                  Account.builder()
                      .idAccount(currentAccount.getIdAccount())
                      .firstName(currentAccount.getFirstName())
                      .lastName(currentAccount.getLastName())
                      .birthDate(currentAccount.getBirthDate())
                      .monthlySalary(currentAccount.getMonthlySalary())
                      .overDrafted(false)
                      .build());

              if (overDraftedBalance < transaction.getAmount())
                throw new RuntimeException("Your balance is insufficient for this transaction");
            } else {
              throw new RuntimeException("Your balance is insufficient for this transaction");
            }
          }
        }

        Transaction saved = this.applyTransactionToAccount(transaction);
        result.add(saved);
      }
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Transaction getTransactionById(String id) {
    try {
      return this.repository.getById(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Transaction deleteTransactionById(String id) {
    try {
      return this.repository.delete(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Transaction applyTransactionToAccount(Transaction transaction) throws SQLException {
    Transaction saved = this.repository.save(transaction);
    Double currentBalanceAmount =
        this.balanceRepository.getCurrentBalanceOfAccount(transaction.getIdAccount()).getAmount();
    Double updatedBalance =
        transaction.getTransactionType() == TransactionType.INCOME
            ? currentBalanceAmount + transaction.getAmount()
            : currentBalanceAmount - transaction.getAmount();
    Balance savedBalance =
        this.balanceRepository.save(
            Balance.builder()
                .idAccount(transaction.getIdAccount())
                .amount(updatedBalance)
                .balanceDatetime(Instant.now())
                .build());
    return saved;
  }

  private void giveOverdraftDebtToAccount(Account account) throws SQLException {
    InterestRate initialInterestRate = this.interestRateRepository.getInitial();

    this.debtRepository.save(
        Debt.builder()
            .amount(account.getMonthlySalary() / 3)
            .debtDatetime(Instant.now())
            .idAccount(account.getIdAccount())
            .idInterestRate(initialInterestRate.getIdInterestRate())
            .build());
  }
}
