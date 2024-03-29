package com.digital.bank.service;

import com.digital.bank.model.TransferGroup;
import com.digital.bank.repository.TransferGroupRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class TransferGroupService {
  public final TransferGroupRepository repository;

  public TransferGroupService(TransferGroupRepository repository) {
    this.repository = repository;
  }

  public List<TransferGroup> getAllTransferGroups() {
    try {
      return this.repository.findAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<TransferGroup> createOrUpdateTransferGroups(List<TransferGroup> transferGroups) {
    try {
      return this.repository.saveAll(transferGroups);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
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
