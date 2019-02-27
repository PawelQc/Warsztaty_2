package pl.application.management;

import pl.application.models.UserGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ManageGroups {

    public static void ManageGroups(Connection conn) throws SQLException {
        printGroups(conn);
        System.out.println();
        System.out.println("Edycja grup. Wybierz jedną z opcji: add (dodanie), edit (modyfikacja), " +
                "delete (usunięcie), quit (koniec programu).");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (!"quit".equals(command)) {
            if ("add".equals(command)) {
                UserGroup groupAdd = new UserGroup();
                System.out.println("Podaj nazwę grupy");
                groupAdd.setName(scanner.nextLine());
                groupAdd.saveGroupToDB(conn);
                System.out.println("Grupa została zapisana :)");
                printGroups(conn);
            }
            else if ("edit".equals(command)) {
                System.out.println("Podaj id grupy, której dane będą edytowane.");
                int idGroupEdit = getId(scanner);
                scanner.nextLine();
                UserGroup groupEdit = UserGroup.loadGroupById(conn, idGroupEdit);
                System.out.println("Podaj nową nazwę grupy");
                groupEdit.setName(scanner.nextLine());
                groupEdit.saveGroupToDB(conn);
                System.out.println("Grupa została zmodyfikowana :)");
                printGroups(conn);
            }
            else if ("delete".equals(command)) {
                System.out.println("Podaj id grupy, którą chcesz usunąć.");
                int idGroupDelete = getId(scanner);
                scanner.nextLine();
                UserGroup groupDelete = UserGroup.loadGroupById(conn, idGroupDelete);
                groupDelete.deleteGroup(conn);
                System.out.println("Grupę usunięto :)");
                printGroups(conn);
            } else {
                System.out.println("Nieprawidłowa komenda - spróbuj jeszcze raz!");
            }

            System.out.println();
            System.out.println("Edycja grup. Wybierz jedną z opcji: add (dodanie), edit (modyfikacja), " +
                    "delete (usunięcie), quit (koniec programu).");
            command = scanner.nextLine();
        }
        System.out.println("Zakończono edycję grup");
    }


    public static void printGroups(Connection conn) throws SQLException {
        String sql = "select * from user_group";
        PreparedStatement printstat = conn.prepareStatement(sql);
        ResultSet resultSet = printstat.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            System.out.println(id + " | " + name);
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
