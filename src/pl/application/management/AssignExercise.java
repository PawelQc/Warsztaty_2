package pl.application.management;

import org.apache.commons.lang3.ArrayUtils;
import pl.application.models.*;

import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

public class AssignExercise {

    public static void AssignExercise(Connection conn) throws SQLException {
        System.out.println("\nPrzypisywanie zadań. Wybierz jedną z opcji: \n *add (przypisanie zadania do użytkownika), " +
                "\n *view (przeglądanie zadań przypisanych do danego użytkownika), \n *quit (koniec programu).");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (!"quit".equals(command)) {
            if ("add".equals(command)) {
                ManageUsers.printUsers(conn);
                System.out.println("\nPodaj id użytkownika");
                int idUser = getUserId(conn,scanner);
                User user = User.loadUserById(conn, idUser);
                ManageExercise.printExercise(conn);
                System.out.println("\nPodaj id zadania");
                int idExercise = getExerciseId(conn,scanner);
                Exercise exercise = Exercise.loadExerciseById(conn, idExercise);
                Timestamp date = new Timestamp(Calendar.getInstance().getTime().getTime());
                Solution solution = new Solution(null, exercise, user, date, null);
                solution.saveSolutionToDB(conn);
                System.out.println("Zadanie przypisano do użytkownika! :)");
            } else if ("view".equals(command)) {
                System.out.println("\nPodaj id użytkownika w celu wyświetlenia przypisanych zadań.");
                int idUser = getUserId(conn,scanner);
                Solution[] solutions = Solution.loadAllSolutionsByUserId(conn, idUser);
                System.out.println(" Przypisane zadania do użytkownika nr " + idUser + ":");
                for (Solution solution : solutions) {
                    System.out.println("  -" + solution.toString());
                }
            } else {
                System.out.println("\nError: Nieprawidłowa komenda - spróbuj jeszcze raz!");
            }
            System.out.println("\nPrzypisywanie zadań. Wybierz jedną z opcji: \n *add (przypisanie zadania do użytkownika), " +
                    "\n *view (przeglądanie zadań przypisanych do danego użytkownika), \n *quit (koniec programu).");
            command = scanner.nextLine();
        }
        System.out.println("\nKoniec działania programu.");
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

    private static int getExerciseId(Connection conn, Scanner scanner) throws SQLException {
        Exercise[] exercises = Exercise.loadAllExercises(conn);
        int[] exIds = new int[exercises.length];
        for (int i = 0; i < exIds.length; i++) {
            exIds[i] = exercises[i].getId();
        }
        int exID = getId(scanner);
        scanner.nextLine();
        while (!ArrayUtils.contains(exIds, exID)) {
            System.out.println("\nError: Podano id zadania nieistniejącego w systemie. Spróbuj jeszcze raz!");
            System.out.println("Dostępne numery id zadań to : " + Arrays.toString(exIds).replace("[", "").replace("]", ""));
            exID = getId(scanner);
            scanner.nextLine();
        }
        return exID;
    }
}
