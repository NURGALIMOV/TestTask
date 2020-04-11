package ru.inurgalimov.controllers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.inurgalimov.dao.ItemDAO;
import ru.inurgalimov.logic.AbstractService;
import ru.inurgalimov.logic.ItemService;
import ru.inurgalimov.models.Item;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Сервлет для отработки запросов по задачам.
 */
public class UpdateServlet extends HttpServlet {

    private final AbstractService<String, Item> service = new ItemService(ItemDAO.getInstance());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handleRequest(resp, "all", null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String description = req.getParameter("description");
        String checked = req.getParameter("checked");
        if ((id == null) || (checked == null)) {
            handleRequest(resp, "add", createItem(description));
        } else {
            handleRequest(resp, "update", createItem(Integer.parseInt(id), "true".equalsIgnoreCase(checked), description));
        }
    }

    @Override
    public void destroy() {
        service.shutDown();
        super.destroy();
    }

    private void handleRequest(HttpServletResponse resp, String key, Item item) throws IOException {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            createJSONArray(service.handleRequest(key, item)).writeJSONString(resp.getWriter());
    }

    /**
     * Создает JSONArray.
     *
     * @param items список задач.
     * @return JSONArray.
     */
    private JSONArray createJSONArray(List<Item> items) {
        JSONArray array = new JSONArray();
        array.addAll(items.stream().map(item -> {
            JSONObject object = new JSONObject();
            object.put("id", item.getId());
            object.put("description", item.getDescription());
            object.put("created", item.getCreated().getTime().toString());
            object.put("done", item.isDone());
            return object;
        }).collect(Collectors.toList()));
        return array;
    }

    /**
     * Создаёт задачу.
     *
     * @param description описание задачи.
     * @return задача.
     */
    private Item createItem(String description) {
        Item item = new Item();
        item.setDescription(description);
        item.setDone(false);
        item.setCreated(Calendar.getInstance());
        return item;
    }

    /**
     * Создаёт задачу.
     *
     * @param id идентификатор задачи.
     * @param done статус выполнения задачи.
     * @param description описание задачи.
     * @return задача.
     */
    private Item createItem(int id, boolean done, String description) {
        Item item = new Item();
        item.setId(id);
        item.setDone(done);
        item.setDescription(description);
        item.setCreated(Calendar.getInstance());
        return item;
    }

}
