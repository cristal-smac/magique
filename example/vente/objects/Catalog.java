// Catalog.java
package vente.objects;

import java.util.*;

public class Catalog extends Vector implements java.io.Serializable
{

	public String getArticlesNames()
	{
		String result = new String() ;
		for (int i=0 ; i<this.size() ; i++)
		{
			if (this.elementAt(i) instanceof Article)
			{
				result = result + ((Article)this.elementAt(i)).getNom() + "\n" ;
			}
		}
		return result ;
	} //getArticleNames

	public String toString()
	{
		String result = new String() ;
		for (int i=0 ; i<this.size() ; i++)
		{
			if (this.elementAt(i) instanceof Article)
			{
				result = result + i +"  "+ ((Article)this.elementAt(i)).toString() + "\n" ;
			}
		}
		return result ;	
	} // toString

	public boolean contains(Article a)
	{
		boolean cont = false ;
		for (int i=0 ; i<this.size() && !cont ; i++)
		{
			if ( ((String)((Article)this.elementAt(i)).getNom()).compareTo( a.getNom()) == 0)
			{
				cont = true ;
			}
		} // for
//		System.out.println("check Contains " + cont) ;
		return cont ;
	} // contains

	public void removeElement(Article a)
	{
		boolean out = false ;
		for (int i=0 ; i<this.size() && !out ; i++)
		{
			if ( ((Article)this.elementAt(i)).getNom().compareTo(a.getNom()) ==0 )
			{
				this.removeElementAt(i) ;
				out = true ;
			} // if
		} // for
	} // removeElement
	

	
} // Catalog.java
