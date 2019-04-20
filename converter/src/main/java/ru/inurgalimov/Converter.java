package ru.inurgalimov;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Требования:
 * -	Java 8 без доп. библиотек;
 * -	консольная программа из одного класса (если есть необходимость - можно вложенные);
 * -	параметры запуска программы: имя входного файла (обязательный), имя выходного файла (необязательный),
 * тип разворота (необязательный);
 * -	если параметры не указаны – выводит справку по параметрам;
 * -	программа переворачивает содержимое входного файла в соотв. с указанным типом разворота
 * (побайтно [первый байт файла<>последний байт файла и т.д.] или
 * побитно [первый бит файла<>последний бит файла и т.д.]) и записывает в выходной файл;
 * -	если выходной файл не задан – перезаписывается входной файл;
 * -	максимальное потребление памяти программой – 64 мегабайта;
 * -	максимальный размер обрабатываемого файла – 64 терабайта;
 * -	доп. временные файлы не создавать.
 *
 * @author Ilshat Nurgalimov
 * @version 20.04.2019
 */
public class Converter {
    private Map<String, String> config = new HashMap<>();
    private static final String TYPEBYTE = "byte";
    private static final String TYPEBIT = "bit";
    private static final long MAXSIZE = 70_368_744_177_664L;
    private File fileIn;
    private File fileOut;

    public Converter(String[] args) {
        config.put("-s", null);
        config.put("-d", null);
        config.put("-t", null);

        for (int i = 0; i < args.length; i++) {
            config.put(args[i], args[++i]);
        }
        if (config.get("-d") == null) {
            config.put("-d", config.get("-s"));
        }
        if (config.get("-t") == null || (!TYPEBYTE.equals(config.get("-t")) && !TYPEBIT.equals(config.get("-t")))) {
            config.put("-t", TYPEBYTE);
        } else {
            config.put("-t", config.get("-t").toLowerCase());
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Parameters are not specified!");
            System.out.println("For the program to work, you must specify "
                    + "the following arguments separated by spaces:");
            System.out.println("1) enter -s and space between the name of the input file (required);");
            System.out.println("2) enter -d and space between the name of the output file (optional);");
            System.out.println("3) enter -t and space type reversal (optional), \"byte\" or \"bit\".");
        }
        Converter converter = new Converter(args);
        if (converter.validate()) {
            if (TYPEBYTE.equals(converter.config.get("-t"))) {
                converter.convertByte();
            }
            if (TYPEBIT.equals(converter.config.get("-t"))) {
                converter.convertBit();
            }
        }
    }

    public boolean validate() {
        boolean check = true;
        if (this.config.get("-s") == null) {
            System.out.println("No source file!");
            check = false;
        } else {
            this.fileIn = new File(this.config.get("-s"));
            long size = this.fileIn.length();
            if (size == 0L) {
                System.out.println("The file does not exist!");
                check = false;
            } else if (size > MAXSIZE) {
                System.out.println("File size exceeds maximum size allowed!");
                check = false;
            } else {
                this.fileOut = new File(this.config.get("-d"));
            }
        }

        return check;
    }

    public boolean convertByte() {
        try (FileInputStream in = new FileInputStream(this.fileIn)) {
            int first = in.read();
            int size = in.available();
            while (size != 1) {
                in.skip((long) size - 1);
                size = in.available();
            }
            int last = in.read();
            try (FileOutputStream out = new FileOutputStream(this.fileOut)) {
                out.write(first);
                out.write(last);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return true;
    }

    public boolean convertBit() {
        try (FileInputStream in = new FileInputStream(this.fileIn)) {
            int first = Integer.toBinaryString(in.read()).charAt(6) - '0';
            int size = in.available();
            while (size != 1) {
                in.skip((long) size - 1);
                size = in.available();
            }
            int last = Integer.toBinaryString(in.read()).charAt(0) - '0';

            try (FileOutputStream out = new FileOutputStream(this.fileOut)) {
                out.write(first);
                out.write(last);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return true;
    }
}