package com.digital.bank.repository;

import com.digital.bank.model.Transfer;
import com.digital.bank.util.drr.utility.DreamReflectRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransferRepository extends DreamReflectRepository<Transfer> {
  private final Connection connection;

  public TransferRepository(Connection connection) {

    super(connection);
    this.connection = connection;
  }

  public List<Transfer> getByTransferGroupId(String idTransferGroup) throws SQLException {
    List<Transfer> results = new ArrayList<>();
    String sql = "SELECT * FROM \"transfer\" WHERE id_transfer_group = ?";
    PreparedStatement statement = this.connection.prepareStatement(sql);
    statement.setString(1, idTransferGroup);
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()) results.add(this.mapResultSet(resultSet));
    return results;
  }

  @Override
  protected Transfer mapResultSet(ResultSet resultSet) {
    try {
      return Transfer.builder()
          .idTransfer(resultSet.getString("id_transfer"))
          .idTransferGroup(resultSet.getString("id_transfer_group"))
          .idTransactionCredit(resultSet.getString("id_transaction_credit"))
          .idTransactionDebit(resultSet.getString("id_transaction_debit"))
          .build();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
