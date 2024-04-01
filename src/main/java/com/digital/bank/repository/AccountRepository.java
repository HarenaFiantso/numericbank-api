package com.digital.bank.repository;

import com.digital.bank.component.AccountStatementComponent;
import com.digital.bank.component.dashboard.AccountTotalIncomeExpenseComponent;
import com.digital.bank.component.dashboard.AccountTransactionByCategoryComponent;
import com.digital.bank.model.Account;
import com.digital.bank.util.drr.utility.DreamReflectRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository extends DreamReflectRepository<Account> {
  private final Connection connection;

  public AccountRepository(Connection connection, Connection connection1) {
    super(connection);
    this.connection = connection1;
  }

  public List<AccountStatementComponent> generateAccountStatement(
      String id, LocalDate startDate, LocalDate endDate) throws SQLException {
    List<AccountStatementComponent> results = new ArrayList<>();

    CallableStatement statement =
        this.connection.prepareCall("{call generate_account_statement(?, ?, ?)}");
    statement.setString(1, id);
    statement.setDate(2, Date.valueOf(startDate));
    statement.setDate(3, Date.valueOf(endDate));
    ResultSet resultSet = statement.executeQuery();

    while (resultSet.next()) {
      results.add(
          AccountStatementComponent.builder()
              .transactionDate(resultSet.getDate("transaction_date").toLocalDate())
              .reference(resultSet.getString("reference"))
              .reason(resultSet.getString("reason"))
              .debit(resultSet.getDouble("debit"))
              .credit(resultSet.getDouble("credit"))
              .balance(resultSet.getDouble("balance"))
              .build());
    }

    return results;
  }

  public List<AccountTransactionByCategoryComponent> getTransactionsByCategoryAndAccountId(
          String accountId, LocalDate startDate, LocalDate endDate) throws SQLException {
    List<AccountTransactionByCategoryComponent> results = new ArrayList<>();

    CallableStatement statement =
            this.connection.prepareCall("{call get_transactions_by_category_and_account_id(?, ?, ?)}");
    statement.setString(1, accountId);
    statement.setDate(2, Date.valueOf(startDate));
    statement.setDate(3, Date.valueOf(endDate));
    ResultSet resultSet = statement.executeQuery();

    while (resultSet.next()) {
      results.add(
              new AccountTransactionByCategoryComponent(
                      resultSet.getString("category"),
                      resultSet.getDouble("amount")
              ));
    }

    return results;
  }

  public List<AccountTotalIncomeExpenseComponent> getIncomeExpenseTotalsByAccount(
          String accountId, LocalDate startDate, LocalDate endDate, boolean groupByDay) throws SQLException {
    List<AccountTotalIncomeExpenseComponent> results = new ArrayList<>();

    CallableStatement statement =
            this.connection.prepareCall("{call get_income_expense_totals_by_account(?, ?, ?, ?)}");
    statement.setString(1, accountId);
    statement.setDate(2, Date.valueOf(startDate));
    statement.setDate(3, Date.valueOf(endDate));
    statement.setBoolean(4, groupByDay);
    ResultSet resultSet = statement.executeQuery();

    while (resultSet.next()) {
      results.add(
              new AccountTotalIncomeExpenseComponent(
                      resultSet.getDate("transaction_date").toLocalDate(),
                      resultSet.getDouble("income"),
                      resultSet.getDouble("expense")
              ));
    }

    return results;
  }

  @Override
  protected Account mapResultSet(ResultSet resultSet) {
    try {
      return Account.builder()
          .idAccount(resultSet.getString("id_account"))
          .firstName(resultSet.getString("first_name"))
          .lastName(resultSet.getString("last_name"))
          .birthDate(resultSet.getDate("birth_date"))
          .monthlySalary(resultSet.getDouble("monthly_salary"))
          .overDrafted(resultSet.getBoolean("over_drafted"))
          .build();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
