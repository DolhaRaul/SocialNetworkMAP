package domain;

import repo.MessageDBRepository;

import java.time.LocalDateTime;
import java.util.Date;

public class Message extends Entity<Integer>
{
    public static int generated_id = 0;///contor pentru numarul de mesaje
    public int id_sender;
    public int id_receiver;
    String continut;

    LocalDateTime data;

    /**
     * constructor implicit, util cand avem nevoie de obiecte "initial goale"
     */
    public Message()
    {

    }

    /**
     * @param user1-id ul primului user,cel care trimite mesajul
     * @param user2-id ul celui de al doilea user, cel care primeste mesajul
     * @param text-continutul mesajului
     */
    public Message(int user1, int user2, String text)
    {
        super(generated_id + 1);
        generated_id++;
        this.id_sender = user1;
        this.id_receiver = user2;
        this.continut = text;
        this.data = LocalDateTime.now();///mesajul se considera trimis EXACT cand va fi apasat un buton "send"
    }

    /**
     * getter pt id ul sender ului
     * @return id ul userului ce trimite mesajul
     */
    public int getId_sender() {
        return id_sender;
    }

    /**
     * getter pt id ul receiver ului
     * @return id ul userului ce primeste mesajul
     */
    public int getId_receiver() {
        return id_receiver;
    }

    /**
     *
     * @return continutul mesajului
     */
    public String getContinut()
    {
        return this.continut;
    }

    /**
     *
     * @return data in care mesajul a fost trimis
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     * @return obtinem ziua cand a fost trimism esajul
     */
    public int getZi()
    {
        return this.data.getDayOfMonth();
    }

    /**
     * @return luna cand a fost trimis mesajul
     */
    public int getLuna()
    {
        return this.data.getMonthValue();
    }

    /**
     * @return anul cand a fost trimis mesajul
     */
    public int getAn()
    {
        return this.data.getYear();
    }

    /**
     *
     * @return ora cand a fost trimism mesajul
     */
    public int getOra()
    {
        return this.data.getHour();
    }

    /**
     *
     * @return minutul cand a fost trimis mesajul
     */
    public int getMinut()
    {
        return this.data.getMinute();
    }

    /**
     *
     * @param id_sender id ul celui ce trimite mesajul
     */
    public void setId_sender(int id_sender) {
        this.id_sender = id_sender;
    }

    /**
     *
     * @param id_receiver id ul celui ce primeste mesajul
     */
    public void setId_receiver(int id_receiver) {
        this.id_receiver = id_receiver;
    }

    /**
     * schimbam continutul mesajului
     * @param continut-noul continut al mesajului
     */
    public void setContinut(String continut)
    {
        this.continut = continut;
    }

    /**
     * setter pentru data(ca sa modificam ceva din Data si Zi)
     * @param data-Date
     */

    /**
     * Setam data cand a fost trimis mesajul
     * @param data-LocalDateTime
     */
    public void setData(LocalDateTime data)
    {
        this.data = data;
    }

    /**
     *
     * @return continutul mesajului(aceasta va fi reprezentarea mesajului sub forma de string)
     */
    @Override
    public String toString()
    {
        return this.continut;
    }
}
