package ru.inurgalimov.controller;


import org.json.JSONArray;
import org.json.JSONObject;
import ru.inurgalimov.persistence.DBStore;
import ru.inurgalimov.persistence.Place;
import ru.inurgalimov.persistence.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервлет для обработки запросов.
 */
public class HallServlet extends HttpServlet {

    /** Хранилище забронированных мест. */
    private DBStore store = DBStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        List<String> list = store.findAll().stream().filter(place -> place.isBooked())
                .map(place -> String.format("%s%s", place.getRow(), place.getPlaceNum())).collect(Collectors.toList());
        writer.println(list);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int hall = Integer.parseInt(req.getParameter("hall"));
        int row = Integer.parseInt(req.getParameter("row"));
        int place = Integer.parseInt(req.getParameter("place"));
        String[] name = req.getParameter("name").split(" ");
        int phoneNum = Integer.parseInt(req.getParameter("phoneNum"));
        store.add(new Place(hall, row, place, true, new User(phoneNum, name[0], name[1], name[2])));
    }
}
