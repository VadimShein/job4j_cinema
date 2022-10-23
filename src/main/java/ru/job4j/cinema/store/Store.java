package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Map;

public interface Store {
    Map<Integer, List<Ticket>> findBusyTickets();
    boolean buyTicket(Account account, Ticket ticket);
    int saveAccount(Account account);
    Account findAccount(Account account);
}
