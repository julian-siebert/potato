package de.juliansiebert.potato;

public interface PotatoScript {

    void start();

    void stop();

    /**
     * Make sure that you completely remove any references that prevent the gc from deleting this instance.
     */
    void kill();

    String getName();

    String getVersion();
}
