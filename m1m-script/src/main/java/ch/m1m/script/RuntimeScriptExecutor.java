package ch.m1m.script;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class RuntimeScriptExecutor {

    private final JavaCompiler compiler;
    private final MemClassLoader classLoader;

    public RuntimeScriptExecutor() {
        compiler = ToolProvider.getSystemJavaCompiler();
        classLoader = new MemClassLoader();
    }

    public void call(StringJavaFileObject script, String method) throws Exception {
        call(script, method, null);
    }

    public void call(StringJavaFileObject script, String method, RuntimeScriptContext ctx) throws Exception {
        Class scriptClass = null;
        try {
            scriptClass = Class.forName(script.getScriptName(), true, classLoader);
        } catch (ClassNotFoundException e) {
            compileStringJavaFileObject(script);
            scriptClass = Class.forName(script.getScriptName(), true, classLoader);
        }

        Object scriptInstance = scriptClass.getDeclaredConstructor().newInstance();

        if (ctx == null) {
            Method scriptMethodExecute = scriptClass.getDeclaredMethod(method);
            scriptMethodExecute.invoke(scriptInstance);

        } else {
            Method scriptMethodExecute = scriptClass.getDeclaredMethod(method, RuntimeScriptContext.class);
            scriptMethodExecute.invoke(scriptInstance, ctx);
        }
    }

    private void compileStringJavaFileObject(StringJavaFileObject script) throws IOException {
        JavaFileManager fileManager = new MemJavaFileManager(compiler, classLoader);
        JavaFileObject javaFile = script;
        Collection<JavaFileObject> units = Collections.singleton(javaFile);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, units);
        boolean success = task.call();
        if (!success) {
            String errMessage = String.format("Failed to compile script: %s", script.getScriptName());
            throw new RuntimeException(errMessage);
        }
        fileManager.close();
    }
}
