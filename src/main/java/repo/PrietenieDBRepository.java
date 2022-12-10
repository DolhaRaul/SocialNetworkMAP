package repo;

import domain.Prietenie;
import domain.validators.EntityIsNull;
import domain.validators.Validator;
import domain.validators.ValidatorException;

import java.sql.*;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrietenieDBRepository implements Repository<Integer, Prietenie>
{
    private String url;
    private String username;
    private String password;

    private Validator<Prietenie> validator;

    /**
     * constructor cu parametrii pentru repo
     * @param url-String
     * @param username-String
     * @param password-String
     * @param validator-Validator<Prietenie>
     */
    public PrietenieDBRepository(String url, String username, String password, Validator<Prietenie> validator)
    {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * Salvam o relatie de prietenie in baza noastra de date
     * @param entity-Prietenie
     * @return prietenia pe care o salvam
     * @throws IllegalArgumentException-nu va fi aruncata
     * @throws ValidatorException-prietenia nu este corecta ca si format
     * @throws EntityIsNull-entitatea este nula
     */
    @Override
    public Prietenie save(Prietenie entity) throws IllegalArgumentException, ValidatorException, EntityIsNull
    {
        String sql = "insert into prietenie(id, id_user1, id_user2) values(?, ?, ?)";
        try
        {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, entity.getID());
            ps.setInt(2, entity.getId_user1());
            ps.setInt(3, entity.getId_user2());
            Set<Prietenie> distinct_friendships =
                    StreamSupport.stream(findAll().spliterator(), false).collect(Collectors.toSet());
            ///vrem sa obtinem prieteniile distincte
            for(Prietenie friendship: distinct_friendships)
            {
                if(friendship.equals(entity))
                    return entity;///prietenia exista deja
            }
            validator.validate(entity);
            ps.executeUpdate();///inseram prietenia cu  valorile introduse dorite
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * Cautam o prietenie de un anumit ID, si daca o gasim o stergem(si aratam ce relatie de prietenie am sters)
     * @param integer-Integer
     * @return Prietenia de sters daca aceasta a fost gasita sau null in caz contrar
     * @throws EntityIsNull-entitatea de sters nu exista
     * @throws SQLException-apare o eroare SQL
     */
    @Override
    public Prietenie delete(Integer integer) throws EntityIsNull, SQLException {
        String sql = "DELETE FROM prietenie WHERE ID = ?";
        try
        {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            Prietenie prietenie_de_sters = findOne(integer);
            ps.setInt(1, integer);
            ps.executeUpdate();
            return prietenie_de_sters;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cautam o prietenie de un anumit id si o returnam daca o gasim, sau nu returnam nimic in caz contrar
     * @param integer-Integer
     * @return Prietenia de cautat daca am gasit o saul null in caz contrar
     */
    @Override
    public Prietenie findOne(Integer integer) {
        String sql = "SELECT * FROM PRIETENIE WHERE ID = ?";
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, integer);///incluim semnul intrebarii cu valoarea dorita, integer
            ResultSet result = ps.executeQuery();
            while(result.next())
            {
                if(result.getInt("id") == integer)///am ajuns la inrehistarea dorita(care este una singura)
                {
                    Prietenie prietenie = new Prietenie(1, 0, 0);
                    prietenie.setID(result.getInt("id"));
                    prietenie.setId_user1(result.getInt("id_user1"));
                    prietenie.setId_user2(result.getInt("id_user2"));
                    return prietenie;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Luam prieteniile una cate una pentru a vedea multitudinea de prietenii din baza noastra de date
     * @return prieteniile din baza noastra de date
     */
    @Override
    public Iterable<Prietenie> findAll() {
        SortedSet<Prietenie> prietenii = new TreeSet<>((x, y)->{return x.getID() - y.getID();});
        String sql = "SELECT * FROM prietenie ORDER BY id";
        try
        {
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet resultset = ps.executeQuery();

            while(resultset.next())
            {
                Integer id = resultset.getInt("id");
                Integer id_user1 = resultset.getInt("id_user1");
                Integer id_user2 = resultset.getInt("id_user2");
                prietenii.add(new Prietenie(id, id_user1, id_user2));
            }
            return prietenii;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return prietenii;
    }

    /**
     * Numaram relatiile de prietenia, una cate una, existente in baza de date pentru a obtine numarul total de prietenii
     * @return numarul de relatii de prietenie din relatia noastra de socializare
     */
    @Override
    public int size()
    {
        Iterable<Prietenie> friendships = this.findAll();
        List<Prietenie> prietenii = StreamSupport.stream(friendships.spliterator(), false).toList();
        return prietenii.size();
    }
}
