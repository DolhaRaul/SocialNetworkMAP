package domain;

import java.time.LocalDateTime;
import java.util.Date;

public class Prietenie extends Entity<Integer>
{
    private int id_user1;
    private int id_user2;

    private LocalDateTime date;

    private PrietenieState state;

    /**
     * constructor cu parametrii pentru o relatie de prietenie
     * @param id_prietenie-Integer
     * @param id_user1-Integer
     * @param id_user2-Integer
     */
    public Prietenie(Integer id_prietenie, Integer id_user1, Integer id_user2)
    {
        super(id_prietenie);
        this.id_user1 = id_user1;
        this.id_user2 = id_user2;
        state = PrietenieState.Accepted;
        date = LocalDateTime.now();
    }

    /**
     * un nou constructor cu paaremtrii pentru Prietenie
     * @param id_prietenie-Integer
     * @param id_user1-Integer
     * @param id_user2-Integer
     * @param date-LocalDateTime
     * @param state-PrietenieState
     */
    public Prietenie(Integer id_prietenie, Integer id_user1, Integer id_user2, LocalDateTime date, PrietenieState state)
    {
        super(id_prietenie);
        this.id_user1 = id_user1;
        this.id_user2 = id_user2;
        this.date = date;
        this.state = state;
    }

    public PrietenieState getState() {
        return state;
    }

    public void setState(PrietenieState state) {
        this.state = state;
    }

    /**
     * setam id ul primului utilizator
     * @param id_user1-Integer
     */
    public void setId_user1(int id_user1) {
        this.id_user1 = id_user1;
    }

    /**
     * setam id ul primului utilizator(care este de fapt id ul Sender ului)
     * @param SenderId-Integer
     */
    public void setSenderId(int SenderId)
    {
        id_user1 = SenderId;
    }
    /**
     * setam id ul celui de al doilea utilizator
     * @param id_user2-Integer
     */
    public void setId_user2(int id_user2) {
        this.id_user2 = id_user2;
    }

    /**
     * setam id ul pt al doilea utilizator(care este de fapt id ul Receiver ului)
     * @param ReceiverId-Integer
     */
    public void setReceiverId(int ReceiverId)
    {
        this.id_user2 = ReceiverId;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * verificam daca 2 prietenii sunt una si aceeasi(daca ea se formeaza intre aceeasi utilizatori)
     * @param obj-Object
     * @return true daca avem 2 prietenii egale sau false in caz contrar
     */
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Prietenie cur))
            return false;
        if(this.id.equals(cur.id))
            return true;///daca au unul si acelasi id e clar
        return (this.id_user1 == cur.id_user1 && this.id_user2 == cur.id_user2) ||
                (this.id_user1 == cur.id_user2 && this.id_user2 == cur.id_user1);
    }

    /**
     @return id-ul primului utilizator
     */
    public int getId_user1() {
        return id_user1;
    }

    /**
     *
     * @return are acelasi rol ca si getter ul de mai sus, Primul User il vom privi ca si Sender(
     * aceste functii le vom folosi cand cautam prieteniii, etc) pentru a intelege contexul mai bine
     */
    public int getSenderId()
    {
        return id_user1;
    }

    /**
     * @return id ul celui de al doilea user
     */
    public int getId_user2() {
        return id_user2;
    }

    /**
     * @return data prieteniei(cand s-a format/trimis/refuzat prietenia_
     */

    /**
     * @return are acelasi rol ca si getter ul de mai sus, Al doilea User il vom privi ca si Receiver(
     * aceste functii le vom folosi cand cautam prieteniii, etc) pentru a intelege contexul mai bine
     */
    public int getReceiverId()
    {
        return id_user2;
    }
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString()
    {
        return "ID_pritenie:" + this.id + "; " + "ID_user1:" + this.id_user1 + "; " + "ID_user2:" + this.id_user2;
    }

}
