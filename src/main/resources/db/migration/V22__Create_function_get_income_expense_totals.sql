CREATE OR REPLACE FUNCTION get_income_expense_totals_by_account(
    p_account_id VARCHAR(255),
    p_start_date DATE,
    p_end_date DATE,
    p_group_by_day BOOLEAN DEFAULT FALSE)
RETURNS TABLE (
    transaction_date DATE,
    income DOUBLE PRECISION,
    expense DOUBLE PRECISION
) AS $$
BEGIN
RETURN QUERY
SELECT
    CASE
        WHEN p_group_by_day THEN transaction_datetime::DATE
            ELSE DATE_TRUNC('month', transaction_datetime)::DATE
END AS transaction_date,
        SUM(CASE WHEN transaction_type = 'INCOME' THEN amount ELSE 0 END) AS income,
        SUM(CASE WHEN transaction_type = 'EXPENSE' THEN amount ELSE 0 END) AS expense
    FROM
        transaction
    WHERE
        id_account = p_account_id AND
        transaction_datetime BETWEEN p_start_date AND p_end_date
    GROUP BY
        transaction_date
    ORDER BY
        transaction_date;
END; $$
LANGUAGE plpgsql;