package com.forGit.watchElements.models;

/**
 * Created by sebastiandeutsch on 08.07.14.
 */
public class ScopesM {
    private boolean on;

    private char character;

    public ScopesM(char character) {
        this.character = character;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public char getCharacter() {
        return character;
    }
}
