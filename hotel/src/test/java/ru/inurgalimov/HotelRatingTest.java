package ru.inurgalimov;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class HotelRatingTest {

    @Test
    public void whereCountFive() {
        String temp = this.getClass().getResource("\\").getFile();
        String fileIn = temp.substring(0, temp.indexOf("target")) + "src\\main\\resources\\testFive.txt";
        String fileOut = temp.substring(0, temp.indexOf("target")) + "src\\main\\resources\\testResultFive.txt";
        HotelRating hotelRating = new HotelRating(fileIn, fileOut);
        hotelRating.start();
        String result = "";
        File file = new File(fileOut);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            result = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(result, is("4 2 5 3 1 "));
        file.delete();
    }
    @Test
    public void whereCountTen() {
        String temp = this.getClass().getResource("\\").getFile();
        String fileIn = temp.substring(0, temp.indexOf("target")) + "src\\main\\resources\\testTen.txt";
        String fileOut = temp.substring(0, temp.indexOf("target")) + "src\\main\\resources\\testResultTen.txt";
        HotelRating hotelRating = new HotelRating(fileIn, fileOut);
        hotelRating.start();
        String result = "";
        File file = new File(fileOut);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            result = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(result, is("5 5 1 1 3 3 2 2 4 4 "));
        file.delete();
    }
}