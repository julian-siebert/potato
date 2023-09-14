package de.juliansiebert.potato;

public interface PotatoScript {

    void start();

    void stop();

    /**
     * Make sure that ypu completely remove any references that stop the gc from deleting this instance.
     */
    void kill();

    String getName();

    String getVersion();
}
