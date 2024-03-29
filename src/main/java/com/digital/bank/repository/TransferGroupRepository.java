package com.digital.bank.repository;

import com.digital.bank.model.TransferGroup;
import com.digital.bank.util.drr.utility.DreamReflectRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TransferGroupRepository extends DreamReflectRepository<TransferGroup> {
  public TransferGroupRepository(Connection connection) {
    super(connection);
  }

  @Override
  protected TransferGroup mapResultSet(ResultSet resultSet) {
    try {
      return TransferGroup.builder()
          .idTransferGroup(resultSet.getString("id_transfer_group"))
          .registrationDate(resultSet.getTimestamp("registration_date").toInstant())
          .effectiveDate(resultSet.getTimestamp("effective_date").toInstant())
          .label(resultSet.getString("label"))
          .build();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
