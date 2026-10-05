package com.jonah.stratabasic.entity;

// What layer creepers share: blast radius (can be fractional) and whether the blast spreads fire
public interface DeepCreeper {

    float blastRadius();

    boolean spreadsFire();

    // Set when blast has started so it isn't redirected again
    boolean blastStarted();

    void startBlast();
}
