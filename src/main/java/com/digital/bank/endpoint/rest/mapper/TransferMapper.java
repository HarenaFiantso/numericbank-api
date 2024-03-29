package com.digital.bank.endpoint.rest.mapper;

import com.digital.bank.component.TransferComponent;
import com.digital.bank.model.Transaction;
import com.digital.bank.model.Transfer;
import com.digital.bank.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class TransferMapper {
  private final TransactionRepository transactionRepository;
  private final TransactionMapper transactionMapper;

  public TransferMapper(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
    this.transactionRepository = transactionRepository;
      this.transactionMapper = transactionMapper;
  }

  public TransferComponent toComponent(Transfer transfer) {
    try {
      Transaction transactionCredit =
          this.transactionRepository.getById(transfer.getIdTransactionCredit());
      Transaction transactionDebit =
          this.transactionRepository.getById(transfer.getIdTransactionDebit());

      return TransferComponent.builder()
          .idTransfer(transfer.getIdTransfer())
          .transactionDebit(transactionMapper.toComponent(transactionDebit))
          .transactionCredit(transactionMapper.toComponent(transactionCredit))
          .idTransferGroup(transfer.getIdTransferGroup())
          .build();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
