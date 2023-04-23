package service.ForDataBase;

import domain.Message;
import domain.User;
import domain.exceptions.EntityIsNull;
import domain.exceptions.ValidatorException;
import repo.MessageDBRepository;
import repo.UtilizatorDBRepository;
import utils.Utils;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class ServiceMessagesDB
{
    private final UtilizatorDBRepository users_repo;

    private final MessageDBRepository messages_repo;

    public ServiceMessagesDB(UtilizatorDBRepository users_repo, MessageDBRepository messages_repo)
    {
        this.users_repo = users_repo;
        this.messages_repo = messages_repo;
    }

    /**
     * Adaugam un mesaj, stiind user iii intre care se trimite mesajul si continutul sau
     * @param id_sender-id ul user ului ce trimite msajul
     * @param id_receiver-id ul user ului ce primeste mesajul
     * @param continut-continutul mesajului
     * @return mesajul de adaugat
     */
    public Message add_message(int id_sender, int id_receiver, String continut) throws EntityIsNull, ValidatorException
    {
        if(users_repo.findOne(id_sender) != null && users_repo.findOne(id_receiver) != null)
        {
            Message mesaj = new Message(id_sender, id_receiver, continut);
            messages_repo.save(mesaj);
            return mesaj;
        }
        return null;///mesajul nu a putut fi adaugat
    }

    /**
     * @param id-id ul mesajului
     * @return Mesajul de id dorit
     */
    public Message delete_message(int id)
    {
        return  messages_repo.delete(id);
    }

    /**
     * @param id-id ul mesajului de modificat
     * @return mesajul modificat sau nimic daca mesajul de modificat nu exista
     */
    public Message update_message(int id, String new_continut)
    {
        Message mesaj = this.messages_repo.findOne(id);
        if(mesaj != null)
            mesaj.setContinut(new_continut);
        return mesaj;
    }

    /**
     * @return lista de mesaje
     */
    public List<Message> findAll()
    {
        return Utils.toStream(messages_repo.findAll()).toList();
    }

    /**
     * @return numarul de mesaje
     */
    public int size()
    {
        return findAll().size();
    }

    /**
     * Returnam toate mesajele existente intre doi useri(care pot fi unul si acelasi), ordonate dupa datile
     * de trimitere a acestor mesaje
     * @param first-User
     * @param second-User
     * @return mesajele dintre doi useri, ordonate crescator dupa datile lor
     */
    public List<Message> findMessagesBtwUser(User first, User second)
    {
        return this.findAll().stream().filter(x->
                {return ( x.getId_sender() == first.getID() && x.getId_receiver() == second.getID() ) ||
                        ( x.getId_receiver() == first.getID() && x.getId_sender() == second.getID() );})
                .sorted(Comparator.comparing(Message::getData)).toList();
    }
}
