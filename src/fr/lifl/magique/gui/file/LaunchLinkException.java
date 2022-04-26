package fr.lifl.magique.gui.file;

import fr.lifl.magique.gui.draw.GraphicLink;

import java.io.IOException;

public class LaunchLinkException extends IOException {
    private final GraphicLink link;

    public LaunchLinkException(GraphicLink link, String message) {
        super(message);
        this.link = link;
    }

    public GraphicLink getLink() {
        return link;
    }
}
