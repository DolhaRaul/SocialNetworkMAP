package domain.validators;

public class ValidatorException extends Exception
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
    public ValidatorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * construim exceptia in cazul in care stim mesajul, cauza, daca supresia e valabila sau nu si daca poate scrie detalii
     * despre aceasta eroare(linia unde s-a produs, etc)
     * @param message-String
     * @param cause-Throwable
     * @param enableSuppression-boolean
     * @param writableStackTrace-boolean
     */
    public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * exceptia in cazul in care s-a prodsu cauza
     * @param cause-Throwable
     */
    public ValidatorException(Throwable cause)
    {
        super(cause);
    }
}
