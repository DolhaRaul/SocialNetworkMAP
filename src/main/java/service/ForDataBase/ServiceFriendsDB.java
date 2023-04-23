package service.ForDataBase;

import domain.Entity;
import domain.Prietenie;
import domain.PrietenieState;
import domain.User;
import domain.exceptions.EntityIsNull;
import domain.exceptions.EntityNotFound;
import domain.exceptions.ValidatorException;
import repo.PrietenieDBRepository;
import repo.UtilizatorDBRepository;
import utils.Utils;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.StreamSupport;

public class ServiceFriendsDB
{
    private final UtilizatorDBRepository users_repo;
    private final PrietenieDBRepository prietenii_repo;

    /**
     * constructor cu parametrii, in care intializam datele membre cu instante de tipul UserFileRepository, respectiv
     * PrietenieFileRepository
     * @param users_repo-UserFileRepository
     * @param prietenii_repo-PrietenieFileRepository
     */
    public ServiceFriendsDB(UtilizatorDBRepository users_repo,PrietenieDBRepository prietenii_repo)
    {
        this.users_repo = users_repo;
        this.prietenii_repo = prietenii_repo;
    }

    private Integer generateId()
    {
        return Utils.toStream(findAll()).map(Entity::getID).reduce(Math::max).orElse(0) + 1;
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

    public Prietenie sendRequest(User from, User to) throws EntityIsNull, ValidatorException, EntityNotFound
    {
        /*Tinand cont ca Prietenia se seteaza implicit ca fiind Accepted, CEEA CE NU E OK(ASA AM FCT NOI PULA ASTA)
        TREBUIE sa apelam update pe Prietenie si sa o facem de tip Pending(fiindca asta este o cerere)
         */
        Integer id = generateId();
        Prietenie prietenie = this.add_prietenie(id, from.getID(), to.getID());
        ///implicit state ul prieteniei e Accepted
        prietenie.setState(PrietenieState.Pending);
        this.update_prietenie(id, PrietenieState.Pending);
        return prietenie;
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

    public Prietenie update_prietenie(Integer id, PrietenieState state) throws EntityNotFound {
        Prietenie friendship = this.findOne(id);
        if(friendship == null)
            throw new EntityNotFound("Prietenia de modificat nu exista!");
        this.prietenii_repo.update(id, state);
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

    public Prietenie findByUsers(User user1, User user2)
    {
        return Utils.toStream(this.findAll())
                .filter(prietenie -> prietenie.getId_user1() == user1.getID() && prietenie.getId_user2() == user2.getID() ||
                        prietenie.getId_user1() == user2.getID() && prietenie.getId_user2() == user1.getID())
                .findFirst().orElse(null);
    }

    /**
     * Obtinem cu cate relatii de prietenie este reteaua noastra populata pana acm
     * @return numarul de relatii de prietenie existente in retea
     */
    public int size()
    {
        return this.prietenii_repo.size();
    }

    /**
     * Obtinem o lista de utilizatori prieteni cu utilizatorul dat ca parametru
     * @param user-User
     * @return Toti utilizatorii care sunt prieteni cu utilizatorul dat ca parametru
     */
    public List<User> findPrieteni(User user)
    {
        return StreamSupport.stream(findAll().spliterator(), false)
                .filter(prietenie->prietenie.getId_user1() == user.getID() || prietenie.getId_user2() == user.getID())
                .filter(prietenie -> prietenie.getState() == PrietenieState.Accepted)
                .map(prietenie -> {
                    if(prietenie.getId_user1() == user.getID())
                        return users_repo.findOne(prietenie.getId_user2());
                    else
                        return users_repo.findOne(prietenie.getId_user1());
                }).toList();
    }

    /**
     * @param user-User
     * @return Toate prieteniile care au un anumit utilizator(Fie care deja exista, SUNT ACCEPTED),
     * fie care au fost trimise acestuia de catre alti utilizatori
     */
    public List<Prietenie> findCereri(User user)
    {
        List<Prietenie> prietenii = Utils.toStream(findAll()).filter(prietenie->
                ((prietenie.getReceiverId() == user.getID() || prietenie.getSenderId() == user.getID()) && prietenie.getState() == PrietenieState.Accepted)||
                prietenie.getState()==PrietenieState.Pending && prietenie.getReceiverId() == user.getID()
                ).toList();
        return prietenii;
    }

    public User findOther(Prietenie prietenie, User user){
        Integer id_other = (prietenie.getId_user1() == user.getID() ? prietenie.getId_user2() : prietenie.getId_user1());
        return users_repo.findOne(id_other);
    }
}

