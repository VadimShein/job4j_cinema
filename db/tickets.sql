CREATE TABLE tickets (
    id SERIAL PRIMARY KEY,
    session_id INT NOT NULL,
    row INT NOT NULL,
    cell INT NOT NULL,
    account_id INT NOT NULL REFERENCES account(id)
);

alter table tickets add constraint ticket_is_busy unique (session_id, row, cell)

