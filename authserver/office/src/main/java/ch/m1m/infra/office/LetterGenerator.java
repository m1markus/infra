package ch.m1m.infra.office;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;

public class LetterGenerator {

    public void generate() throws IOException {

        // XWPF Word 2007 OOXML
        //
        XWPFDocument article = new XWPFDocument();

        XWPFParagraph header = article.createParagraph();
        header.setAlignment(ParagraphAlignment.CENTER);

        // a ..Run can have:
        // tabs, XWPFPicture, Formatting (bold, underline, color, italic)
        //
        XWPFRun headerRun = header.createRun();
        headerRun.setText("This is output 2");
        headerRun.setFontFamily("ARIAL");
        headerRun.setFontSize(18);

        XWPFParagraph subHeader = article.createParagraph();
        subHeader.setAlignment(ParagraphAlignment.BOTH);
        // 1140 twips = 1 Inch, 567 twips = 1 cm
        subHeader.setIndentationFirstLine(500);

        XWPFRun subHeaderRun = subHeader.createRun();
        subHeaderRun.setFontFamily("ARIAL");
        subHeaderRun.setFontSize(12);
        subHeaderRun.setText(getBigText1());

        String fileName = "/tmp/gen_office_letter.docx";
        FileOutputStream fos = new FileOutputStream(fileName);
        article.write(fos);

        System.out.println("Document created in: " + fileName);

        fos.close();
        article.close();
    }

    private String getBigText1() {
        String text = "ksjdfjas fj slj sdjjsdlfjs d sd sjsd sdjs d sdjfljsdljs kljsdf sdfj sdjf sdks d sljlksj sfj lsdjskjsjksdjs ls kjssjsj fls dkljsd fjsdlkjsdlfj sdlks js lsjfljsdl fsdjflkdjksldjfskf sk sj sdlkjs fkjs lksd k fkldjflirirn s,j sjr jlks jslfjs dsd ljslkfjsljsl sd sdj lsdklflsjflsjlsjhsd fhhf sf sjslsj fljs ljsdl sjdf";
        return text;
    }
}
