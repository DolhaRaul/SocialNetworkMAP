package run;

import domain.Prietenie;
import domain.User;
import domain.validators.EntityIsNull;
import domain.validators.PrietenieValidator;
import domain.validators.UserValidator;
import domain.validators.ValidatorException;
import repo.PrietenieDBRepository;
import repo.Repository;
import repo.UtilizatorDBRepository;
import service.ForDataBase.ServiceFriendsDB;
import service.ForDataBase.ServiceUsersDB;
import ui.UserInterface;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ValidatorException, IOException, EntityIsNull {
//        String usersFileName = Config.getProperties().getProperty("Users");
//        String friendshipsFileName = Config.getProperties().getProperty("Prietenii");
//        UserFileRepository users_repo = new UserFileRepository(new UserValidator(), usersFileName);
//        PrietenieFileRepository prietenii_repo = new PrietenieFileRepository(new PrietenieValidator(), friendshipsFileName);
//        ServiceUsers users_service = new ServiceUsers(users_repo, prietenii_repo);
//        ServicePrietenii prietenii_service = new ServicePrietenii(users_repo, prietenii_repo);
//        UserInterface ui = new UserInterface(users_service, prietenii_service);
//        ui.run_menu();
        final String url = "jdbc:postgresql://localhost:5432/postgres";
        final String username = "postgres";
        final String password = "Rauldolha1";
        Repository<Integer, User> repoDBUsers = new UtilizatorDBRepository(url, username, password, new UserValidator());
        Repository<Integer, Prietenie> repoDBFriends = new PrietenieDBRepository(url, username, password, new PrietenieValidator());
        ServiceUsersDB usersDB_service = new ServiceUsersDB(repoDBUsers, repoDBFriends);
        ServiceFriendsDB friendsDB_service = new ServiceFriendsDB(repoDBUsers, repoDBFriends);
        UserInterface ui = new UserInterface(usersDB_service, friendsDB_service);
        ui.run_menu();
    }
}