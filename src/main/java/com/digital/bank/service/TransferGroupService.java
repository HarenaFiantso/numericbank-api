package com.digital.bank.service;

import com.digital.bank.component.TransferBodyComponent;
import com.digital.bank.component.TransferComponent;
import com.digital.bank.model.TransferGroup;
import com.digital.bank.repository.TransferGroupRepository;
import com.digital.bank.repository.TransferRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransferGroupService {
  private final TransferGroupRepository repository;
  private final TransferService transferService;

  public TransferGroupService(TransferGroupRepository repository, TransferService transferService) {
    this.repository = repository;
    this.transferService = transferService;
  }

  public List<TransferGroup> getAllTransferGroups() {
    try {
      return this.repository.findAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<TransferComponent> createOrUpdateTransferGroups(List<TransferBodyComponent> transfers) {
    List<TransferComponent> results = new ArrayList<>();
    for (TransferBodyComponent transfer : transfers) {
      TransferComponent madeTransfer = this.transferService.makeTransfer(transfer);
      results.add(madeTransfer);
    }
    return results;
  }

  public TransferGroup getTransferGroupById(String id) {
    try {
      return this.repository.getById(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public TransferGroup deleteTransferGroupById(String id) {
    try {
      return this.repository.delete(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
