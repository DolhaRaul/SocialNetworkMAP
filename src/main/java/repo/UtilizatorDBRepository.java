package repo;

import domain.User;
import domain.validators.Validator;
import domain.validators.ValidatorException;

import java.sql.*;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.StreamSupport;

public class UtilizatorDBRepository implements Repository<Integer, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UtilizatorDBRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * In baza noastra de date, fiecare utilizator este unic identificat printr o linie, "inregistrare", iare toata aceasta
     * tabela de utlizatori o afisam
     * @return toti utilizatorii din "tabe", din baza noastar de date
     */
    @Override
    public Iterable<User> findAll() {
        SortedSet<User> users = new TreeSet<>((x, y)->{return x.getID() - y.getID();});///pt ca vrem ca inregistrarile sunt distincte
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users ORDER BY ID");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("nume");
                String lastName = resultSet.getString("prenume");
                String oras = resultSet.getString("oras");

                User utilizator = new User(id, firstName, lastName, oras);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    /**
     * Toti utilizatorii din baza noastra de baza, returnam numarul lor
     * @return numarul de utilizatori al retelei
     */
    @Override
    public int size()
    {
        Iterable<User> numbers_of_users = findAll();
        ///convertim mai intai colectia iterabila intr un stream folosimnd clasa Spliterator(), dupa o facem sa fie lista
        List<User> users = StreamSupport.stream(numbers_of_users.spliterator(), false).toList();
        return users.size();
    }

    /**
     * Daca se poate, salvam un utilizator in baza de date
     * @param entity-E
     * @return entitatea de salvat
     */
    @Override
    public User save(User entity) throws ValidatorException {
        String sql = "insert into users (id, nume, prenume, oras) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getID());
            ps.setString(2, entity.getNume());
            ps.setString(3, entity.getPrenume());
            ps.setString(4, entity.getOras());
            validator.validate(entity);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public User delete(Integer id_del) throws SQLException {
        String sql = "delete from users where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ///metoda inlocuieste semnul intrebarii(1 in cazul nostru) din instructiunea SQL pe baza careia
            // PreparedStatement ul ps a fost creat(delete in cazul nostru) cu a doua valoarea din lisat de parametrii
            //(id_del in cazul nostri)
            ps.setInt(1, id_del);
            User user = findOne(id_del);///user ul de sters
            ps.executeUpdate();///facem stergerea, dupa ce am identificat user ul de sters(ACESTA exista practic)
            return user;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public User findOne(Integer integer) {
        String sql = "select * from users where id = ?";
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);///preparam instructiunea sql pe baza Stringului sql

            ps.setInt(1, integer);///inlocuim semnul intrebarii cu integer
            ResultSet result = ps.executeQuery();///facem query l pe baza careia obtinem tabela specifica instructiunii sql,
            ///tabelul fiind format din cel mult o inregistrare
            while(result.next())
            {
                if(result.getInt("id") == integer)///am ajuns la coloana dorita
                {
                    User user = new User(1, "", "", "");
                    user.setID(result.getInt("id"));
                    user.setNume(result.getString("nume"));
                    user.setPrenume(result.getString("prenume"));
                    user.setOras(result.getString("oras"));
                    return user;
                }
            }
        }
        catch (SQLException e)
        {
           e.printStackTrace();
        }
        return null;
    }
}
