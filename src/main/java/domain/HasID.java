package domain;

///metodele implicite pentru o clasa care are ID
public interface HasID<ID>
{
    /**
     *
     * @return Id ul unei entitati
     */
    ID getID();

    /**
     * setam id ul ueni entitati
     * @param id-ID
     */
    void setID(ID id);
}
