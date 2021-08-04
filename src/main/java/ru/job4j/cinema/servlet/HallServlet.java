package ru.job4j.cinema.servlet;

import org.json.JSONObject;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


public class HallServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        Map<Integer, List<Ticket>> cinemaHall = PsqlStore.instOf().findBusyTickets();

        int pr = cinemaHall.size();
        System.out.println("HallSize: " + pr);

        JSONObject jsonObj = new JSONObject();
        for (Map.Entry<Integer, List<Ticket>> row : cinemaHall.entrySet()) {
            jsonObj.put(Integer.toString(row.getKey()), row.getValue());
        }
        PrintWriter writer = new PrintWriter(resp.getOutputStream(), true, StandardCharsets.UTF_8);
        writer.println(jsonObj.toString());
    }
}
