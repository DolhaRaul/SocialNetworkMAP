package repo;

import domain.Entity;
import domain.exceptions.EntityIsNull;
import domain.validators.Validator;
import domain.exceptions.ValidatorException;

import java.util.HashMap;
import java.util.Map;

public  class AbstractRepo<ID, E extends Entity<ID>> implements Repository<ID, E>
{
    protected Map<ID, E> items;///elementele repo ului

    protected Validator<E> validator;///un validator care valideaza elementele de tip E

    /**
     * constructor cu parametrii pentru clasa AbstarctRepository(desi nu il vom apela niciodata cu exceptia claselor
     * derivate, fiindca clasa aceasta este asbtracta)
     * @param validator-Validato
     */
    public AbstractRepo(Validator<E> validator)
    {
        this.items = new HashMap<>();
        this.validator = validator;
    }

    /**
     * salvam si returnam entitatea
     * @param entity-E
     * @return entitatea de adaugat
     * @throws IllegalArgumentException -nu e folosita
     * @throws ValidatorException -im cazul in care entitatea de salvat nu e in format corect
     * @throws EntityIsNull -entitatea e nula(nu are datele membre initializate cu ceva)
     */
    @Override
    public E save(E entity) throws IllegalArgumentException, ValidatorException, EntityIsNull {
        /*
        OBSERVATIE!!!MEREU SA TINEM CONT CA EXCEPTIILE RunTimeError, Error si CeleDerivate din acestea NU VOR APAREA AUTOMAT
        IN DEFINITIA FUNCTIEI(la fel si pentru functiile in care aceasta este folosita, nu se propaga fiindca ELE SUNT DE TIPUL
        UNCHECKED(pot aparea si fara sa fie mentionate), asa ca trebuie apelate explicit
        De ex, IllegalArgumentException(pe care am inlocuit o cu EntityIsNull pentru ca aceasta,facuta de noi, E CHECKED)
        IlleagalArgumentException nu e folosita
         */
            if (entity == null)
                throw new EntityIsNull("Entitatea nu poate fi nula");
            if (items.containsKey(entity.getID()))///exista deja entitatea pe care vrem sa o aduagam
                return entity;
            ///suntem in cazul in care entitatea nu se afla in colectia noastra de entitati
            validator.validate(entity);
            items.put(entity.getID(), entity);
            return entity;
    }

    /**
     * stergem o entitate de un Id dorit
     * @param id-ID
     * @return entitatea pe care am sters o
     * @throws EntityIsNull -in cazul in care ID ul introdus e vid
     */
    @Override
    public E delete(ID id) throws EntityIsNull
    {
        if(id == null)
            throw new EntityIsNull("Id ul nu poate fi vid");
        E entity = null;///initial entity e null(e o referinta catre o zona unde nu avem nimic)
        if(items.containsKey(id))
        {
            entity = items.get(id);///exista obiectul
            items.remove(id);
        }
        return entity;
    }

    /**
     * Gasim o entitatea de un anumit ID, daca exista
     * @param id-ID
     * @return entitatea de un anumit ID sau null daca aceasta nu exista
     */
    @Override
    public E findOne(ID id) {
        if(items.containsKey(id))
            return items.get(id);
        return null;
    }

    /**
     * Elementele repo ului sub forma unei colectii
     * @return toate elementele repo ului
     */
    @Override
    public Iterable<E> findAll() {
        return items.values();
    }

    /**
     * Metoda ne spune cate entitati avem pana in momentul curent
     * @return numarul de entitati alre repo ului
     */
    @Override
    public int size()
    {
        return this.items.size();
    }
}
