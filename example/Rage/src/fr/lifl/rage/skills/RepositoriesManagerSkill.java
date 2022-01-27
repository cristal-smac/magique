package fr.lifl.rage.skills;

import java.util.*;
import fr.lifl.rage.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.platform.*;

public class RepositoriesManagerSkill extends MagiqueDefaultSkill {

    /**
     * The created repositories.
     */
    protected Set repositories;

    /**
     * Creates a new <code>RepositoriesManager</code>
     */
    public RepositoriesManagerSkill(Agent a) {
	super(a);
	repositories = Collections.synchronizedSet(new HashSet());
    }

    //
    // Lance un agent TaskRepository sur la meme plateforme
    //
    public void startRepositoriesManagerSkill() {
	Platform p = getMyAgent().getPlatform();
	Agent repository = new Agent("ResultRepository");

	repository.addSkill(new ResultRepositorySkill(repository));
	p.addAgent(repository);
	repository.connectToBoss(getMyAgent().getName());
    }

    /* 
	public Object ExecuteRemoteQuery(RemoteQuery remoteQuery) {	
	}
    */
}



