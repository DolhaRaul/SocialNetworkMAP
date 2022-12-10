package domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable
{
    private final static long serialVersionUID = 11122L;
    protected ID id;

    /**
     * constructor cu parametrii
     * @param id-ID
     */
    public Entity(ID id)
    {
        this.id = id;
    }

    /**
     * Obtinem id-ul(identificatorul) unei entitati
     * @return id ul entitatii de tip ID
     */
    public ID getID() {
        return id;
    }

    /**
     * Setam ID ul unei entitati sa fie altul dorit
     * @param id-ID
     */
    public void setID(ID id) {
        this.id = id;
    }
}
