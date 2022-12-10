package domain.validators;

public interface Validator<T>
{
    /**
     * verificam daca entitatea transmisa ca paarmetru e valida
     * @param entity-T
     * @throws ValidatorException -daca parametrul generic T nu e in format corect
     */
    void validate(T entity) throws ValidatorException;
}
