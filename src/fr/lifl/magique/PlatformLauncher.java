package fr.lifl.magique;

public class PlatformLauncher extends AbstractMagiqueMain {

    static AtomicAgent agent;

    public static void main(String[] args) {

        if (args.length == 0) {
            try {
                fr.lifl.magique.Start.go("fr.lifl.magique.PlatformLauncher", args);
            } catch (Exception e) {
                System.out.println("Impossible de lancer la platform");
                e.printStackTrace();
                System.exit(1);
            }
        }
        if (args.length == 1) {
            try {
                String[] theTrueArgs = new String[args.length - 1];
                for (int i = 0; i < args.length - 1; i++) {
                    theTrueArgs[i] = args[i - 1];
                }
                fr.lifl.magique.Start.go("fr.lifl.magique.PlatformLauncher", Integer.valueOf(args[0]).intValue(), theTrueArgs);
            } catch (Exception e) {
                System.out.println("Impossible de lancer la platform");
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    public void theRealMain(String[] args) {

        if (args.length >= 1) Agent.setVerboseLevel(Integer.parseInt(args[0]));

        agent = createAgent("fr.lifl.magique.AtomicAgent", "AgentCreator");

        agent.addSkill(new fr.lifl.magique.gui.skills.ClassLoaderSkill(agent));
        agent.addSkill(new fr.lifl.magique.gui.skills.AgentCreatorSkill(agent));

        System.out.println("PlatformLauncher launched");
    }
}

