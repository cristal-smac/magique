package fr.lifl.rage;

import fr.lifl.rage.wrappers.*;

public interface BD4Rage
{

    public void insert(WrapperResult wrapperResult);

    public java.util.ArrayList select(Query query);

    public java.util.ArrayList selectAndDelete(Query query);

    public void delete(Query query);

    public void cleanUp();

}
