package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();
    private static final Logger LOG = LogManager.getLogger(PsqlStore.class.getName());

    public PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties"))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Map<Integer, List<Ticket>> findBusyTickets() {
        ConcurrentHashMap<Integer, List<Ticket>> cinemaHall = new ConcurrentHashMap<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM tickets")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    int session = it.getInt("session_id");
                    int row = it.getInt("row");
                    int cell = it.getInt("cell");
                    int accountId = it.getInt("account_id");
                    Ticket ticket = new Ticket(session, row, cell, accountId);
                    cinemaHall.putIfAbsent(it.getInt("row"), new ArrayList<>());
                    cinemaHall.get(it.getInt("row")).add(ticket);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return cinemaHall;
    }

    @Override
    public boolean buyTicket(Account account, Ticket ticket) {
        int ticketId = 0;
        int accountId;
        Account dbAccount = findAccount(account);
        if (dbAccount == null) {
            accountId = saveAccount(account);
        } else {
            accountId = dbAccount.getId();
        }
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO tickets(session_id, row, cell, account_id) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ticket.getSessionId());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, accountId);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticketId = id.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return ticketId > 0;
    }

    public int saveAccount(Account account) {
        int accountId = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO account(username, email, phone) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    accountId = id.getInt(1);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return accountId;
    }

    public Account findAccount(Account account) {
        Account rsl = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM account ac where ac.email = ?")) {
            ps.setString(1, account.getEmail());
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    int id = it.getInt("id");
                    String username = it.getString("username");
                    String email = it.getString("email");
                    String phone = it.getString("phone");
                    rsl = new Account(id, username, email, phone);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }
}
