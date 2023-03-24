package controller;

import domain.Prietenie;
import domain.User;
import domain.validators.PrietenieValidator;
import domain.validators.UserValidator;
import repo.PrietenieDBRepository;
import repo.Repository;
import repo.UtilizatorDBRepository;
import service.ForDataBase.ServiceFriendsDB;
import service.ForDataBase.ServiceUsersDB;

public class DataController
{
    private ServiceUsersDB users_service;
    private ServiceFriendsDB friends_service;
    private Repository<Integer, User> repoUsers;
    private Repository<Integer, User> repoFriends;

    public DataController()
    {
        final String url = "jdbc:postgresql://localhost:5432/postgres";
        final String username = "postgres";
        final String password = "Rauldolha1";
        UtilizatorDBRepository repoDBUsers = new UtilizatorDBRepository(url, username, password, new UserValidator());
        PrietenieDBRepository repoDBFriends = new PrietenieDBRepository(url, username, password, new PrietenieValidator());
        users_service = new ServiceUsersDB(repoDBUsers, repoDBFriends);
        friends_service = new ServiceFriendsDB(repoDBUsers, repoDBFriends);
    }

    public ServiceUsersDB getUsers_service() {
        return users_service;
    }

    public ServiceFriendsDB getFriends_service()
    {
        return friends_service;
    }
}
