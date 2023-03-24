package domain.validators;

import domain.User;
import domain.exceptions.ValidatorException;

public class UserValidator implements Validator<User>
{
    /**
     * verificam daca un user este valid sau nu
     * @param entity-User
     * @throws ValidatorException -validam un user(acesta sa fie in format corect)
     */
    @Override
    public void validate(User entity) throws ValidatorException
    {
        if(entity.getID() <= 0)
            throw new ValidatorException("Id-ul userului trebuie sa fie pozitiv!");
        if(entity.getNume().equals(""))
            throw new ValidatorException("Numele userului nu poate fi vid!");
        if(entity.getPrenume().equals(""))
            throw new ValidatorException("Prenumele userului nu poate fi vid!");
        if(entity.getOras().equals(""))
            throw new ValidatorException("Orasul userului nu poate fi vid!");
        /**
         * tinem minte aici, pe rand, numele, prenumele si orasul user ului
         */
        for(Character c: entity.getNume().toCharArray())
        {
            if(Character.isDigit(c))
            {
                throw new ValidatorException("Numele nu poate contine cifre");
            }
        }
        for(Character c: entity.getPrenume().toCharArray())
        {
            if(Character.isDigit(c))
            {
                throw new ValidatorException("Prenumele nu poate contine cifre");
            }
        }
        for(Character c: entity.getOras().toCharArray())
        {
            if(Character.isDigit(c))
            {
                throw new ValidatorException("Orasul nu poate contine cifre");
            }
        }
    }
}
