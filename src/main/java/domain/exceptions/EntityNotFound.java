package domain.exceptions;

public class EntityNotFound extends MyException
{
    /**
     * clasa proprie de exceptie ce mosteneste Exception si ne spune cand am gasit o entitate
     * @param message-String
     */
    public EntityNotFound(String message)
    {
        super(message);
    }
}
