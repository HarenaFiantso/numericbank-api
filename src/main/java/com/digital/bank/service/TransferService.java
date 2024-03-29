package com.digital.bank.service;

import com.digital.bank.component.TransferComponent;
import com.digital.bank.endpoint.rest.mapper.AccountMapper;
import com.digital.bank.model.*;
import com.digital.bank.model.type.TransactionType;
import com.digital.bank.repository.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransferService {
  private final TransferRepository repository;
  private final TransactionService transactionService;
  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;
  private final TransferGroupRepository transferGroupRepository;
  private final TransactionRepository transactionRepository;
  private final TransactionCategoryRepository transactionCategoryRepository;

  public TransferComponent makeTransfer(TransferComponent toMake) {
    try {
      TransferComponent.TransferComponentBuilder resultBuilder = TransferComponent.builder();

      TransactionCategory transactionCategory =
          this.transactionCategoryRepository.getByName("Transfer");

      Transaction.TransactionBuilder debitTransactionBuilder =
          Transaction.builder()
              .amount(toMake.getAmount())
              .reason(toMake.getReason())
              .transactionType(TransactionType.EXPENSE)
              .transactionDatetime(toMake.getTransferDatetime())
              .idAccount(toMake.getAccountDebit().getIdAccount())
              .idTransactionCategory(transactionCategory.getIdTransactionCategory());

      Transaction.TransactionBuilder creditTransactionBuilder =
          Transaction.builder()
              .amount(toMake.getAmount())
              .reason(toMake.getReason())
              .transactionType(TransactionType.INCOME)
              .transactionDatetime(toMake.getTransferDatetime())
              .idTransactionCategory(transactionCategory.getIdTransactionCategory());

      if(toMake.getAccountCredit() != null)
        creditTransactionBuilder.idAccount(toMake.getAccountCredit().getIdAccount());

      List<Transaction> savedTransactions =
          this.transactionService.createOrUpdateTransactions(
              Stream.of(
                      debitTransactionBuilder.build(),
                      toMake.getAccountCredit() != null ? creditTransactionBuilder.build() : null)
                  .filter(Objects::nonNull)
                  .collect(Collectors.toList()));

      Transaction debitTransaction = savedTransactions.get(0);
      Transaction creditTransaction =
          toMake.getAccountCredit() != null ? savedTransactions.get(1) : null;

      Transfer.TransferBuilder transferBuilder = Transfer.builder();

      resultBuilder.amount(debitTransaction.getAmount());
      resultBuilder.reason(debitTransaction.getReason());

      Account debitAccount = this.accountRepository.getById(debitTransaction.getIdAccount());

      resultBuilder.accountDebit(accountMapper.toComponent(debitAccount));
      transferBuilder.idTransactionDebit(debitTransaction.getIdTransaction());

      if (creditTransaction != null) {
        Account creditAccount = this.accountRepository.getById(creditTransaction.getIdAccount());

        resultBuilder.accountCredit(accountMapper.toComponent(creditAccount));
        transferBuilder.idTransactionCredit(creditTransaction.getIdTransaction());
      }

      TransferGroup savedTransferGroup =
          this.transferGroupRepository.save(
              TransferGroup.builder()
                  .label(debitTransaction.getReason())
                  .registrationDate(debitTransaction.getTransactionDatetime())
                  .effectiveDate(debitTransaction.getTransactionDatetime())
                  .build());

      resultBuilder.transferDatetime(savedTransferGroup.getEffectiveDate());
      transferBuilder.idTransferGroup(savedTransferGroup.getIdTransferGroup());

      Transfer savedTransfer = this.repository.save(transferBuilder.build());

      this.transactionRepository.saveAll(
          savedTransactions.stream()
              .map(
                  transaction ->
                      Transaction.builder()
                          .idTransaction(transaction.getIdTransaction())
                          .amount(transaction.getAmount())
                          .reason(transaction.getReason())
                          .transactionType(transaction.getTransactionType())
                          .transactionDatetime(transaction.getTransactionDatetime())
                          .idTransfer(savedTransfer.getIdTransfer())
                          .idAccount(transaction.getIdAccount())
                          .idTransactionCategory(transaction.getIdTransactionCategory())
                          .build())
              .collect(Collectors.toList()));

      resultBuilder.idTransfer(savedTransfer.getIdTransfer());

      return resultBuilder.build();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
