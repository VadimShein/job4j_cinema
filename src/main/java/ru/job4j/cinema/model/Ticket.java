package ru.job4j.cinema.model;

import java.util.Objects;

public class Ticket {
    private int sessionId;
    private int row;
    private int cell;
    private int accountId;


    public Ticket(int sessionId, int row, int cell, int accountId) {
        this.sessionId = sessionId;
        this.row = row;
        this.cell = cell;
        this.accountId = accountId;

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int session) {
        this.sessionId = session;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return row == ticket.row && cell == ticket.cell;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, cell);
    }
}
