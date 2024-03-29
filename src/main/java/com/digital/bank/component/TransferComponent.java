package com.digital.bank.component;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TransferComponent {
  private final String idTransfer;
  private final Instant transferDatetime;
  private final Double amount;
  private final String reason;
  private final AccountComponent accountDebit;
  private final AccountComponent accountCredit;
}
