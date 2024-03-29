package com.digital.bank.component;

import com.digital.bank.model.Transaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TransferComponent {
  private final String idTransfer;
  private final String idTransferGroup;
  private final TransactionComponent transactionDebit;
  private final TransactionComponent transactionCredit;
}
