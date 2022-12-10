package run;

import config.Config;

public class TestRunner {
    public static void main(String[] args) {
        Config config = new Config();
        config.getProperties();
        try {
            System.out.println("Teste repo useri:");
            Tests.test_repo_users();
            System.out.println();
            System.out.println("Teste repo prietenii:");
            Tests.test_repo_friendships();
            System.out.println();
            System.out.println("Teste in memory repositories:");
            Tests.test_in_memories_repos();
            System.out.println("Tesetele pentru in memory repositories sunt bune!");
            System.out.println("Test service useri:");
            Tests.test_service_users();
            System.out.println("Tesetele pentru service useri sunt bune!");
            System.out.println("Teste service prietenii:");
            Tests.test_service_prietenii();
            System.out.println("Tesetele pentru service useri sunt bune!");
            System.out.println("Teste pentru DatBase Repository users:");
            Tests.test_database_repo_users();
            System.out.println("Testele pentru DatBase Repository users sunt bune!");
            System.out.println("Testele pentru DatBase Repository prietenii:");
            Tests.test_database_repo_prietenii();
            System.out.println("Testele pentru DatBase Repository prietenii sunt bune!");
        } catch (Exception e) {
            System.err.println("Fisier corupt sau eroare la validare!");
            e.printStackTrace();
        }
    }
}
