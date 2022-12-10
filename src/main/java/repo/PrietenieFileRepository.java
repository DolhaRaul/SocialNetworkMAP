package repo;

import domain.Prietenie;
import domain.validators.EntityIsNull;
import domain.validators.Validator;
import domain.validators.ValidatorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class PrietenieFileRepository extends AbstractRepo<Integer, Prietenie>
{
    private String filename;

    /**
     * constructor cu parametrii
     * @param validator-Validator
     * @param filename-String
     */
    public PrietenieFileRepository(Validator<Prietenie> validator, String filename) throws ValidatorException, EntityIsNull {
        super(validator);
        this.filename = filename;
        load();
    }

    /**
     * Aduagam o prietenie dorita de utilizator, cu conditia ca ID ul ei sa fie unic si acea sa nu fie o relatie de
     * prietenie deja existenta(se formeaza intre utilizatori diferiti)
     * @param relatie-Prietenie
     * @return prietenia adaugata
     * @throws IllegalArgumentException -nu e cazul
     * @throws ValidatorException -in cazul in care prietenia de adaugat nu e corecta ca si format
     * @throws EntityIsNull -in cazul in care prietenia nu e vida(nu are parametrii completati)
     */
    @Override
    public Prietenie save(Prietenie relatie) throws IllegalArgumentException, ValidatorException, EntityIsNull
    {
//        for(Prietenie enntity: this.findAll())
//        {
//            if(relatie.equals(enntity))
//                return relatie;
//        }
        for(Map.Entry<Integer, Prietenie> entry: items.entrySet())
        {
            if(relatie.equals(entry.getValue()))
                return relatie;///exista deja prietenia pe care vrem sa o adaugam
        }
        return super.save(relatie);
    }
    /**
     * incarcam toate datele datele din fisierul "filename" in colectiile noastre
     * @throws ValidatorException-aruncam exceptie cand fisierul "este corupt"(nu putem extrage datele corespunzator)
     */
    public void load() throws ValidatorException, EntityIsNull {
        try {
            Path path = Paths.get(filename);
            List<String> lines = Files.readAllLines(path);///
            for (String linie : lines) {
                String[] words = linie.split(";");
                if (words.length != 3)///nu avem 3 string uri, unul corespunzator pentru fiecare data membra
                    throw new IOException("Fisierul este corupt!");
                /*
                Daca primim in fisier un sir de forma, de ex:3;3;4 4, atunci vom avea 3 string uri, dar ultimul va pusca
                pe metoda Integer.parseint() si nu avem ce ii face
                 */
                Prietenie relationship = new Prietenie(Integer.parseInt(words[0]), Integer.parseInt(words[1]),
                        Integer.parseInt(words[2]));
                super.save(relationship);///salvam entitatea in buffer ul din PrietenieFileRepository(care e de tip HashMap)
            }
        }
        catch(IOException e)
        {
            System.err.println("Eroare la citirea fisierului!!!");
        }
    }
}
