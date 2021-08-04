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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();
    private static final Logger LOG = LogManager.getLogger(PsqlStore.class.getName());

    public PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
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

                    List<Ticket> gotTickets = cinemaHall.computeIfAbsent(row,
                            k -> {
                        List<Ticket> tickets = new ArrayList<>();
                        tickets.add(ticket);
                        return tickets;
                    });
                    if (!gotTickets.isEmpty()) {
                        gotTickets.add(ticket);
                        cinemaHall.put(it.getInt("row"), gotTickets);
                    }
                }
                System.out.println("cinSize" + cinemaHall.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
        }
        return cinemaHall;
    }

    @Override
    public int buyTicket(Account account, Ticket ticket) {
        int accountId = 0;
        int ticketId = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO account(username, email, phone) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
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

        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO tickets(session_id, row, cell, account_id) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
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
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return ticketId;
    }
}
