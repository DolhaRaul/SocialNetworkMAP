package service.ForFile;

import domain.Prietenie;
import domain.User;
import domain.exceptions.EntityIsNull;
import domain.exceptions.EntityNotFound;
import domain.exceptions.ValidatorException;
import repo.PrietenieFileRepository;
import repo.UserFileRepository;

import java.util.HashMap;

public class ServiceUsers
{
    private UserFileRepository users_repo;
    private PrietenieFileRepository friends_repo;

    /**
     * constructor cu parametrii, in care intializam datele membre cu insatnte de tipul UsersFileRepository, respectiv
     * PrietenieFileRepository; Datele in fisiere vor fi incarcate automat tinand, in constructorii claselor de tip repo
     * @param users_repo-UserFileRepository
     * @param friends_repo-PrietenieFileRepository
     */
    public ServiceUsers(UserFileRepository users_repo, PrietenieFileRepository friends_repo)
    {
        this.users_repo = users_repo;
        this.friends_repo = friends_repo;
    }

    /**
     * construim un obiect de tip User(daca se poate) si il salvam in colectia de utilizatori(colectie de tip HashMap)
     * @param id-Integer
     * @param nume-String
     * @param prenume-String
     * @param oras-String
     * @return obiectul de tip User ce va fi construit(daca se poate) pe baza datelor primite
     * @throws ValidatorException -Exception
     */
    public User add_user(int id, String nume, String prenume, String oras) throws ValidatorException, EntityIsNull {
        User user = new User(id, nume, prenume, oras);
        return this.users_repo.save(user);
    }

    /**
     * Stergem un utilizator pe baza unui id primit. Daca nu gasim nici un utilizator pentru un id primit, atunci vom
     * genera o eroare
     * @param id-Integer
     * @return Utilizatorul pe care l-am sters pe baza id-ului ce l-am primit ca si parametru
     * @throws IllegalArgumentException -user ul nu a fost gasit
     */
    public User delete_user(int id) throws EntityIsNull, EntityNotFound
    {
        User user = this.users_repo.delete(id);///utilizatorul de sters
        if(user == null)
            throw new EntityNotFound("Utilizatorul de sters nu a fost gasit!");

        Iterable<Prietenie> relatii = friends_repo.findAll();
        HashMap<Integer, Prietenie> prietenii_de_sters = new HashMap<>();
        for(Prietenie relatie: relatii)///vedem toate relatiile de prietenie
        {
            if (relatie.getId_user2() == user.getID() || relatie.getId_user1() == user.getID())
              prietenii_de_sters.put(relatie.getID(), relatie);
        }
        for(Integer cheie: prietenii_de_sters.keySet())
            friends_repo.delete(cheie);
        return user;
    }

    /**
     * Metoda ne obtine colectie(iterabila) de utilizatori
     * @return colectia noastra de utilizatori
     */
    public Iterable<User> findAll()
    {
        return this.users_repo.findAll();
    }

    /**
     * Obtinem cu cati utilizatorii este reteaua noastra populata pana acm
     * @return numarul de users de pana acm
     */

    /**
     * Obtinem detaliile despre un anumit utilizator de ID dorit
     * @param id-Integer
     * @return Un utilizator de un anumit id
     */
    public User findOne(int id)
    {
        return this.users_repo.findOne(id);
    }
    public int size()
    {
        return this.users_repo.size();
    }
}
