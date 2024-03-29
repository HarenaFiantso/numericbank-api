package com.digital.bank.repository;

import com.digital.bank.model.Transaction;
import com.digital.bank.model.type.TransactionType;
import com.digital.bank.util.drr.utility.DreamReflectRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository extends DreamReflectRepository<Transaction> {

    private final Connection connection;

    public TransactionRepository(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    public List<Transaction> getTransactionsOfAccount(String accountId, Integer limit) throws SQLException{
        List<Transaction> result = new ArrayList<>();
        String sql = "SELECT * FROM \"transaction\" WHERE id_account = ? ORDER BY transaction_datetime DESC LIMIT ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setString(1, accountId);
        statement.setInt(2, limit);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next())
            result.add(this.mapResultSet(resultSet));
        return result;
    }

    public List<Transaction> getTransactionsOfAccount(String accountId) throws SQLException{
        List<Transaction> result = new ArrayList<>();
        String sql = "SELECT * FROM \"transaction\" WHERE id_account = ? ORDER BY transaction_datetime DESC";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setString(1, accountId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next())
            result.add(this.mapResultSet(resultSet));
        return result;
    }

    @Override
    protected Transaction mapResultSet(ResultSet resultSet) {
        try{
            return Transaction.builder()
                    .idTransaction(resultSet.getString("id_transaction"))
                    .amount(resultSet.getDouble("amount"))
                    .transactionType(TransactionType.valueOf(resultSet.getString("transaction_type")))
                    .idAccount(resultSet.getString("id_account"))
                    .transactionDatetime(resultSet.getTimestamp("transaction_datetime").toInstant())
                    .reason(resultSet.getString("reason"))
                    .idTransactionCategory(resultSet.getString("id_transaction_category"))
                    .idTransfer(resultSet.getString("id_transfer"))
                    .build();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
