package ui;

import domain.exceptions.EntityIsNull;
import domain.exceptions.EntityNotFound;
import domain.exceptions.ValidatorException;
import service.ForDataBase.ServiceFriendsDB;
import service.ForDataBase.ServiceUsersDB;

import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface
{
    /*
    NOI NU AVEM METODA SAVETOFILE(), DOAR LOAD(), DECI TOATE OPERATIILE CE LE FACEM NU VOR FI VIZIBILE IN FISIER
    DUPA EXECUTIA PROGRAMULUI
     */
    private ServiceUsersDB users;
    private ServiceFriendsDB prietenii;

    /**
     * constructor cu parametrii, unde initiaizam datele membre cu instante de obiecte ServiceUsers, respectiv ServicePrietenii
     * @param users-ServiceUsers
     * @param prietenii-ServicePrietenii
     */
    public UserInterface(ServiceUsersDB users, ServiceFriendsDB prietenii)
    {
        this.users = users;
        this.prietenii = prietenii;
    }
    /**
     * Permitem utilizatorului sa introduca un user
     */
    public static void menu()
    {
        System.out.println("0.Iesire din program");
        System.out.println("au.Afisare useri");
        System.out.println("ap.Afisare prietenii");
        System.out.println("1.Adaugare utilizator");
        System.out.println("2.Stergere utilizator");
        System.out.println("3.Adaugare prietenie");
        System.out.println("4.Stergere prietenie");
    }
    /**
     * Aplicatia ce utilizatorul o poate utiliza
     */
    public void run_menu()
    {
        menu();
        Scanner myObj = new Scanner(System.in);
        System.out.println("Introduceti optiunea dorita:");
        String command = myObj.next();
        while (!command.equals("0"))///cat timp nu iesim din pogram
        {
            try {
                switch (command)
                {
                    case "au" -> this.show_users();
                    case "ap" -> this.show_prietenii();
                    case "1" -> this.add_user();
                    case "2" -> this.delete_user();
                    case "3" -> this.add_prietenie();
                    case "4" -> this.delete_prietenie();
                    default -> {
                    }
                }
//                menu();
//                System.out.println("Introduceti optiunea dorita:");
//                command = myObj.next();
            }
            catch(ValidatorException e)
            {
                System.err.println("Eroare la validare: " + e.getMessage());
            }
            catch(EntityIsNull e)
            {
                System.err.println("Entitate de tip nul: " + e.getMessage());
            }
            catch(EntityNotFound e)
            {
                System.err.println("Entitate negasita: " + e.getMessage());
            }
            catch(Exception e)
            {
                System.err.println("Eroare: " + e.getMessage());
                e.printStackTrace();
            }
            finally{
                menu();
                System.out.println("Introduceti optiunea dorita:");
                command = myObj.next();
            }
        }
    }
    /**
     * Permitem utilizatorului sa introduca un user
     */
    public void add_user() throws ValidatorException, EntityIsNull {
            ///streamer pentru date de intrare, mai precis pentru citire date de la tastatura
            Scanner myobj = new Scanner(System.in);
            System.out.print("Dati id-ul utilizatorului aici:");
            int id = myobj.nextInt();
            System.out.print("Dati numele utilizatorului aici:");
            String nume = myobj.next();
            System.out.print("Dati prenumele utilizatorului aici:");
            String prenume = myobj.next();
            System.out.print("Dati orasul utilizatorului aici:");
            String oras = myobj.next();
            this.users.add_user(id, nume, prenume, oras);
        }

    /**
     *Permitem utilizatorului sa stearga un user dorit
     * @throws IllegalArgumentException -
     * @throws EntityIsNull
     * @throws EntityNotFound
     */
    public void delete_user() throws IllegalArgumentException, EntityIsNull, EntityNotFound, SQLException {
            Scanner myObj = new Scanner(System.in);
            System.out.print("Dati id-ul utilizatorului aici:");
            int id = myObj.nextInt();
            this.users.delete_user(id);
    }
    /**
     * Permitem utilizatorului sa creeze o relatie de prietenie dorita
     */
    public void add_prietenie() throws ValidatorException, IllegalArgumentException, EntityIsNull {
            Scanner myobj = new Scanner(System.in);
            System.out.print("Dati id-ul prieteniei aici:");
            int id = myobj.nextInt();
            System.out.print("Dati id-ul primului utilizator aici:");
            int id_user1 = myobj.nextInt();
            System.out.print("Dati id-ul celui de al doilea utilizator aici:");
            int id_user2 = myobj.nextInt();
            this.prietenii.add_prietenie(id, id_user1, id_user2);
    }

    /**
     * Permitem utilizatorului sa stearga o relatie de prietenie dorita
     */
    public void delete_prietenie() throws EntityIsNull, EntityNotFound, SQLException {

            Scanner myObj = new Scanner(System.in);
            System.out.print("Dati id-ul prieteniei aici:");
            int id = myObj.nextInt();
            this.prietenii.delete_prietenie(id);
    }

    /**
     * Afisam toti utilizatorii din momentul curent
     */
    public void show_users()
    {
        this.users.findAll().forEach((x)-> System.out.println(x.toString()));
    }
    /**
     * Afisam toti prietenii din momentul curent
     */
    public void show_prietenii()
    {
        this.prietenii.findAll().forEach((x)-> System.out.println(x.toString()));
    }
}
