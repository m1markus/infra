package ch.m1m.infra.office;

import java.io.IOException;

// https://www.javatpoint.com/apache-poi-tutorial
// http://poi.apache.org/
// http://poi.apache.org/casestudies.html

// https://www.tutorialspoint.com/apache_poi_word/apache_poi_word_quick_guide.htm
// http://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/xwpf/usermodel/examples/
// https://www.baeldung.com/java-microsoft-word-with-apache-poi
// https://www.roytuts.com/create-a-word-document-using-apache-poi/

public class OfficeGenarator {

    public static void main(String ...args) throws IOException {

        String fileName = "/tmp/gen_office_letter.docx";

        LetterGenerator letterGenerator = new LetterGenerator();
        letterGenerator.generate(fileName);

        //letterGenerator.genTable10(fileName);

        //letterGenerator.generateTableOnly(fileName);

        System.out.println("Document created in: " + fileName);
        spawnProcess("LibreOffice.app", fileName);

        /*
        ExcelGenerator excelGenerator = new ExcelGenerator();
        excelGenerator.generate();
        */

    }

    public static int spawnProcess(String application, String fileName) {
        int rc = -1;

        // mac osx
        // open -a /Applications/LibreOffice.app
        String osCommandLine = String.format("open -a /Applications/%s %s", application, fileName);
        System.out.println("calling: " + osCommandLine);

        Process process;
        try {
            process = Runtime.getRuntime()
                    .exec(osCommandLine);
            rc = 0;
        } catch (IOException e) {
            System.err.println(e);
        }

        return rc;
    }
}
