package ch.m1m.script;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemClassLoaderTest {

    private static final Logger LOG = LoggerFactory.getLogger(MemClassLoaderTest.class);

    private static final boolean useSingleExecutor = true;
    private static final RuntimeScriptExecutor singleExecutor = new RuntimeScriptExecutor();

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
        RuntimeScriptExecutor runtimeScript = getRuntimeScriptExecutor();
        StringJavaFileObject script = getValidScript_with_no_package_execute_no_args();

        // WHEN
        // THEN
        for (int i = 0; i < 1; i++) {
            runtimeScript.call(script, "execute");
        }
    }

    @Test
    public void givenAValidScriptWithPackageName_whenExecutingWithoutCtx_thenWeGetNoException() throws Exception {
        // GIVEN
        RuntimeScriptExecutor runtimeScript = getRuntimeScriptExecutor();
        StringJavaFileObject script = getValidScript_with_package_execute_no_args();

        // WHEN
        // THEN
        for (int i = 0; i < 1; i++) {
            runtimeScript.call(script, "execute");
        }
    }

    @Test
    public void givenAValidScript_whenExecutingWithCtx_thenWeGetAExitStatusOfSuccess() throws Exception {
        // GIVEN
        RuntimeScriptExecutor runtimeScript = getRuntimeScriptExecutor();
        StringJavaFileObject script = getValidScript_with_no_package_execute_with_context();
        RuntimeScriptContext ctx = new RuntimeScriptContext();

        // WHEN
        for (int i = 0; i < 1; i++) {
            runtimeScript.call(script, "execute", ctx);
        }

        // THEN
        assertEquals(0, ctx.getExitStatus());
    }

    @Test
    public void givenAValidScript_whenExecutingWithArgs_thenWeGetAExitStatusOfSuccess() throws Exception {
        // GIVEN
        RuntimeScriptExecutor runtimeScript = getRuntimeScriptExecutor();
        StringJavaFileObject script = getValidScript_with_different_arguments();
        RuntimeScriptContext ctx = new RuntimeScriptContext();

        // WHEN
        Integer rc = 0;
        for (int i = 0; i < 1; i++) {
            rc = (Integer) runtimeScript.call(script, "execute", ctx, "90", Integer.valueOf(9));
        }

        // THEN
        assertEquals(99, rc);
        assertEquals(99, ctx.getExitStatus());
    }

    @Test
    public void givenAScriptThatCanNotCompile_whenExecuting_thenWeGetARuntimeException() throws Exception {
        // GIVEN
        RuntimeScriptExecutor runtimeScript = getRuntimeScriptExecutor();
        StringJavaFileObject script = getInvalidScript_that_does_not_compile();

        // WHEN
        Throwable error = Assertions.assertThrows(RuntimeException.class, () -> {
            runtimeScript.call(script, "execute");
        });

        // THEN
        assertEquals("Failed to compile script: MyScriptDoesNotCompile", error.getMessage());
    }

    @Test
    public void measure_the_time_for_normal_method_calls() {

        class TestPerformance {
            public Object execute() {
                //System.out.println("nothing special");
                return null;
            }
        }

        for (int i = 0; i < 1; i++) {
            Instant start = Instant.now();

            TestPerformance tp = new TestPerformance();
            tp.execute();

            String timeSpend = Duration.between(start, Instant.now()).toString();
            LOG.info("Called execute() on new TestPerformance instance in: {}", timeSpend);
        }
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

    private StringJavaFileObject getValidScript_with_different_arguments() {
        String MyScriptArgs = String.join("\n",
                "",
                "import ch.m1m.script.RuntimeScriptContext;",
                "public class MyScriptArgs {",
                "    public Integer execute(RuntimeScriptContext ctx, String s1, Integer i1) {",
                "         System.out.println(\"hello v1.0.0 with arguments\");",
                "         int rc = Integer.parseInt(s1) + i1;",
                "         ctx.setExitStatus(rc);",
                "         return Integer.valueOf(rc);",
                "    }",
                "}");
        return new StringJavaFileObject("MyScriptArgs", MyScriptArgs);
    }

    private StringJavaFileObject getInvalidScript_that_does_not_compile() {
        return new StringJavaFileObject("MyScriptDoesNotCompile", "public class does_not_compile");
    }

    private RuntimeScriptExecutor getRuntimeScriptExecutor() {
        if (useSingleExecutor) {
            return singleExecutor;
        }
        return new RuntimeScriptExecutor();
    }
}
