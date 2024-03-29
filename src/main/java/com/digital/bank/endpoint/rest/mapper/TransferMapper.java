package com.digital.bank.endpoint.rest.mapper;

import com.digital.bank.component.AccountComponent;
import com.digital.bank.component.TransferComponent;
import com.digital.bank.model.Transfer;
import com.digital.bank.repository.AccountRepository;
import com.digital.bank.repository.TransactionRepository;
import com.digital.bank.repository.TransferGroupRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.Instant;

@Component
public class TransferMapper {
  private final TransactionRepository transactionRepository;
  private final TransferGroupRepository transferGroupRepository;
  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;

  public TransferMapper(TransactionRepository transactionRepository, AccountRepository accountRepository, TransferGroupRepository transferGroupRepository, AccountMapper accountMapper) {
    this.transactionRepository = transactionRepository;
      this.accountRepository = accountRepository;
      this.transferGroupRepository = transferGroupRepository;
      this.accountMapper = accountMapper;
  }

  public TransferComponent toComponent(Transfer transfer) {
    try {
      AccountComponent creditAccount = getCreditAccount(transfer);
      AccountComponent debitAccount = getDebitAccount(transfer);
      Double amount = getAmount(transfer);
      String reason = getReason(transfer);
      Instant transferDatetime = getDatetime(transfer);

      return TransferComponent.builder()
              .idTransfer(transfer.getIdTransfer())
              .transferDatetime(transferDatetime)
              .amount(amount)
              .reason(reason)
              .accountDebit(debitAccount)
              .accountCredit(creditAccount)
              .build();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private AccountComponent getDebitAccount(Transfer transfer) throws SQLException {
    return accountMapper.toComponent(
            accountRepository.getById(
                    transactionRepository.getById(
                            transfer.getIdTransactionDebit()
                    ).getIdAccount()
            )
    );
  }

  private AccountComponent getCreditAccount(Transfer transfer) throws SQLException {
    return transfer.getIdTransactionCredit() != null ? accountMapper.toComponent(
            accountRepository.getById(
                    transactionRepository.getById(
                            transfer.getIdTransactionCredit()
                    ).getIdAccount()
            )
    ) : null;
  }

  private Double getAmount(Transfer transfer) throws SQLException {
    return transactionRepository.getById(transfer.getIdTransactionDebit()).getAmount();
  }

  private String getReason(Transfer transfer) throws SQLException {
    return transactionRepository.getById(transfer.getIdTransactionDebit()).getReason();
  }

  private Instant getDatetime(Transfer transfer) throws SQLException {
    return transferGroupRepository.getById(transfer.getIdTransferGroup()).getEffectiveDate();
  }
}
