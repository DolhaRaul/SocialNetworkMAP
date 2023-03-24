package domain.validators;

public class EntityIsNull extends Exception
{
    /**
     * clasa proprie de exceptie ce se apeleaza(pentru o arunca o exceptie) cand o entitate este null si nu trebuie sa fie
     * @param message-String
     */
    public EntityIsNull(String message)
    {
        super(message);
    }
}
