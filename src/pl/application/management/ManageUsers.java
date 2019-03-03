package pl.application.management;

import org.apache.commons.lang3.ArrayUtils;
import pl.application.models.User;
import pl.application.models.UserGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class ManageUsers {

    public static void ManageUsers(Connection conn) throws SQLException {
        printUsers(conn);
        System.out.println("\nEdycja użytkowników. Wybierz jedną z opcji: \n *add (dodanie), \n *edit (modyfikacja), " +
                "\n *delete (usunięcie), \n *quit (koniec programu).");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (!"quit".equals(command)) {
            if ("add".equals(command)) {
                System.out.println("\nDodawanie nowego użytkownika");
                User userAdd = new User();
                System.out.println("\nPodaj imię użytkownika");
                userAdd.setUsername(scanner.nextLine());
                userAdd.setEmail(getUniqueEmail(conn,scanner));
                System.out.println("\nPodaj hasło");
                userAdd.setPassword(scanner.nextLine());
                userAdd.setUserGroup(UserGroup.loadGroupById(conn, getGroupId(conn,scanner)));
                userAdd.saveToDB(conn);
                System.out.println("Użytkowanik został zapisany :)");
                printUsers(conn);
            } else if ("edit".equals(command)) {
                System.out.println("\nEdycja danych użytkownika.");
                int idUserEdit = getUserId(conn,scanner);
                User userEdit = User.loadUserById(conn, idUserEdit);
                System.out.println("\nPodaj nowe imię użytkownika");
                userEdit.setUsername(scanner.nextLine());
                userEdit.setEmail(getUniqueEmail(conn,scanner));
                System.out.println("\nPodaj hasło");
                userEdit.setPassword(scanner.nextLine());
                userEdit.setUserGroup(UserGroup.loadGroupById(conn, getGroupId(conn,scanner)));
                userEdit.saveToDB(conn);
                System.out.println("Użytkowanik został zmodyfikowany :)");
                printUsers(conn);
            } else if ("delete".equals(command)) {
                System.out.println("\nUsuwanie użytkownika.");
                int idUserDelete = getUserId(conn,scanner);
                User userDelete = User.loadUserById(conn, idUserDelete);
                userDelete.delete(conn);
                System.out.println("Użytkowanik został usunięty :)");
                printUsers(conn);
            } else {
                System.out.println("\nError: Nieprawidłowa komenda - spróbuj jeszcze raz!");
            }
            System.out.println("\nEdycja użytkowników. Wybierz jedną z opcji: \n *add (dodanie), \n *edit (modyfikacja), " +
                    "\n *delete (usunięcie), \n *quit (koniec programu).");
            command = scanner.nextLine();
        }
        System.out.println("\nZakończono edycję użytkowników");
    }


    public static void printUsers(Connection conn) throws SQLException {
        System.out.println("\nLista użytkowników:");
        String sql = "select * from users";
        PreparedStatement printstat = conn.prepareStatement(sql);
        ResultSet resultSet = printstat.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            int groupid = resultSet.getInt(5);
            System.out.println(" -User " + id + ", " + name + ", " + email + ", Grupa " + groupid);
        }
    }

    private static int getId(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("\nError: Podaj wartość id jako liczbę całkowitą!");
            scanner.nextLine();
        }
        return scanner.nextInt();
    }

    private static int getUserId(Connection conn, Scanner scanner) throws SQLException {
        User[] users = User.loadAllUsers(conn);
        int[] usersIds = new int[users.length];
        for (int i = 0; i < usersIds.length; i++) {
            usersIds[i] = users[i].getId();
        }
        System.out.println("\nPodaj id użytkownika:");
        int userID = getId(scanner);
        scanner.nextLine();

        while (!ArrayUtils.contains(usersIds, userID)) {
            System.out.println("\nError: Podano id użytkownika nieistniejącego w systemie. Spróbuj jeszcze raz!");
            System.out.println("Dostępne numery id użytkowników to : " + Arrays.toString(usersIds).replace("[", "").replace("]", ""));
            userID = getId(scanner);
            scanner.nextLine();
        }
        return userID;
    }

    private static int getGroupId(Connection conn, Scanner scanner) throws SQLException {
        UserGroup[] userGroups = UserGroup.loadAllGroups(conn);
        int[] userGroupsIds = new int[userGroups.length];
        for (int i = 0; i < userGroupsIds.length; i++) {
            userGroupsIds[i] = userGroups[i].getId();
        }
        System.out.println("\nPodaj id grupy:");
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

    private static String getUniqueEmail(Connection conn, Scanner scanner) throws SQLException {
        User[] users = User.loadAllUsers(conn);
        String[] usersEmails = new String[users.length];
        for (int i = 0; i < usersEmails.length; i++) {
            usersEmails[i] = users[i].getEmail();
        }
        System.out.println("\nPodaj email użytkownika:");
        String userEmail = scanner.nextLine();
        while (ArrayUtils.contains(usersEmails, userEmail)) {
            System.out.println("\nError: Podano email, który już istnieje w systemie. Spróbuj jeszcze raz!");
            userEmail = scanner.nextLine();
        }
        return userEmail;
    }

}

