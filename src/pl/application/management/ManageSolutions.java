package pl.application.management;

import pl.application.models.*;

import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class ManageSolutions {

    public static void ManageSolutions(Connection conn) throws SQLException {
        System.out.println("Edycja rozwiązań. Wybierz jedną z opcji: add (przypisanie zadań do użytkowników), " +
                "view (przeglądanie rozwiązań danego użytkownika), quit (koniec programu).");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (!"quit".equals(command)) {
            if ("add".equals(command)) {
                ManageUsers.printUsers(conn);
                System.out.println();
                System.out.println("Podaj id użytkownika");
                int idUser = getId(scanner);
                scanner.nextLine();
                User user = User.loadUserById(conn, idUser);

                ManageExercise.printExercise(conn);
                System.out.println("Podaj id zadania");
                int idExercise = getId(scanner);
                scanner.nextLine();
                Exercise exercise = Exercise.loadExerciseById(conn, idExercise);

                java.sql.Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

                Solution solution = new Solution(null, exercise, user, date);
                solution.saveSolutionToDB(conn);
                System.out.println("Rozwiązanie zapisano :)");


            } else if ("view".equals(command)) {
                System.out.println("Podaj id użytkownika, którego rozwiązania chcesz wyświetlić.");
                int idUser = getId(scanner);
                scanner.nextLine();
                Solution[] solutions = Solution.loadAllSolutionsByUserId(conn, idUser);
                for (Solution solution : solutions) {
                    System.out.println(solution.toString());
                }

            } else {
                System.out.println("Nieprawidłowa komenda - spróbuj jeszcze raz!");
            }

            System.out.println();
            System.out.println("Edycja rozwiązań. Wybierz jedną z opcji: add (przypisanie zadań do użytkowników)," +
                    " view (przeglądanie rozwiązań danego użytkownika), quit (koniec programu).");

            command = scanner.nextLine();
        }
        System.out.println("Zakończono edycję rozwiązań");
    }

    private static int getId(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Podaj wartość id jako liczbę całkowitą we wskazanym powyżej zakresie");
            scanner.nextLine();
        }
        return scanner.nextInt();
    }
}
