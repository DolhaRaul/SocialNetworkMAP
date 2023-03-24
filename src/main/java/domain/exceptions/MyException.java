package domain.exceptions;

public class MyException extends Exception
{
    /**
     * clasa proprie de exceptie ce mosteneste Exception si ne spune cand am gasit o entitate
     * @param message-String
     */
    public MyException(String message)
    {
        super(message);
    }
}
