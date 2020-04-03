package ru.inurgalimov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.*;

import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.impl.StdSchedulerFactory;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ilshat Nurgalimov
 * @version 06.05.2019
 */

public class SqlRuParser implements Job {
    private String url;
    private String year;
    private static final Config CONFIG = new Config();
    private static final Logger LOG = LogManager.getLogger(SqlRuParser.class.getName());
    private final Map<String, Integer> calendar;

    public SqlRuParser() {
        this.url = "https://www.sql.ru/forum/job/";
        this.year = "" + (Calendar.getInstance().get(Calendar.YEAR) - 1);
        this.calendar = new HashMap<>();
        calendar.put("янв", 1);
        calendar.put("фев", 2);
        calendar.put("мар", 3);
        calendar.put("апр", 4);
        calendar.put("май", 5);
        calendar.put("июн", 6);
        calendar.put("июл", 7);
        calendar.put("авг", 8);
        calendar.put("сен", 9);
        calendar.put("окт", 10);
        calendar.put("ноя", 11);
        calendar.put("дек", 12);
    }

    public static void main(String[] args) {
        CONFIG.init();
        String exp = CONFIG.get("cron.time");
        try {
            JobDetail job = JobBuilder.newJob(SqlRuParser.class).withIdentity("SqlParse").build();
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .withSchedule(cronSchedule(exp))
                    .forJob(job)
                    .build();
            SchedulerFactory schFactory = new StdSchedulerFactory();
            Scheduler scheduler = schFactory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            LOG.error("Quartz exception.", e);
        }
    }

    public void start(String date, VacancyDB vacancyDB) throws Exception {
        boolean check = true;
        int index = 1;
        Document doc = Jsoup.connect(url + index).get();
        String name;
        String text;
        String link;
        String time;

        while (check) {
            for (Element e1 : doc.select("tr")) {
                Elements description = e1.select("td.postslisttopic");
                Elements dateVacancy = e1.select("td[style].altCol");
                if (description.size() != 0) {
                    Elements element = description.select("a");
                    name = element.get(0).text();
                    link = element.get(0).attr("href");
                    if (name.contains("Java") && !(name.contains("JavaScript") || (name.contains("Java Script")))) {
                        Document document = Jsoup.connect(link).get();
                        Elements elements = document.select("td.msgBody");
                        text = elements.get(1).text();
                        String dateElement = dateVacancy.text();
                        if (!dateElement.startsWith("сегодня") && !dateElement.startsWith("вчера")) {
                            String[] temp = dateElement.split(" ");
                            time = String.format("%s-%s-%s %s:00",
                                    20 + temp[2].substring(0, 2), this.calendar.get(temp[1]), temp[0], temp[3]);
                        } else {
                            Calendar today = Calendar.getInstance();
                            int day = today.get(Calendar.DATE);
                            int month = today.get(Calendar.MONTH) + 1;
                            int y = today.get(Calendar.YEAR);
                            String[] temp = dateElement.split(" ");
                            if (dateElement.startsWith("вчера")) {
                                day--;
                            }
                            time = String.format("%s-%s-%s %s:00", y, month, day, temp[1]);
                        }

                        System.out.println(name);
                        System.out.println(link);
                        System.out.println(text);
                        System.out.println(time);

                        if (time.equals(date)) {
                            check = false;
                        } else if (time.startsWith(date)) {
                            check = false;
                        }
                        if (check) {
                            vacancyDB.add(new Vacancy(name, text, link, time));
                        }
                    }
                }
            }
            index++;
            doc = Jsoup.connect(url + index).get();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Start:");
        SqlRuParser sqlRuParser = new SqlRuParser();
        try (VacancyDB vacancyDB = new VacancyDB(CONFIG)) {
            vacancyDB.init();
            vacancyDB.initTable();
            if (vacancyDB.isEmpty()) {
                sqlRuParser.start(sqlRuParser.year, vacancyDB);
            } else {
                sqlRuParser.start(vacancyDB.getLastDate(), vacancyDB);
            }
        } catch (SQLException sql) {
            LOG.error("JDBC exception.", sql);
        } catch (Exception e) {
            LOG.error("Error when running the program.", e);
        }
        System.out.println("End.");
    }
}
