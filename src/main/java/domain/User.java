package domain;

public class User extends Entity<Integer>
{
    private String nume;
    private String prenume;
    private String oras;
    /**
     @param id-Innteger
     @param nume-string
     @param prenume-string
     @param oras -string
     *constructor cu parametrii
     */
    public User(Integer id, String nume, String prenume, String oras)
    {
        super(id);
        this.nume = nume;
        this.prenume = prenume;
        this.oras = oras;
    }
    /**
     * getter pt nume
     */
    public String getNume() {
        return nume;
    }
    /**
     * getter pt prenume
     */
    public String getPrenume() {
        return prenume;
    }

    /**
     * getter pt oras
     */
    public String getOras() {
        return oras;
    }

    /**
     @param nume-String
     *seteaza numele user ului
     */
    public void setNume(String nume) {
        this.nume = nume;
    }
    /**
     @param prenume-String
     *seteaza numele user ului
     */
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
    /**
     @param oras-String
     *seteaza orasului user ului
     */
    public void setOras(String oras) {
        this.oras = oras;
    }

    /**
     *
     * @return continutul obiectului sub forma de mesaj
     */
    @Override
    public String toString() {
        return "ID:" + this.getID() + ";"+ " Nume:" + this.nume + ";" + " Prenume:" + this.prenume + ";" + " Oras:" + this.oras;
    }
}
