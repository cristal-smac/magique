public class Card implements java.io.Serializable
{
    public static Card ROCK = new Card();
    public static Card PAPER = new Card();
    public static Card SCISSORS = new Card();

    public static  Card fromChar(Character c) 
    {
        switch (c.charValue()) 
	    {
	    case 'r':
	    case 'R':
		return ROCK;
	    case 's':
	    case 'S':
		return SCISSORS;       
	    case 'p':
	    case 'P':
		return PAPER;            
	    default :	return null; // ne doit jamais se produire !!
	    }
    }

    public String toString()
    {
	if (this==ROCK) return "Rock";
	if (this==PAPER) return "Paper";
	if (this==SCISSORS) return "Scissors";
	return null; // ne doit jamais se produire !!
	
    }

    public Character toCharacter()
    {
	if (this==ROCK) return new Character('r');
	if (this==PAPER) return new Character('p');
	if (this==SCISSORS) return new Character('s');
	return null; // ne doit jamais se produire !!
	
    }

    public int compareTo(Card c)
    { 
	if (this==ROCK)
	    {
		if (c==ROCK) return 0;
		if (c==SCISSORS) return +1;
		if (c==PAPER) return -1;
	    }
	if (this==SCISSORS)
	    {
		if (c==ROCK) return -1;
		if (c==SCISSORS) return 0;
		if (c==PAPER) return +1;
	    }
	    if (this==PAPER)
		{
		    if (c==ROCK) return +1;
		    if (c==SCISSORS) return -1;
		    if (c==PAPER) return 0;
		}
	    return 0; // ne doit jamais se produire !
    }
}
