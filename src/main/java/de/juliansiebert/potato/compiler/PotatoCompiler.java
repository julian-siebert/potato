package de.juliansiebert.potato.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.util.Arrays;

public final class PotatoCompiler {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    public void compile(String... paths) throws IllegalArgumentException {
        if (paths == null) throw new IllegalArgumentException("paths cannot be null");
        if (paths.length == 0) throw new IllegalArgumentException("no paths to compile");
        int code = compiler.run(null, null, null, paths);
        if (code == 0) return;
        throw new RuntimeException("unable to compile with code " + code +  " " + Arrays.toString(paths));
    }
}
