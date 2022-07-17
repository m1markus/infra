package ch.m1m.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.XmlPrinter;
import com.github.javaparser.printer.YamlPrinter;

import java.util.Optional;

public class MyParser {

    private static String PARSE_COND_EXP = "Std_FN = 'Marc' AND Std_N1 = 'Kent' OR Std_O1 >= 100";

    public static void main(String... args) {

        MyParser p = new MyParser();
        p.run();

        System.out.println("### end ###");
    }

    public void run() {
        int sumx = sumX(5);
        System.out.println("x=" + sumx);
    }

    private int sumX(int n) {
        if (n > 0) {
            return n + sumX(n - 1);
        }
        return 0;
    }
}