package domain.exceptions;

public class ValidatorException extends MyException
{
    /**
     * construim exceptia in care stim continutul mesajului
     * @param message-String
     */
    public ValidatorException(String message)
    {
        super(message);
    }

    /**
     * construim exceptia in cazul in care stim continutul mesajului si cauza exceptiei
     * @param message-String
     * @param cause-Throwable
     */
    public ValidatorException(String message, Throwable cause) {
        super(message);
    }
}
