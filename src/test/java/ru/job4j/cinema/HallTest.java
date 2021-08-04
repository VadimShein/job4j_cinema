package ru.job4j.cinema;

import org.junit.Test;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.store.PsqlStore;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HallTest {
    @Test
    public void storeTest() {
        Map<Integer, List<Ticket>> cinemaHall = PsqlStore.instOf().findBusyTickets();
        System.out.println(cinemaHall.get(1).get(0).getCell());
        assertThat(cinemaHall.size(), is(1));
    }
}
