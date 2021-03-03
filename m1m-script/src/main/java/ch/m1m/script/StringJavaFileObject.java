package ch.m1m.script;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

// https://dzone.com/articles/javaparser-java-code-generation
//
// https://github.com/javaparser/javaparser
//
public class StringJavaFileObject extends SimpleJavaFileObject {
    private final String source;
    private final String scriptName;

    public StringJavaFileObject(String name, String source) {
        super(URI.create("string:///" + name.replaceAll("\\.", "/") +
                Kind.SOURCE.extension), Kind.SOURCE);
        this.source = source;
        this.scriptName = name;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors)
            throws IOException {
        return source;
    }

    public String getScriptName() {
        return scriptName;
    }
}
