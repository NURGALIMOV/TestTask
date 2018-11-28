package ru.inurgalimov;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.LongPollingBot;

public class TestBot extends TelegramLongPollingBot implements LongPollingBot {
    private Database database;

    public TestBot() {
        database = new Database();
        database.openDBFile("jdbc:sqlite:C:\\java\\users.sqlite");
        database.createTable("CREATE TABLE users(surname  text PRIMARY KEY NOT NULL, "
                + "name text, data text);");
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TestBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        String[] arr = message.split(" ");
        if (arr[0].equals("add")) {
            database.add(arr[1], arr[2], arr[3]);
            sendMsg(update.getMessage().getChatId().toString(), "Пользователь добавлен в список!");
        } else if (arr[0].equals("update")) {
            database.update(arr[1], arr[3]);
            sendMsg(update.getMessage().getChatId().toString(), "Данные по пользователю обновлены!");
        } else if (arr[0].equals("delete")) {
            database.delete(arr[1]);
            sendMsg(update.getMessage().getChatId().toString(), "Пользователь удален из списка!");
        } else if (arr[0].equals("list")) {
            sendMsg(update.getMessage().getChatId().toString(), "Данные");
            for (String str : database.list()) {
                sendMsg(update.getMessage().getChatId().toString(), str);
            }
        } else {
            sendMsg(update.getMessage().getChatId().toString(), "Я вас не понимаю!");
        }
    }

    /**
     * Метод для настройки сообщения и его отправки.
     *
     * @param chatId id чата
     * @param s      Строка, которую необходимот отправить в качестве сообщения.
     */
    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return "@Inurtestbot";
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    @Override
    public String getBotToken() {
        return "785815403:AAFBeVgcNitiG4oxtudgMi_zhizMHIlOYPo";
    }
}
