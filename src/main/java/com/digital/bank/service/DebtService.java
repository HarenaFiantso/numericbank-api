package com.digital.bank.service;

import com.digital.bank.model.Debt;
import com.digital.bank.model.InterestRate;
import com.digital.bank.repository.DebtRepository;
import com.digital.bank.repository.InterestRateRepository;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DebtService {
  private final DebtRepository repository;
  private final InterestRateRepository interestRateRepository;

  public List<Debt> getAllDebts() {
    try {
      return this.repository.findAll();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Debt> createOrUpdateDebts(List<Debt> debts) {
    try {
      return this.repository.saveAll(debts);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Debt getDebtById(String id) {
    try {
      return this.repository.getById(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Debt deleteDebtById(String id) {
    try {
      return this.repository.delete(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Debt getCurrentDebt(String idAccount) {
    try {
      Debt currentDebt = this.repository.getCurrentDebtOfAccount(idAccount);
      if (currentDebt != null) {
        Date currentDate = Date.from(Instant.now());
        Date debtDate = Date.from(currentDebt.getDebtDatetime());
        InterestRate interestRate =
            this.interestRateRepository.getById(currentDebt.getIdInterestRate());
        long dayDifference = calculateDiff(debtDate, currentDate);
        Double addedValue =
            ((currentDebt.getAmount() * interestRate.getValue()) / 100) * dayDifference;
        currentDebt =
            this.repository.save(
                Debt.builder()
                    .amount((currentDebt.getAmount() + addedValue))
                    .debtDatetime(Instant.now())
                    .idInterestRate(currentDebt.getIdInterestRate())
                    .idAccount(currentDebt.getIdAccount())
                    .build());
      }
      return currentDebt;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private long calculateDiff(Date firstDate, Date secondDate) {

    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

    return diff;
  }
}
