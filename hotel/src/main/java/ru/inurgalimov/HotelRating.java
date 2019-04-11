package ru.inurgalimov;

import java.io.*;
import java.util.*;

/**
 * Отели [#121887]
 *
 * @author Ilshat Nurgalimov
 * @version 11.04.2019
 */
public class HotelRating {
    private File input;
    private File output;
    private int count;
    private int subCount;
    private String[] ratingHotel;

    public HotelRating(String pathInput, String pathOutput) {
        this.input = new File(pathInput);
        this.output = new File(pathOutput);
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            this.count = Integer.parseInt(reader.readLine());
            this.subCount = count / 5;
            this.ratingHotel = reader.readLine().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HotelRating h = new HotelRating(args[0], args[1]);
        h.start();
    }

    public void start() {
        Map<Integer, Integer> treeMap = new TreeMap<>();
        int temp;
        List<Integer> list = new ArrayList<>();
        for (String str : this.ratingHotel) {
            temp = Integer.parseInt(str);
            treeMap.put(temp, 0);
            list.add(temp);
        }
        int assessment = 1;
        int t = this.subCount;
        for (Integer i : treeMap.keySet()) {
            if (t == 0) {
                t = this.subCount - 1;
                treeMap.put(i, ++assessment);
                continue;
            }
            treeMap.put(i, assessment);
            t--;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.output))) {
            for (Integer i : list) {
                writer.write(treeMap.get(i) + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
