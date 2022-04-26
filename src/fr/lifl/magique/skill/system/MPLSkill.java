/**
 * MPLSkill.java
 * <p>
 * <p>
 * Created: Wed Nov 15 11:49:24 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;

import fr.lifl.magique.agent.PlatformAgent;
import fr.lifl.magique.skill.DefaultSkill;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MPLSkill extends DefaultSkill {

    private final String STOP_MESSAGE = "STOP";
    private final String STOP_ANSWER = "STOPPED";
    private final String KILL_MESSAGE = "KILL";
    private final String KILL_ANSWER = "KILLED";
    private final String PING_MESSAGE = "PING";
    private final String PING_ANSWER = "PINGED";
    private ServerSocket server;
    private Socket mySocket = null;
    private BufferedReader in;
    private OutputStream out = null;
    /** MPL port = platformport + 1 */
    private final int port;
    private final Object monitor = new Object();

    /**
     * @param PlatformAgent the agent that owns this skill
     * @param port the port of my  platform
     */
    public MPLSkill(PlatformAgent a, int port) {
        super(a);
        this.port = port + 1;
    }

    /** monitor for socket creation detection */


    public void startMPLSkill() {
        new Thread(new MPLThread()).start();
    }

    private void performWrapper(String skill) {
        perform(skill);
    }

    class MPLThread implements Runnable {
        public void run() {
            boolean again = true;
            String msg = null;

            new Thread(new ServerSocketThread()).start();


            /* attend que le socket soit cr��e */
            synchronized (monitor) {
                while (mySocket == null) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            while (again) {
                try {
                    msg = in.readLine();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                if (msg != null) {
                    if (msg.equals(STOP_MESSAGE)) {
                        again = false;
                        performWrapper("killPlatform");
                        try {
                            out.write(STOP_ANSWER.getBytes());
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    } else if (msg.equals(KILL_MESSAGE)) {
                        again = false;
                        performWrapper("killPlatform");
                        try {
                            out.write(KILL_ANSWER.getBytes());
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    } else if (msg.equals(PING_MESSAGE)) {
                        try {
                            out.write(PING_ANSWER.getBytes());
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    class ServerSocketThread implements Runnable {
        public void run() {
            try {
                server = new ServerSocket(port);
                mySocket = server.accept();
                in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                out = mySocket.getOutputStream();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            monitor.notify();
        }
    }
} // MPLSkill
