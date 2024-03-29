package com.digital.bank.endpoint.rest.mapper;

import com.digital.bank.component.TransferComponent;
import com.digital.bank.component.TransferGroupComponent;
import com.digital.bank.model.Transfer;
import com.digital.bank.model.TransferGroup;
import com.digital.bank.repository.TransactionRepository;
import com.digital.bank.repository.TransferRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransferGroupMapper {
  private final TransferRepository transferRepository;
  private final TransferMapper transferMapper;

  public TransferGroupMapper(TransferRepository transferRepository, TransferMapper transferMapper) {
    this.transferRepository = transferRepository;
    this.transferMapper = transferMapper;
  }

  public TransferGroupComponent toComponent(TransferGroup transferGroup) {
    try {
      List<Transfer> transfers =
          this.transferRepository.getByTransferGroupId(transferGroup.getIdTransferGroup());

      return TransferGroupComponent.builder()
          .idTransferGroup(transferGroup.getIdTransferGroup())
          .label(transferGroup.getLabel())
          .effectiveDate(transferGroup.getEffectiveDate())
          .registrationDate(transferGroup.getRegistrationDate())
          .transfers(
              transfers.stream().map(this.transferMapper::toComponent).collect(Collectors.toList()))
          .build();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
