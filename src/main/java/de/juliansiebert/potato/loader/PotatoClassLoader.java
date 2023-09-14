package de.juliansiebert.potato.loader;

import java.net.URL;
import java.net.URLClassLoader;

public final class PotatoClassLoader extends URLClassLoader {
    private final String name;

    public PotatoClassLoader(String name, URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.name = name;
    }

    @Override
    protected void addURL(URL url) {
        if (url == null) throw new IllegalArgumentException("URL cannot be null");
        super.addURL(url);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
