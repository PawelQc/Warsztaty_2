package pl.application.management;

import org.apache.commons.lang3.ArrayUtils;
import pl.application.models.UserGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class ManageGroups {

    public static void ManageGroups(Connection conn) throws SQLException {
        printGroups(conn);
        System.out.println("\nEdycja grup. Wybierz jedną z opcji: \n *add (dodanie), \n *edit (modyfikacja), " +
                "\n *delete (usunięcie), \n *quit (koniec programu).");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (!"quit".equals(command)) {
            if ("add".equals(command)) {
                UserGroup groupAdd = new UserGroup();
                System.out.println("\nPodaj nazwę grupy");
                groupAdd.setName(scanner.nextLine());
                groupAdd.saveGroupToDB(conn);
                System.out.println("\nGrupa została zapisana :)");
                printGroups(conn);
            }
            else if ("edit".equals(command)) {
                System.out.println("\nPodaj id grupy, której dane będą edytowane.");
                int idGroupEdit = getGroupId(conn,scanner);
                UserGroup groupEdit = UserGroup.loadGroupById(conn, idGroupEdit);
                System.out.println("\nPodaj nową nazwę grupy");
                groupEdit.setName(scanner.nextLine());
                groupEdit.saveGroupToDB(conn);
                System.out.println("Grupa została zmodyfikowana :)");
                printGroups(conn);
            }
            else if ("delete".equals(command)) {
                System.out.println("\nPodaj id grupy, którą chcesz usunąć.");
                int idGroupDelete = getGroupId(conn,scanner);
                UserGroup groupDelete = UserGroup.loadGroupById(conn, idGroupDelete);
                groupDelete.deleteGroup(conn);
                System.out.println("Grupę usunięto :)");
                printGroups(conn);
            } else {
                System.out.println("\nError: Nieprawidłowa komenda - spróbuj jeszcze raz!");
            }

            System.out.println();
            System.out.println("\nEdycja grup. Wybierz jedną z opcji: \n *add (dodanie), \n *edit (modyfikacja), " +
                    "\n *delete (usunięcie), \n *quit (koniec programu).");
            command = scanner.nextLine();
        }
        System.out.println("Zakończono edycję grup");
    }


    public static void printGroups(Connection conn) throws SQLException {
        System.out.println("\nLista utworzonych grup:");
        String sql = "select * from user_group";
        PreparedStatement printstat = conn.prepareStatement(sql);
        ResultSet resultSet = printstat.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            System.out.println(" " + id + " | " + name);
        }
    }

    private static int getId(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("\nError: Podaj wartość id jako liczbę całkowitą!");
            scanner.nextLine();
        }
        return scanner.nextInt();
    }

    private static int getGroupId(Connection conn, Scanner scanner) throws SQLException {
        UserGroup[] userGroups = UserGroup.loadAllGroups(conn);
        int[] userGroupsIds = new int[userGroups.length];
        for (int i = 0; i < userGroupsIds.length; i++) {
            userGroupsIds[i] = userGroups[i].getId();
        }
        int groupID = getId(scanner);
        scanner.nextLine();

        while (!ArrayUtils.contains(userGroupsIds, groupID)) {
            System.out.println("\nError: Podano id grupy nieistniejącej w systemie. Spróbuj jeszcze raz!");
            System.out.println("Dostępne numery id grup to : " + Arrays.toString(userGroupsIds).replace("[", "").replace("]", ""));
            groupID = getId(scanner);
            scanner.nextLine();
        }
        return groupID;
    }

}
