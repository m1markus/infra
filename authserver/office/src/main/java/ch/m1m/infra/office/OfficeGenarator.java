package ch.m1m.infra.office;

import java.io.IOException;

// http://poi.apache.org/
// http://poi.apache.org/casestudies.html

public class OfficeGenarator {

    public static void main(String ...args) throws IOException {

        LetterGenerator letterGenerator = new LetterGenerator();
        letterGenerator.generate();

        /*
        ExcelGenerator excelGenerator = new ExcelGenerator();
        excelGenerator.generate();
        */

    }
}
