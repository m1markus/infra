package ch.m1m.script;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemClassLoaderTest {

    // http://openbook.rheinwerk-verlag.de/java7/1507_19_002.html
    //
    @Test
    public void verySimpleLowLevelTest() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String src = "public class A { static { System.out.println(\"Java Compiler API 3\"); } }";
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        MemClassLoader classLoader = new MemClassLoader();
        JavaFileManager fileManager = new MemJavaFileManager(compiler, classLoader);
        JavaFileObject javaFile = new StringJavaFileObject("A", src);
        Collection<JavaFileObject> units = Collections.singleton(javaFile);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, units);
        task.call();
        fileManager.close();
        Class.forName("A", true, classLoader).getDeclaredConstructor().newInstance();
    }

    @Test
    public void givenAValidScript_whenExecutingWithoutCtx_thenWeGetNoException() throws Exception {
        // GIVEN
        RuntimeScriptExecutor runtimeScript = new RuntimeScriptExecutor();
        StringJavaFileObject script = getValidScript_with_no_package_execute_no_args();

        // WHEN
        // THEN
        runtimeScript.call(script, "execute");
    }

    @Test
    public void givenAValidScriptWithPackageName_whenExecutingWithoutCtx_thenWeGetNoException() throws Exception {
        // GIVEN
        RuntimeScriptExecutor runtimeScript = new RuntimeScriptExecutor();
        StringJavaFileObject script = getValidScript_with_package_execute_no_args();

        // WHEN
        // THEN
        runtimeScript.call(script, "execute");
    }

    @Test
    public void givenAValidScript_whenExecutingWithCtx_thenWeGetAExitStatusOfSuccess() throws Exception {
        // GIVEN
        RuntimeScriptExecutor runtimeScript = new RuntimeScriptExecutor();
        StringJavaFileObject script = getValidScript_with_no_package_execute_with_context();
        RuntimeScriptContext ctx = new RuntimeScriptContext();

        // WHEN
        runtimeScript.call(script, "execute", ctx);

        // THEN
        assertEquals(0, ctx.getExitStatus());
    }

    @Test
    public void givenAScriptThatCanNotCompile_whenExecuting_thenWeGetARuntimeException() throws Exception {
        // GIVEN
        RuntimeScriptExecutor runtimeScript = new RuntimeScriptExecutor();
        StringJavaFileObject script = getInvalidScript_that_does_not_compile();

        // WHEN
        Throwable error = Assertions.assertThrows(RuntimeException.class, () -> {
            runtimeScript.call(script, "execute");
        });

        // THEN
        assertEquals("Failed to compile script: MyScriptDoesNotCompile", error.getMessage());
    }

    private StringJavaFileObject getValidScript_with_no_package_execute_no_args() {
        String MyScript = String.join("\n",
                "",
                "public class MyScript {",
                "    public void execute() {",
                "         System.out.println(\"hello v1.0.0 no arg\");",
                "    }",
                "}");
        return new StringJavaFileObject("MyScript", MyScript);
    }

    private StringJavaFileObject getValidScript_with_package_execute_no_args() {
        String MyScriptWithPackageName = String.join("\n",
                "",
                "package ch.m1m.test.script;",
                "public class MyScriptWithPackageName {",
                "    public void execute() {",
                "         System.out.println(\"hello v1.0.0 with package name\");",
                "    }",
                "}");
        return new StringJavaFileObject("ch.m1m.test.script.MyScriptWithPackageName", MyScriptWithPackageName);
    }

    private StringJavaFileObject getValidScript_with_no_package_execute_with_context() {
        String MyScriptCtx = String.join("\n",
                "",
                "import ch.m1m.script.RuntimeScriptContext;",
                "public class MyScriptCtx {",
                "    public void execute(RuntimeScriptContext ctx) {",
                "         System.out.println(\"hello v1.0.0 with ctx\");",
                "         ctx.setExitStatus(0);",
                "    }",
                "}");
        return new StringJavaFileObject("MyScriptCtx", MyScriptCtx);
    }

    private StringJavaFileObject getInvalidScript_that_does_not_compile() {
        return new StringJavaFileObject("MyScriptDoesNotCompile", "public class does_not_compile");
    }
}
