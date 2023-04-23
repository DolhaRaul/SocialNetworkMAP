package repo;

import domain.Message;
import domain.exceptions.EntityIsNull;
import domain.exceptions.ValidatorException;
import domain.validators.Validator;
import utils.Utils;

import java.net.ConnectException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class MessageDBRepository implements Repository<Integer, Message>
{
    final private String url;

    final private String username;

    final private String password;

    final private Validator<Message> validator;

    /**
     * construcor cu parametrii pt repo
     * @param url-String
     * @param username-String
     * @param password-String
     * @param validator-String
     */
    public MessageDBRepository(String url, String username, String password, Validator<Message> validator)
    {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     *
     * @param mesaj-Message
     * @return mesaju salvat
     * @throws IllegalArgumentException-nu se arunca
     * @throws ValidatorException-in cazul in care formatul mesajului nu este unul corect
     * @throws EntityIsNull-in cazul in care mesajul este null
     */
    @Override
    public Message save(Message mesaj) throws IllegalArgumentException, ValidatorException, EntityIsNull {
        String sql = "insert into mesaje(id, id_sender, id_receiver, continut, date, time) values(?, ?, ?, ?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)
            )
        {
            ps.setInt(1, mesaj.getID());
            ps.setInt(2, mesaj.getId_sender());
            ps.setInt(3, mesaj.getId_receiver());
            ps.setString(4, mesaj.getContinut());
            ps.setDate(5, Date.valueOf(mesaj.getData().toLocalDate()));
            ps.setTime(6, Time.valueOf(mesaj.getData().toLocalTime()));
            ///inainte de a salva mesajul, ne asiguram ca este in format corect
            validator.validate(mesaj);
            ps.executeUpdate();///executam query ul(inseram mesajul, cu datele dorite)
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return mesaj;
    }

    @Override
    public Message delete(Integer integer) throws EntityIsNull, SQLException {
        String sql = "DELETE  FROM mesaje WHERE ID = ?";
        try(Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)
        )
        {
            Message to_delete_mesaj = findOne(integer);///gasim mesajul de sters
            ps.setInt(1, integer);
            ps.executeUpdate();///il stergem
            return  to_delete_mesaj;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message findOne(Integer integer)
    {
        String sql = "SELECT * FROM mesaje WHERE ID = ?";
        try(Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, integer);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next())
            {
                ///am gasit mesajul dorit
                if(resultSet.getInt("id") == integer)
                {
                    int id_sender = resultSet.getInt("id_sender");
                    int id_receiver = resultSet.getInt("id_receiver");
                    String continut = resultSet.getString("continut");
                    Date data = resultSet.getDate("date");
                    Time timp = resultSet.getTime("time");
                    ///OBS!!! CAND CREEM ACEST OBIECT, NU VOM OBTINE DATA CAND A FOST TRIMIS, CI DATA
                    ///DE ACM!!!PENTRU CA SE INITIALIZEAZA DIRECT IN CONSTRUCTOR DATA MEMBRA LOCALDATETIME
                    Message mesaj_dorit= new Message();
                    mesaj_dorit.setID(integer);
                    mesaj_dorit.setId_sender(id_sender);
                    mesaj_dorit.setId_receiver(id_receiver);
                    mesaj_dorit.setContinut(continut);
                    mesaj_dorit.setData(Utils.creeazaDataTImp(data, timp));
                    return mesaj_dorit;///am gasit mesajul dorit
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        String sql = "SELECT * FROM mesaje";
        try(Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)
        )
        {
            List<Message> mesaje_curente = new ArrayList<>();
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                int id_sender = resultSet.getInt("id_sender");
                int id_receiver = resultSet.getInt("id_receiver");
                String continut = resultSet.getString("continut");
                Date data = resultSet.getDate("date");
                Time timp =  resultSet.getTime("time");
                ///OBS!!! CAND CREEM ACEST OBIECT, NU VOM OBTINE DATA CAND A FOST TRIMIS, CI DATA
                ///DE ACM!!!PENTRU CA SE INITIALIZEAZA DIRECT IN CONSTRUCTOR DATA MEMBRA LOCALDATETIME
                Message mesaj_dorit = new Message();///avem nevoie de un astfel de constructor, pt
                                        ///a seta id ul(generat automat), data si timpul
                mesaj_dorit.setID(id);
                mesaj_dorit.setId_sender(id_sender);
                mesaj_dorit.setId_receiver(id_receiver);
                mesaj_dorit.setContinut(continut);
                mesaj_dorit.setData(Utils.creeazaDataTImp(data, timp));
                mesaje_curente.add(mesaj_dorit);
            }
            return mesaje_curente;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public int size() {
        return Utils.toStream(this.findAll()).toList().size();
    }
}
