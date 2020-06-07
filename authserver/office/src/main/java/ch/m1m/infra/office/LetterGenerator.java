package ch.m1m.infra.office;

import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LetterGenerator {

    private void setCellText(XWPFTableCell cell,
                             String text,
                             ParagraphAlignment paragraphAlignment) {
        XWPFParagraph tempParagraph = cell.getParagraphs().get(0);
        tempParagraph.setIndentationLeft(100);
        tempParagraph.setIndentationRight(100);
        tempParagraph.setAlignment(paragraphAlignment);
        XWPFRun tempRun = tempParagraph.createRun();
        tempRun.setFontSize(10);
        //tempRun.setColor("FFFFFF");
        tempRun.setText(text);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
    }

    public void generate(String fileName) throws IOException {

        // XWPF Word 2007 OOXML
        //
        XWPFDocument article = new XWPFDocument();

        XWPFParagraph header = article.createParagraph();
        header.setAlignment(ParagraphAlignment.CENTER);

        // a ..Run can have:
        // tabs, XWPFPicture, Formatting (bold, underline, color, italic)
        //
        XWPFRun headerRun = header.createRun();
        headerRun.setText("This is output 3");
        headerRun.setFontFamily("ARIAL");
        headerRun.setFontSize(18);
        //headerRun.setTextPosition(100);

        XWPFParagraph subHeader = article.createParagraph();
        subHeader.setAlignment(ParagraphAlignment.BOTH);
        // 1140 twips = 1 Inch, 567 twips = 1 cm
        subHeader.setIndentationFirstLine(500);

        XWPFRun subHeaderRun = subHeader.createRun();
        subHeaderRun.setFontFamily("ARIAL");
        subHeaderRun.setFontSize(12);
        subHeaderRun.setText(getBigText1());

        //System.out.println("Color: " + subHeaderRun.getColor());

        // add a Tabel
        //
        XWPFTable table1 = article.createTable(4, 2);
        table1.setWidth("100%");
        table1.setTableAlignment(TableRowAlign.CENTER);
        // set header
        XWPFTableRow tableHeader = table1.getRow(0);
        XWPFTableCell header0 = tableHeader.getCell(0);
        header0.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        //header0.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

        header0.setText("header col-0");
        XWPFTableCell header1 = tableHeader.getCell(1);
        header1.setText("header col-1");

        // add data rows
        //
        XWPFTableRow dataRow = table1.getRow(1);
        XWPFTableCell cell = dataRow.getCell(0);
        setCellText(cell, "Müller AG Beratung 2 kStunden", ParagraphAlignment.LEFT);
        //cell.setText("beratung 2 stunden");

        cell = dataRow.getCell(1);
        //cell.setText("CHF 500.00");
        setCellText(cell, "CHF 500.00", ParagraphAlignment.LEFT);

        //setHeaderRowforSingleCell(cell, "CHF 500.00");

        // add signature
        //
        XWPFParagraph signature = article.createParagraph();
        signature.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun signatureRun = signature.createRun();
        signatureRun.setText("Immo-Quick GmbH Sandra Müller");
        signatureRun.setFontFamily("TIMES NEW ROMAN");
        signatureRun.setFontSize(12);

        FileOutputStream fos = new FileOutputStream(fileName);
        article.write(fos);

        fos.close();
        article.close();
    }

    public void generateTableOnly(String fileName) throws IOException {
        //Blank Document
        XWPFDocument document = new XWPFDocument();

        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(new File(fileName));

        //create table
        XWPFTable table = document.createTable();
        table.setWidth("100%");

        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("col one, row one");
        tableRowOne.addNewTableCell().setText("col two, row one");
        tableRowOne.addNewTableCell().setText("col three, row one");

        //create second row
        XWPFTableRow tableRowTwo = table.createRow();
        tableRowTwo.getCell(0).setText("col one, row two");
        tableRowTwo.getCell(1).setText("col two, row two");
        tableRowTwo.getCell(2).setText("col three, row two");

        //create third row
        XWPFTableRow tableRowThree = table.createRow();
        tableRowThree.getCell(0).setText("col one, row three");
        tableRowThree.getCell(1).setText("col two, row three");
        tableRowThree.getCell(2).setText("col three, row three");

        document.write(out);
        out.close();
        System.out.println("create_table.docx written successully");
    }

    public void genTable10(String fileName) throws IOException {
        XWPFDocument document= new XWPFDocument();

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run=paragraph.createRun();
        run.setText("The table:");

        //create the table
        XWPFTable table = document.createTable(3,3);
        table.setWidth("100%");
        for (int r = 0; r < 3; r++) {
            XWPFTableRow row = table.getRow(r);

            row.setHeight(1440/2); // 1/2inch; 1440Twip = 1440/20 == 72pt = 1inch

            for (int c = 0; c < 3; c++) {
                XWPFTableCell cell = row.getCell(c);
                cell.setText("row " + r + ", col " + c);

                // get first paragraph in cell - this contains the content set above by cell.setText
                XWPFParagraph firstParagraphInCell = cell.getParagraphArray(0);
                // set spacing after to 0 (defaults to 10pt)
                firstParagraphInCell.setSpacingAfter(0);

                if (r == 0) {
                    // default vertical align
                } else if (r == 1) {
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                } else if (r == 2) {
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
                }
            }
        }

        FileOutputStream out = new FileOutputStream(fileName);
        document.write(out);
        out.close();
        document.close();
    }

    private String getBigText1() {
        String text = "ksjdfjas fj slj sdjjsdlfjs d sd sjsd sdjs d sdjfljsdljs kljsdf sdfj sdjf sdks d sljlksj sfj lsdjskjsjksdjs ls kjssjsj fls dkljsd fjsdlkjsdlfj sdlks js lsjfljsdl fsdjflkdjksldjfskf sk sj sdlkjs fkjs lksd k fkldjflirirn s,j sjr jlks jslfjs dsd ljslkfjsljsl sd sdj lsdklflsjflsjlsjhsd fhhf sf sjslsj fljs ljsdl sjdf";
        return text;
    }
}
