CREATE OR REPLACE FUNCTION get_transactions_by_category_and_account_id(p_account_id VARCHAR(255), p_start_date DATE, p_end_date DATE)
RETURNS TABLE (
    category VARCHAR(255),
    amount DOUBLE PRECISION
) AS $$
BEGIN
RETURN QUERY
SELECT
    tc.name AS category,
    SUM(t.amount) AS amount
FROM
    transaction t
        LEFT JOIN
    transaction_category tc ON t.id_transaction_category = tc.id_transaction_category
WHERE
        t.id_account = p_account_id AND
    t.transaction_datetime BETWEEN p_start_date AND p_end_date AND
        t.transaction_type IN ('INCOME', 'EXPENSE')
GROUP BY
    tc.name;
END; $$
LANGUAGE plpgsql;