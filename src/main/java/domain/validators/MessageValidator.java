package domain.validators;

import domain.Message;
import domain.exceptions.ValidatorException;

public class MessageValidator implements Validator<Message>
{
    @Override
    public void validate(Message mesaj) throws ValidatorException {
        if("".equals(mesaj.getContinut()))
            throw new ValidatorException("Continutul mesajului nu poate fi vid!");
        if(mesaj.getId_sender() <= 0)
            throw new ValidatorException("Id ul celui care a trimis mesajul trebuie sa fie pozitiv!");
        if(mesaj.getId_receiver() <= 0)
            throw new ValidatorException("Id ul celui ce a primit mesajul trebuie sa fie pozitiv!");
        ///sender ul si receiver ul POT sa fie unul si acelasi(am vzt pe retele ca poti sa iti trimiti mesaje tie singur)
    }
}
