package service.ForFile;

import domain.Prietenie;
import domain.exceptions.EntityIsNull;
import domain.exceptions.EntityNotFound;
import domain.exceptions.ValidatorException;
import repo.PrietenieFileRepository;
import repo.UserFileRepository;

public class ServicePrietenii
{
    UserFileRepository users_repo;
    PrietenieFileRepository prietenii_repo;

    /**
     * constructor cu parametrii, in care intializam datele membre cu instante de tipul UserFileRepository, respectiv
     * PrietenieFileRepository
     * @param users_repo-UserFileRepository
     * @param prietenii_repo-PrietenieFileRepository
     */
    public ServicePrietenii(UserFileRepository users_repo, PrietenieFileRepository prietenii_repo)
    {
        this.users_repo = users_repo;
        this.prietenii_repo = prietenii_repo;
    }

    /**
     * Cream o prietenie si o returnam(daca aceasta exista deja, doar o returnam)
     * @param id-Integer
     * @param id_user1-Integer
     * @param id_user2-Integer
     * @return Prietenia creata pe baza datelor primite ca parametru
     * @throws ValidatorException
     */
    public Prietenie add_prietenie(int id, int id_user1, int id_user2) throws ValidatorException, EntityIsNull {
        /*
        OBSERVATIE!!!MEREU SA TINEM CONT CA EXCEPTIILE RunTimeError, Error si CeleDerivate din acestea NU VOR APAREA AUTOMAT
        IN DEFINITIA FUNCTIEI(la fel si pentru functiile in care aceasta este folosita, nu se propaga fiindca ELE SUNT DE TIPUL
        UNCHECKED(pot aparea si fara sa fie mentionate), asa ca trebuie apelate explicit
         */
        ///exista acesti useri intre care cream prietenia
        if(users_repo.findOne(id_user1) != null && users_repo.findOne(id_user2) != null)
        {
            Prietenie friendship = new Prietenie(id, id_user1, id_user2);
            this.prietenii_repo.save(friendship);
            return friendship;
        }
        return null;///nu s-a putut adauga prietenia
    }

    /**
     * Stergem si returnam o prietenie de un id dorit(daca aceasta exista)
     * @param id-Integer
     * @return Prietenia stearsa pentru un id introdus
     * @throws EntityIsNull -ID ul prieteniei de sters este vid(nu a fost introdus)
     * @throws EntityNotFound -Prietenia de sters nu a fost gasita
     */

    public Prietenie delete_prietenie(int id) throws EntityIsNull, EntityNotFound {
        Prietenie friendship = this.prietenii_repo.delete(id);
        if(friendship == null)///prietenia de sters nu a fost gasita
            throw new EntityNotFound("Prietenia de sters nu exista!");
        return friendship;
    }

    /**
     * Metoda ne obtine colectie(iterabila) de prietenii
     * @return colectia noastra de prietenii
     */
    public Iterable<Prietenie> findAll()
    {
        return this.prietenii_repo.findAll();
    }

    /**
     * Obtinem detaliile unei prietenii de un anumit id
     * @param id-Integer
     * @return Prietenie de un anumit id
     */
    public Prietenie findOne(int id)
    {
        return this.prietenii_repo.findOne(id);
    }

    /**
     * Obtinem cu cate relatii de prietenie este reteaua noastra populata pana acm
     * @return numarul de relatii de prietenie existente in retea
     */
    public int size()
    {
        return this.prietenii_repo.size();
    }
}
