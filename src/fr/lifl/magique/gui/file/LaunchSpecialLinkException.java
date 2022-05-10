package fr.lifl.magique.gui.file;

import fr.lifl.magique.gui.draw.GraphicLink;

import java.io.IOException;

public class LaunchSpecialLinkException extends IOException {
    private final GraphicLink link;

    public LaunchSpecialLinkException(GraphicLink link, String message) {
        super(message);
        this.link = link;
    }

    public GraphicLink getSpecialLink() {
        return link;
    }
}
