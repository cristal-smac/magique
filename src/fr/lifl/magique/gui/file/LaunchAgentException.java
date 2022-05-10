package fr.lifl.magique.gui.file;

import fr.lifl.magique.gui.draw.GraphicAgent;

import java.io.IOException;

public class LaunchAgentException extends IOException {
    private final GraphicAgent agent;

    public LaunchAgentException(GraphicAgent agent, String message) {
        super(message);
        this.agent = agent;
    }

    public GraphicAgent getAgent() {
        return agent;
    }
}
