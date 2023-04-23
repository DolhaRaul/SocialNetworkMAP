package run;

import domain.Message;
import domain.exceptions.EntityIsNull;
import domain.validators.MessageValidator;
import domain.validators.PrietenieValidator;
import domain.validators.UserValidator;
import domain.exceptions.ValidatorException;
import repo.MessageDBRepository;
import repo.PrietenieDBRepository;
import repo.UtilizatorDBRepository;
import service.ForDataBase.ServiceFriendsDB;
import service.ForDataBase.ServiceUsersDB;
import ui.UserInterface;
import utils.Utils;

import java.io.IOException;
import java.util.stream.Collectors;

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
        MessageDBRepository msgDBmessages = new MessageDBRepository(url, username, password, new MessageValidator());
        msgDBmessages.save(new Message(2, 1, "Salut"));
        ///System.out.println(msgDBmessages.findOne(1));
        UtilizatorDBRepository repoDBUsers = new UtilizatorDBRepository(url, username, password, new UserValidator());
        PrietenieDBRepository repoDBFriends = new PrietenieDBRepository(url, username, password, new PrietenieValidator());
        ServiceUsersDB usersDB_service = new ServiceUsersDB(repoDBUsers, repoDBFriends);
        ServiceFriendsDB friendsDB_service = new ServiceFriendsDB(repoDBUsers, repoDBFriends);
        UserInterface ui = new UserInterface(usersDB_service, friendsDB_service, msgDBmessages);
        ui.run_menu();
    }
}