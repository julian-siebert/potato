package de.juliansiebert.potato.loader;

import de.juliansiebert.potato.PotatoScript;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public final class MultiClassLoader {
    private final ClassLoader parent;
    private final Set<PotatoClassLoader> loaders;

    public MultiClassLoader(ClassLoader parent) {
        this.parent = parent;
        this.loaders = new HashSet<>();
    }

    public PotatoScript loadScript(String name, String targetClass, URL url) throws RuntimeException {
        if (name == null) throw new IllegalArgumentException("name parameter cannot be null");
        if (targetClass == null) throw new IllegalArgumentException("target parameter cannot be null");
        if (url == null) throw new IllegalArgumentException("url parameter cannot be null");
        if (targetClass.endsWith(".java")) {
            targetClass = targetClass.substring(0, targetClass.length() - 5);
        }
        targetClass = targetClass.replace("/", ".");
        PotatoScript script;
        try {
            PotatoClassLoader loader = new PotatoClassLoader(name, new URL[] {url}, this.parent);
            this.loaders.add(loader);
            Class<?> theMysteriousClass = Class.forName(targetClass, true, loader);
            Constructor<?> constructor = theMysteriousClass.getConstructor();
            loader.close();
            constructor.setAccessible(true);
            script = (PotatoScript) constructor.newInstance();
            constructor.setAccessible(false);
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load " + targetClass, exception);
        } catch (ClassNotFoundException exception) {
            throw new IllegalArgumentException("Class path " + targetClass + " is not valid for path " + url, exception);
        } catch (NoSuchMethodException exception) {
            throw new IllegalArgumentException("Class " + targetClass + " has a non-zero-parameter constructor. Please remove it.", exception);
        } catch (InvocationTargetException exception) {
            throw new IllegalStateException("Constructor of " + targetClass + " throw an exception", exception);
        } catch (InstantiationException exception) {
            throw new IllegalArgumentException("Class " + targetClass + " is abstract. Please make it at least final", exception);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Unable to init constructor of " + targetClass + ". Maybe it's not public?", exception);
        }
        return script;
    }

    public void unloadScript(String name) {
        if (name == null) throw new IllegalArgumentException("name parameter cannot be null");
        this.loaders.removeIf(loader -> loader.getName().equals(name));
    }
}
