ALTER TABLE "transfer"
    ADD COLUMN id_transaction_debit
        VARCHAR(255) REFERENCES transaction(id_transaction),
    ADD COLUMN id_transaction_credit
        VARCHAR(255) REFERENCES transaction(id_transaction);