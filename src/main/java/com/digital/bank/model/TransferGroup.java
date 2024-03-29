package com.digital.bank.model;

import com.digital.bank.util.drr.annotation.Column;
import com.digital.bank.util.drr.annotation.Id;
import com.digital.bank.util.drr.annotation.Model;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
@Model(table = "transfer_group")
public class TransferGroup {
  @Id
  @Column(name = "id_transfer_group")
  private final String idTransferGroup;

  @NonNull
  @Column(name = "registration_date")
  private final Instant registrationDate;

  @NonNull
  @Column(name = "effective_date")
  private final Instant effectiveDate;

  @NonNull
  @Column(name = "label")
  private final String label;
}
