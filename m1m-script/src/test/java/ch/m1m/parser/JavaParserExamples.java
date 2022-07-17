package ch.m1m.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.YamlPrinter;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JavaParserExamples {

    @Test
    public void parse_simple_class() {
        CompilationUnit cu = StaticJavaParser.parse("public class C1 {}");
        Optional<ClassOrInterfaceDeclaration> cOrI = cu.getClassByName("C1");

        System.out.println("class name is: " + cOrI.get().getNameAsString());
    }

    @Test
    public void print_ast_from_something() {
        Statement stmt = StaticJavaParser.parseStatement("int a = 10;");

        //XmlPrinter printer = new XmlPrinter(true);
        // json printer class not found
        //JsonPrinter printer = new JsonPrinter(true);
        YamlPrinter printer = new YamlPrinter(true);
        System.out.println(printer.output(stmt));
    }

    @Test
    public void generate_class_with_public_member_field() {
        CompilationUnit cu = new CompilationUnit();
        cu.setPackageDeclaration("ch.m1m.java.generator");
        ClassOrInterfaceDeclaration c1 = cu.addClass("C2").setPublic(true);
        c1.addPublicField(String.class, "firstname");

        System.out.println(cu.toString());
    }
}
