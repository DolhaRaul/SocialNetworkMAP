package service.ForJavaFx;

import domain.User;
import domain.validators.EntityIsNull;
import domain.validators.ValidatorException;
import javafx.collections.ObservableList;
import repo.InMemoryRepository;
import repo.Repository;
import utils.events.ChangeEventType;
import utils.events.UtilizatorEntityChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;

public class UtilizatorJavaFxService implements Observable<UtilizatorEntityChangeEvent>
{
    private Repository<Integer, User> repoUsers;
    private List<Observer<UtilizatorEntityChangeEvent>> observers=new ArrayList<>();

    public UtilizatorJavaFxService(Repository<Integer, User> repo)
    {
        this.repoUsers = repo;
    }

    public User addUtilizator(User user) throws EntityIsNull, ValidatorException {
        if(repoUsers.save(user) == null){
            UtilizatorEntityChangeEvent event = new UtilizatorEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public User deleteUtilizator(Integer id) throws EntityIsNull, SQLException
    {
        User user=repoUsers.delete(id);
        if (user != null)
        {
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, user));
            return user;
        }
        return null;
    }

    public Iterable<User> getAll()
    {
        return repoUsers.findAll();
    }



    @Override
    public void addObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }

}
