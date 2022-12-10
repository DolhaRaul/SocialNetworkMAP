package repo;

import domain.User;
import domain.validators.EntityIsNull;
import domain.validators.Validator;
import domain.validators.ValidatorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UserFileRepository extends AbstractRepo<Integer, User>
{
    private String filename;

    /**
     *constructor cu parametrii; cand pelam constructorul, automat atunci si incarcam datele din fisier
     * @param validator-Validator
     * @param filename-String
     */
    public UserFileRepository(Validator<User> validator, String filename) throws ValidatorException, EntityIsNull {
        super(validator);
        this.filename = filename;
        load();
    }

    /**
     * salvam datele din fisier in repo ul de useri
     * @throws ValidatorException -cand fisierul este corupt(nu putem extrage datele corespunzator)
     */
    public void load() throws ValidatorException, EntityIsNull {
        try {
            Path path = Paths.get(filename);///obtinem calea acestui fisier, "path ul" sau
            List<String> lines = Files.readAllLines(path);///citim oate liniile din fisier
            for (String linie : lines)///parcurgem liniile una cate una
            {
                String[] words = linie.split(";");///consideram datele membre separate prin ";"
                if (words.length != 4)///nu avem 4 cate cuvinte, cate unul pentru fiecare data membra a user ului
                    throw new IOException("Fisierul este corupt!");
                /*
                desi exceptia IOException nu apare in definitia metodei load(), o putem arunca fiindca ne aflam in interiorul
                unui bloc try-catch CARE TRATAEAZA ACESTI TIP DE EXCEPTIE!!(Am vzt asta la Curs3-MAP-12/54)
                 */
                User user = new User(Integer.parseInt(words[0]),
                        words[1], words[2], words[3]);
                super.save(user);
            }
        }
        catch(IOException e)
        {
            System.err.println("Eroare la citirea fisierului!");
            e.printStackTrace();
        }
    }
}
