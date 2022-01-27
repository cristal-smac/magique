package fr.lifl.rage.wrappers;

import java.io.*;
import fr.lifl.rage.*;

/**
 * Encapsulates an object implementing the interface <code>Selector</code>. 
 *
 * This is required because polymorphism of methods isn't implemented in Magique.
 * However, these objects are only used for internal purposes by Rage and shouldn't 
 * be used by users.
 *
 * @author Patrice Boulanger 
 **/
public class WrapperSelector implements Serializable
{

    private fr.lifl.rage.Selector selector;
    
    public WrapperSelector(fr.lifl.rage.Selector selector)
    {
	this.selector = selector;
    }

    public Selector getSelector()
    {
	return this.selector;
    }

}
