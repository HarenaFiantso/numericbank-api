package com.digital.bank.component;

import com.digital.bank.model.Transfer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
@Setter
public class TransferGroupComponent {
  private final String idTransferGroup;
  private final Instant registrationDate;
  private final Instant effectiveDate;
  private final String label;
  private final List<TransferComponent> transfers;
}
