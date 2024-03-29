CREATE OR REPLACE FUNCTION generate_account_statement(p_account_id VARCHAR, p_start_date DATE, p_end_date DATE)
    RETURNS TABLE
            (
                transaction_date DATE,
                reference        VARCHAR,
                reason           VARCHAR,
                credit           DOUBLE PRECISION,
                debit            DOUBLE PRECISION,
                balance          DOUBLE PRECISION
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT t.transaction_datetime::DATE                                                           AS transaction_date,
               t.id_transaction                                                                       AS reference,
               t.reason,
               CASE WHEN t.transaction_type = 'INCOME' THEN t.amount::DOUBLE PRECISION ELSE 0.00 END  AS credit,
               CASE WHEN t.transaction_type = 'EXPENSE' THEN t.amount::DOUBLE PRECISION ELSE 0.00 END AS debit,
               COALESCE((SELECT b.amount::DOUBLE PRECISION
                         FROM balance b
                         WHERE b.id_account = t.id_account
                           AND b.balance_datetime > t.transaction_datetime
                         ORDER BY b.balance_datetime ASC
                         LIMIT 1), 0.00)
                                                                                                      AS balance
        FROM transaction t
        WHERE t.id_account = p_account_id
          AND t.transaction_datetime::DATE BETWEEN p_start_date AND p_end_date
        ORDER BY t.transaction_datetime DESC;
END;
$$
    LANGUAGE plpgsql;