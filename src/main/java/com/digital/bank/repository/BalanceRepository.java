package com.digital.bank.repository;

import com.digital.bank.model.Balance;
import com.digital.bank.util.drr.utility.DreamReflectRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;

@Repository
public class BalanceRepository extends DreamReflectRepository<Balance> {
    private final Connection connection;
    public BalanceRepository(Connection connection) {

        super(connection);
        this.connection = connection;
    }

    public Balance getCurrentBalanceOfAccount(String accountId) throws SQLException{
        String sql = "SELECT * FROM \"balance\" WHERE id_account = ? AND balance_datetime <= ? ORDER BY balance_datetime DESC LIMIT 1";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setString(1, accountId);
        statement.setTimestamp(2, Timestamp.from(Instant.now()));
        ResultSet resultSet = statement.executeQuery();
        if(!resultSet.next())
            return null;
        return this.mapResultSet(resultSet);
    }

    @Override
    protected Balance mapResultSet(ResultSet resultSet) {
        try {
            return Balance.builder()
                    .idBalance(resultSet.getString("id_balance"))
                    .amount(resultSet.getDouble("amount"))
                    .balanceDatetime(resultSet.getTimestamp("balance_datetime").toInstant())
                    .idAccount(resultSet.getString("id_account"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
