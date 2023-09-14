package de.juliansiebert.potato;

import de.juliansiebert.potato.compiler.PotatoCompiler;
import de.juliansiebert.potato.loader.MultiClassLoader;

import java.io.File;
import java.net.MalformedURLException;

public final class Potato {
    private final MultiClassLoader classLoader;
    private final PotatoCompiler compiler;

    public Potato() {
        this.classLoader = new MultiClassLoader(getClass().getClassLoader());
        this.compiler = new PotatoCompiler();
    }

    /**
     * Loads a Script java file from the sourceFolder.
     * For Example className 'de/example/ExampleScript' is in sourceFolder './scripts'.
     *
     * @param className The target Script class
     * @param sourceFolder The target folder with the path innit.
     */
    public PotatoScript loadScript(String scriptName, String className, File sourceFolder) throws IllegalArgumentException, IllegalStateException {
        if (!sourceFolder.isDirectory()) throw new IllegalArgumentException("Given sourceFolder is not a directory");
        if (className.endsWith(".java")) {
            className = className.substring(0, className.length() - 5);
        }
        className = className.replace(".", "/");
        File sourceFile = new File(sourceFolder, className + ".java");
        if (!sourceFile.isFile()) throw new IllegalArgumentException("Script file is a directory");
        if (!sourceFile.exists()) throw new IllegalArgumentException("Script file doesn't exist");
        compiler.compile(sourceFile.getPath());
        try {
            PotatoScript script = this.classLoader.loadScript(scriptName, className, sourceFolder.toURI().toURL());
            File classFile = new File(sourceFolder, className + ".class");
            if (!script.getName().equals(scriptName)) {
                this.classLoader.unloadScript(scriptName);
                throw new IllegalStateException(scriptName + " doesn't match script " + className);
            }
            if (classFile.exists()) {
                if (!classFile.delete()) throw new RuntimeException("Unable to delete " + classFile.getPath());
            }
            return script;
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Unable to load script", exception);
        }
    }

    /**
     * Loads a Script java file from the sourceFolder.
     * For Example className 'de.example.ExampleScript' is in sourceFolder './scripts'.
     *
     * @param className The target Script class
     * @param sourceFolder The target folder with the path innit.
     */
    public PotatoScript loadScript(String scriptName, String className, String sourceFolder) {
        return loadScript(scriptName, className, new File(sourceFolder));
    }

    public void unloadScript(PotatoScript script) {
        script.kill();
        this.classLoader.unloadScript(script.getName());
    }
}
