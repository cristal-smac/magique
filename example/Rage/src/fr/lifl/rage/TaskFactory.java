package fr.lifl.rage;

public interface TaskFactory {

    /**
     * Returns <code>true</code> if this factory has more task to provide.
     *
     * @return returns <code>true</code> if this factory has more task to provide.
     */
    public boolean hasNext();

    /**
     * Returns the next task of this factory.
     *
     * @return the next task of this factory.
     */
    public Task next();

    /**
     * Return the factory identificator of this factory
     *
     * @return the factory identificator of this factory.
     */
    public String getFactoryID();

}
 
