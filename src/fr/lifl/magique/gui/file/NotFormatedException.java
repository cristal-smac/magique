package fr.lifl.magique.gui.file;

import java.io.IOException;

public class NotFormatedException extends IOException {
    public NotFormatedException(int num, String s) {
        super("Error line " + num + ":" + s);
    }
}
