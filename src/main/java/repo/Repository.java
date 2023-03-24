package repo;

import domain.exceptions.EntityIsNull;
import domain.exceptions.ValidatorException;

import java.sql.SQLException;

public interface Repository<ID, E>
{
    /**
     * salvam entitatea in repository
     * @param entity-E
     * @return Entitatea salvata sau null daca nu o putem salva
     */
    E save(E entity) throws IllegalArgumentException, ValidatorException, EntityIsNull;

    /**
     * salvam entitatea
     * @param id-ID
     * @return entiatea pe care am sters o sau null daca entitatea de sters nu exista
     * @throws EntityIsNull
     */
    E delete(ID id) throws EntityIsNull, SQLException;

    /**
     *
     * @param id
     * @return
     */
    E findOne(ID id);

    /**
     * @return toate elementele colectiei
     */
    Iterable<E> findAll();

    /**
     * obtinem cate entitati are repo ul pana in momentul de fata
     * @return numarul de entitati
     */
    int size();
}
