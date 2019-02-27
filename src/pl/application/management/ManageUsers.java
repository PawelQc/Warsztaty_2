package pl.application.management;

import pl.application.models.User;
import pl.application.models.UserGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ManageUsers {

    public static void ManageUsers(Connection conn) throws SQLException {
        printUsers(conn);
        System.out.println();
        System.out.println("Edycja użytkowników. Wybierz jedną z opcji: add (dodanie), edit (modyfikacja), " +
                "delete (usunięcie), quit (koniec programu).");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (!"quit".equals(command)) {
            if ("add".equals(command)) {
                User userAdd = new User();
                System.out.println("Podaj imię użytkownika");
                userAdd.setUsername(scanner.nextLine());
                System.out.println("Podaj email");
                userAdd.setEmail(scanner.nextLine());
                System.out.println("Podaj hasło");
                userAdd.setPassword(scanner.nextLine());
                System.out.println("Podaj id grupy");
                userAdd.setUserGroup(UserGroup.loadGroupById(conn, scanner.nextInt()));
                scanner.nextLine();
                userAdd.saveToDB(conn);
                System.out.println("Użytkowanik został zapisany :)");
                printUsers(conn);
            } else if ("edit".equals(command)) {
                System.out.println("Podaj id użytkownika, którego dane będą edytowane.");
                int idUserEdit = getId(scanner);
                scanner.nextLine();
                User userEdit = User.loadUserById(conn, idUserEdit);
                System.out.println("Podaj nowe imię użytkownika");
                userEdit.setUsername(scanner.nextLine());
                System.out.println("Podaj email");
                userEdit.setEmail(scanner.nextLine());
                System.out.println("Podaj hasło");
                userEdit.setPassword(scanner.nextLine());
                System.out.println("Podaj id grupy");
                userEdit.setUserGroup(UserGroup.loadGroupById(conn, scanner.nextInt()));
                scanner.nextLine();
                userEdit.saveToDB(conn);
                System.out.println("Użytkowanik został zmodyfikowany :)");
                printUsers(conn);
            } else if ("delete".equals(command)) {
                System.out.println("Podaj id użytkownika, którego dane będą usuwane.");
                int idUserDelete = getId(scanner);
                scanner.nextLine();
                User userDelete = User.loadUserById(conn, idUserDelete);
                userDelete.delete(conn);
                System.out.println("Użytkowanik został usunięty :)");
                printUsers(conn);
            } else {
                System.out.println("Nieprawidłowa komenda - spróbuj jeszcze raz!");
            }

            System.out.println();
            System.out.println("Edycja użytkowników. Wybierz jedną z opcji: add (dodanie), edit (modyfikacja), " +
                    "delete (usunięcie), quit (koniec programu).");
            command = scanner.nextLine();
        }
        System.out.println("Zakończono edycję użytkowników");
    }


    public static void printUsers(Connection conn) throws SQLException {
        String sql = "select * from users";
        PreparedStatement printstat = conn.prepareStatement(sql);
        ResultSet resultSet = printstat.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            String password = resultSet.getString(4);
            int groupid = resultSet.getInt(5);
            System.out.println("User " + id + ", " + name + ", " + email + ", " + password + ", Grupa " + groupid);
        }
    }

    private static int getId(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Podaj wartość id jako liczbę całkowitą we wskazanym powyżej zakresie");
            scanner.nextLine();
        }
        return scanner.nextInt();
    }

}

