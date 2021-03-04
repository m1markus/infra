package ch.m1m.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class RuntimeScriptExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(RuntimeScriptExecutor.class);

    private final JavaCompiler compiler;
    private final MemClassLoader classLoader;

    public RuntimeScriptExecutor() {
        compiler = ToolProvider.getSystemJavaCompiler();
        classLoader = new MemClassLoader();
    }

    public Object call(StringJavaFileObject script, String method) throws Exception {
        return call(script, method, null);
    }

    public Object call(StringJavaFileObject script, String method, RuntimeScriptContext ctx) throws Exception {
        Instant start = Instant.now();
        Object rcObj;
        Class scriptClass = compileOrLoadClass(script);
        Object scriptInstance = scriptClass.getDeclaredConstructor().newInstance();
        String logWithContextCalled = "";

        if (ctx == null) {
            Method scriptMethodExecute = scriptClass.getDeclaredMethod(method);
            rcObj = scriptMethodExecute.invoke(scriptInstance);

        } else {
            logWithContextCalled = "with ctx ";
            Method scriptMethodExecute = scriptClass.getDeclaredMethod(method, RuntimeScriptContext.class);
            rcObj = scriptMethodExecute.invoke(scriptInstance, ctx);
        }

        String timeSpend = Duration.between(start, Instant.now()).toString();
        LOG.info("Called call() {}for script {} in: {}", logWithContextCalled, script.getScriptName(), timeSpend);

        return rcObj;
    }

    public Object call(StringJavaFileObject script, String method, RuntimeScriptContext ctx, Object... vArgValues)
            throws Exception {

        Instant start = Instant.now();
        Object rcObj;
        Class<?> scriptClass = compileOrLoadClass(script);
        Object scriptInstance = scriptClass.getDeclaredConstructor().newInstance();

        // build class and value arrays
        //
        int arrayLen = vArgValues.length + 1;
        Class[] arrParamClass = new Class[arrayLen];
        arrParamClass[0] = RuntimeScriptContext.class;

        Object[] arrParamValues = new Object[arrayLen];
        arrParamValues[0] = ctx;

        for (int si = 0, ti = 1; si < vArgValues.length; si++, ti++) {
            arrParamClass[ti] = vArgValues[si].getClass();
            arrParamValues[ti] = vArgValues[si];
        }

        Method scriptMethodExecute = scriptClass.getDeclaredMethod(method, arrParamClass);
        rcObj = scriptMethodExecute.invoke(scriptInstance, arrParamValues);

        String timeSpend = Duration.between(start, Instant.now()).toString();
        LOG.info("Called call() with args for script {} in: {}", script.getScriptName(), timeSpend);

        return rcObj;
    }

    private Class<?> compileOrLoadClass(StringJavaFileObject script) throws IOException, ClassNotFoundException {
        Class<?> scriptClass = null;
        try {
            scriptClass = Class.forName(script.getScriptName(), true, classLoader);
        } catch (ClassNotFoundException e) {
            compileStringJavaFileObject(script);
            scriptClass = Class.forName(script.getScriptName(), true, classLoader);
        }
        return scriptClass;
    }

    private void compileStringJavaFileObject(StringJavaFileObject script) throws IOException {
        Instant start = Instant.now();
        JavaFileManager fileManager = new MemJavaFileManager(compiler, classLoader);
        Collection<JavaFileObject> units = Collections.singleton(script);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, units);
        boolean success = task.call();
        fileManager.close();
        if (!success) {
            String errMessage = String.format("Failed to compile script: %s", script.getScriptName());
            LOG.warn(errMessage);
            throw new RuntimeException(errMessage);
        } else {
            String timeSpend = Duration.between(start, Instant.now()).toString();
            LOG.info("Compiled script {} in: {}", script.getScriptName(), timeSpend);
        }
    }
}
