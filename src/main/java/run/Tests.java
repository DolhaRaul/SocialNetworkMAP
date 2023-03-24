package run;

import domain.Prietenie;
import domain.User;
import domain.exceptions.EntityIsNull;
import domain.exceptions.ValidatorException;
import domain.validators.*;
import repo.*;
import config.Config;
import service.ForFile.ServicePrietenii;
import service.ForFile.ServiceUsers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Tests
{
    public static void test_in_memories_repos() throws EntityIsNull, ValidatorException {
        User user1 = new User(1, "Dolha", "Raul", "BN");
        User user2 = new User(2, "Mihai", "Muntean", "BN");
        Validator<User> validator = new UserValidator();
        InMemoryRepository<Integer, User> repo_users = new InMemoryRepository<>(validator);
        assert(user1.equals(repo_users.save(user1)));
        assert(user2.equals(repo_users.save(user2)));
        assert(repo_users.size() == 2);

        Validator<Prietenie> validator_p = new PrietenieValidator();
        InMemoryRepository<Integer, Prietenie> repo_prietenii = new InMemoryRepository<>(validator_p);
        Prietenie prietenie1 = new Prietenie(1, 1, 2);
        assert(prietenie1.equals(repo_prietenii.save(prietenie1)));
        assert(repo_prietenii.size() == 1);
    }
    public static void test_repo_users() throws ValidatorException, EntityIsNull {
        String usersFileName = Config.getProperties().getProperty("Users");
        UserFileRepository users_repo = new UserFileRepository(new UserValidator(), usersFileName);
        users_repo.findAll().forEach(x->System.out.println(x.toString()));
    }
    public static void test_repo_friendships() throws ValidatorException, IOException, EntityIsNull {
        String friendshipsFileName = Config.getProperties().getProperty("Prietenii");
        PrietenieFileRepository prietenii_repo = new PrietenieFileRepository(new PrietenieValidator(), friendshipsFileName);
        prietenii_repo.findAll().forEach(x->System.out.println(x.toString()));
    }
    public static void test_service_users() throws Exception {
        String usersFileName = Config.getProperties().getProperty("Users");
        String friendshipsFileName = Config.getProperties().getProperty("Prietenii");
        UserFileRepository users_repo = new UserFileRepository(new UserValidator(), usersFileName);
        PrietenieFileRepository prietenii_repo = new PrietenieFileRepository(new PrietenieValidator(), friendshipsFileName);
        ServiceUsers users_service = new ServiceUsers(users_repo, prietenii_repo);
        users_service.add_user(10, "Mihai", "Muntean", "Beclean");
        users_service.add_user(11, "Mihai", "Muntean", "Beclean");
        users_service.delete_user(11);
        ///users_service.findAll().forEach(x->System.out.println(x.toString()));
        try{
            users_service.delete_user(11);///nu exista nici un user de id dorit
            assert(false);
        }
        catch(Exception e)
        {
        }
    }

    public static void test_service_prietenii() throws Exception {
        String usersFileName = Config.getProperties().getProperty("Users");
        String friendshipsFileName = Config.getProperties().getProperty("Prietenii");
        UserFileRepository users_repo = new UserFileRepository(new UserValidator(), usersFileName);
        PrietenieFileRepository prietenii_repo = new PrietenieFileRepository(new PrietenieValidator(), friendshipsFileName);
        ServicePrietenii prietenii_service = new ServicePrietenii(users_repo, prietenii_repo);
        prietenii_service.add_prietenie(7, 2, 4);
        assert(prietenii_service.size() == 5);
        ///aceasta prietenie exista deja, deci nu o mai putem adauga
        assert(prietenii_service.findOne(7).equals(prietenii_service.add_prietenie(8, 2, 4)));
        assert(prietenii_service.size() == 5);
        ///prietenii_service.findAll().forEach(x->System.out.println(x.toString()));
        try{
            prietenii_service.delete_prietenie(12);///nu exista nici un user de id dorit
            assert(false);
        }
        catch(Exception e)
        {
            ///nu exista prietenia de sters
        }
        try{
            prietenii_service.add_prietenie(8, 2, 2);
            assert(false);
        }
        catch(Exception e)
        {
            ///utlizatorii relatiei sunt unul si acelasi
        }
    }
    public static void test_database_repo_users() throws EntityIsNull, ValidatorException, SQLException {
        final String url = "jdbc:postgresql://localhost:5432/postgres";
        final String username = "postgres";
        final String password = "Rauldolha1";
        User user1 = new User(1, "Dolha", "Raul", "Bistrita");
        User user2 = new User(3, "Mihai", "Pop", "Bistrita");
        User user3 = new User(4, "Maria", "Ana", "Brasov");
        List<User> utilizatori_din_DB = Arrays.asList(user1, user2, user3);
        UtilizatorDBRepository repoDB = new UtilizatorDBRepository(url, username, password, new UserValidator());
//        assert repoDB.size() == 2;///avem 2 utilizatori
//        try{
//            repoDB.save(user1);///acesta nu exista, deci se va salva in baza de date
//        }
//        catch(Exception e)
//        {
//        }
        assert repoDB.size() == 3;///avem 3 utilizatori acum
        /**
         * SAU INCEARCA SA SCHIMBI METODA FINDALL(), COMANDA SQL FIIND: SELECT * FROM USERS ORDER BY ID
         */
        List<User> utilizatori = StreamSupport.stream(repoDB.findAll().spliterator(), false).collect(Collectors.toList());
        List<User> usable_users = new ArrayList<>(utilizatori);///colectia de mai sus e aparent IMUTABILA, nu se poate face sort
        //pe colectia aceea
        Collections.sort(usable_users, (x, y) ->{return x.getID() - y.getID();});
        ///utilizatorii de mai sus sunt si cei din baza de date
        for(int i = 0; i < utilizatori_din_DB.size(); i++)
            assert(utilizatori_din_DB.get(i).getID().equals(usable_users.get(i).getID()));///utilizatorii sunt unic identificati de id
        User maybe_is = repoDB.delete(5);
        assert(maybe_is == null);///nu exista utilizatorul de sters
    }
    public static void test_database_repo_prietenii() throws EntityIsNull, ValidatorException, SQLException {
        final String url = "jdbc:postgresql://localhost:5432/postgres";
        final String username = "postgres";
        final String password = "Rauldolha1";
        Prietenie prietenie1 = new Prietenie(1, 1, 3);
        Prietenie prietenie2 = new Prietenie(2, 3, 4);
        Prietenie prietenie3 = new Prietenie(3, 3, 4);
        ///prietenie3 si prietenie2 sunt echivalente
        Repository<Integer, Prietenie>  repoDB = new PrietenieDBRepository(url, username, password, new PrietenieValidator());
        assert repoDB.size() == 2;///momentan avem 2 prietenii in baza noastra de date
        repoDB.save(prietenie3);///e una si aceeasi cu prietenie1, care exista deja, deci nu se va adauga
        assert repoDB.size() == 2;///vedem ca nu s a adaugat
        List<Prietenie> prietenii = StreamSupport.stream(repoDB.findAll().spliterator(), false).collect(Collectors.toList());
        assert prietenii.size() == 2;
        Prietenie prietenie4 = new Prietenie(4, 4, 5);///o prietenie noua
        assert(repoDB.findOne(prietenie4.getID()) == null);///vedem ca nu exista
        repoDB.save(prietenie4);
        assert repoDB.size() == 3;
        repoDB.delete(prietenie4.getID());
        assert repoDB.size() == 2;///am sters prietenia 4 inapoi
    }
}
