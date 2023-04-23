package controller;

import domain.Message;
import domain.Prietenie;
import domain.User;
import domain.validators.MessageValidator;
import domain.validators.PrietenieValidator;
import domain.validators.UserValidator;
import repo.MessageDBRepository;
import repo.PrietenieDBRepository;
import repo.Repository;
import repo.UtilizatorDBRepository;
import service.ForDataBase.ServiceFriendsDB;
import service.ForDataBase.ServiceMessagesDB;
import service.ForDataBase.ServiceUsersDB;

public class DataController
{
    private ServiceUsersDB users_service;
    private ServiceFriendsDB friends_service;

    private ServiceMessagesDB messages_service;;

    public DataController()
    {
        final String url = "jdbc:postgresql://localhost:5432/postgres";
        final String username = "postgres";
        final String password = "Rauldolha1";
        UtilizatorDBRepository repoDBUsers = new UtilizatorDBRepository(url, username, password, new UserValidator());
        PrietenieDBRepository repoDBFriends = new PrietenieDBRepository(url, username, password, new PrietenieValidator());
        MessageDBRepository repoDBMesaje = new MessageDBRepository(url, username, password, new MessageValidator());

        users_service = new ServiceUsersDB(repoDBUsers, repoDBFriends);
        friends_service = new ServiceFriendsDB(repoDBUsers, repoDBFriends);
        messages_service = new ServiceMessagesDB(repoDBUsers, repoDBMesaje);
    }

    public ServiceUsersDB getUsers_service() {
        return users_service;
    }

    public ServiceFriendsDB getFriends_service()
    {
        return friends_service;
    }

    public ServiceMessagesDB getMessages_service(){return messages_service;}
}
