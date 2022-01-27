package fr.lifl.rage.wrappers;

import java.io.*;
import fr.lifl.rage.*;

/**
 * Encapsulates an object implementing the interface <code>Result</code>. 
 *
 * This is required because polymorphism of methods isn't implemented in Magique.
 * However, these objects are only used for internal purposes by Rage and shouldn't 
 * be used by users.
 *
 * @author Patrice Boulanger 
 **/
public class WrapperResult implements Serializable
{

    private fr.lifl.rage.Result result;
    
    public WrapperResult(fr.lifl.rage.Result result)
    {
        this.result = result;
    }

    public Result getResult()
    {
        return this.result;
    }

}
