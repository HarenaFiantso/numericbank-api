package com.digital.bank.repository;

import com.digital.bank.model.Debt;
import com.digital.bank.util.drr.utility.DreamReflectRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

@Repository
public class DebtRepository extends DreamReflectRepository<Debt> {
  private final Connection connection;

  public DebtRepository(Connection connection) {
    super(connection);
    this.connection = connection;
  }

  public Debt getCurrentDebtOfAccount(String idAccount) throws SQLException {
    String sql = "SELECT * FROM \"debt\" WHERE id_account = ? ORDER BY debt_datetime DESC LIMIT 1";
    PreparedStatement statement = this.connection.prepareStatement(sql);
    statement.setString(1, idAccount);
    ResultSet resultSet = statement.executeQuery();
    if(!resultSet.next())
      return null;
    return this.mapResultSet(resultSet);
  }

  @Override
  protected Debt mapResultSet(ResultSet resultSet) {
    try {
      return Debt.builder()
          .idDebt(resultSet.getString("id_debt"))
          .amount(resultSet.getDouble("amount"))
          .debtDatetime(resultSet.getTimestamp("debt_datetime").toInstant())
          .idAccount(resultSet.getString("id_account"))
          .idInterestRate(resultSet.getString("id_interest_rate"))
          .build();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
