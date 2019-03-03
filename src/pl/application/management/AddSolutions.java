package pl.application.management;

import org.apache.commons.lang3.ArrayUtils;
import pl.application.models.Exercise;
import pl.application.models.Solution;
import pl.application.models.User;
import pl.application.util.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

public class AddSolutions {

    public static void main(String[] args) {

        try (Connection conn = DbUtil.getConnection()) {

            System.out.println("\nDodawanie rozwiązań przez użytkownika. Wybierz jedną z opcji:" +
                    "\n *add – dodawanie rozwiązania, " +
                    "\n *view – przeglądanie swoich rozwiązań," +
                    "\n *quit - wyjście z programu.");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();

            while (!"quit".equals(command)) {

                if ("add".equals(command)) {
                    int idUser = getUserId(conn, scanner);
                    User user = User.loadUserById(conn, idUser);
                    Solution[] userSolutions = Solution.loadAllSolvedSolutionsByUserId(conn, user.getId());
                    Solution[] allAssignedExercises = Solution.loadAllSolutionsByUserId(conn, user.getId());

                    int[] exercisesToDoIds = new int[allAssignedExercises.length];
                    for (int i = 0; i < allAssignedExercises.length; i++) {
                        exercisesToDoIds[i] = allAssignedExercises[i].getExercise().getId();
                    }

                    int[] idsfromSolutions = new int[userSolutions.length];
                    for (int i = 0; i < userSolutions.length; i++) {
                        idsfromSolutions[i] = userSolutions[i].getExercise().getId();
                    }

                    System.out.println("\nZadania do rozwiązania (które zostały do użytkownika przypisane):");
                    exercisesToDoIds = getExercisesIds(conn, exercisesToDoIds, idsfromSolutions);

                    if (exercisesToDoIds.length == 0) {
                        System.out.println("\n !!Brak rozwiązań do dodania przez użytkownika.");
                    } else {
                        saveUserSolution(conn, scanner, idUser, exercisesToDoIds);
                    }

                } else if ("view".equals(command)) {
                    System.out.print("\nWyświetlanie rozwiązań wybranego użytkownika. ");
                    int idUser = getUserId(conn,scanner);
                    Solution[] solutions = Solution.loadAllSolvedSolutionsByUserId(conn, idUser);
                    for (Solution solution : solutions) {
                        System.out.println(solution.toString());
                    }

                } else {
                    System.out.println("\nNieprawidłowa komenda - spróbuj jeszcze raz!");
                }

                System.out.println("\nDodawanie rozwiązań przez użytkownika. Wybierz jedną z opcji:" +
                        "\n *add – dodawanie rozwiązania, " +
                        "\n *view – przeglądanie swoich rozwiązań," +
                        "\n *quit - wyjście z programu.");
                command = scanner.nextLine();
            }
            System.out.println("\nZakończono edycję rozwiązań użytkownika");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveUserSolution(Connection conn, Scanner scanner, int idUser, int[] exercisesToDoIds) throws SQLException {
        System.out.println("\nPodaj id zadania do którego chcesz dodać rozwiązanie:");
        int idExercise = getIdExercise(scanner, exercisesToDoIds);
        Timestamp updateDate = new Timestamp(Calendar.getInstance().getTime().getTime());
        System.out.println("\nPodaj opis rozwiązania:");
        String description = scanner.nextLine();
        Solution solution = Solution.loadSolutionByUserIdandExerciseId(conn, idUser, idExercise);
        solution.setUpdated(updateDate);
        solution.setDescription(description);
        solution.saveSolutionToDB(conn);
        System.out.println("Zapis rozwiązania odbył się poprawnie :-)");
    }

    private static int[] getExercisesIds(Connection conn, int[] exercisesToDoIds, int[] idsfromSolutions) throws SQLException {
        for (int i = 0; i < exercisesToDoIds.length; i++) {
            for (int j = 0; j < idsfromSolutions.length; j++) {
                if (exercisesToDoIds[i] == idsfromSolutions[j]) {
                    exercisesToDoIds[i] = 0;
                }
            }
        }
        for (int i = 0; i < exercisesToDoIds.length; i++) {
            if (exercisesToDoIds[i] == 0) {
                exercisesToDoIds = ArrayUtils.removeAllOccurences(exercisesToDoIds, 0);
            }
        }
        for (int i = 0; i < exercisesToDoIds.length; i++) {
            Exercise exercise = Exercise.loadExerciseById(conn, exercisesToDoIds[i]);
            System.out.println(" *" + exercise.toString());
        }
        return exercisesToDoIds;
    }

    private static int getIdExercise(Scanner scanner, int[] exercisesToDoIds) {
        int idExercise = getId(scanner);
        scanner.nextLine();
        while (!ArrayUtils.contains(exercisesToDoIds, idExercise)) {
            System.out.println("Error: Podałeś zadanie spoza zakresu do rozwiązania - spróbuj jeszcze raz!");
            System.out.println("\nPodaj id zadania do którego chcesz dodać rozwiązanie:");
            idExercise = getId(scanner);
            scanner.nextLine();
        }
        return idExercise;
    }

    private static int getId(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Error: Podaj wartość id jako liczbę całkowitą!");
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

}
