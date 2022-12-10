package repo;

import domain.Entity;
import domain.validators.Validator;

public class InMemoryRepository<ID, E extends Entity<ID>> extends AbstractRepo<ID, E>
{
    /**
     *constructor cu parametrii
     * @param validator-Validator pentru elementul generic E
     */
    public InMemoryRepository(Validator<E> validator)
    {
        super(validator);
    }
}
