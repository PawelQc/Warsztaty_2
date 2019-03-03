package pl.application.management;

import org.apache.commons.lang3.ArrayUtils;
import pl.application.models.Exercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class ManageExercise {


    public static void ManageExercise(Connection conn) throws SQLException {
        printExercise(conn);
        System.out.println("\nEdycja zadań. Wybierz jedną z opcji: \n *add (dodanie), \n *edit (modyfikacja), " +
                "\n *delete (usunięcie), \n *quit (koniec programu).");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (!"quit".equals(command)) {
            if ("add".equals(command)) {
                Exercise exAdd = new Exercise();
                System.out.println("\nPodaj tytuł zadania");
                exAdd.setTitle(scanner.nextLine());
                System.out.println("\nPodaj opis zadania");
                exAdd.setDescription(scanner.nextLine());
                exAdd.saveExerciseToDB(conn);
                System.out.println("Zadanie zostało zapisane :)");
                printExercise(conn);
            }
            else if ("edit".equals(command)) {
                System.out.println("\nPodaj id zadania, którego dane będą edytowane.");
                int idExEdit = getExerciseId(conn,scanner);
                Exercise exEdit = Exercise.loadExerciseById(conn, idExEdit);
                System.out.println("\nPodaj nowy tytuł zadania");
                exEdit.setTitle(scanner.nextLine());
                System.out.println("\nPodaj nowy opis zadania");
                exEdit.setDescription(scanner.nextLine());
                exEdit.saveExerciseToDB(conn);
                System.out.println("Zadanie zostało zmodyfikowane :)");
                printExercise(conn);
            }
            else if ("delete".equals(command)) {
                System.out.println("\nPodaj id zadania, które chcesz usunąć.");
                int idExDelete = getExerciseId(conn,scanner);
                Exercise exDelete = Exercise.loadExerciseById(conn, idExDelete);
                exDelete.deleteExercise(conn);
                System.out.println("Zadanie usunięto :)");
                printExercise(conn);
            } else {
                System.out.println("\nNieprawidłowa komenda - spróbuj jeszcze raz!");
            }

            System.out.println("\nEdycja użytkowników. Wybierz jedną z opcji: \n *add (dodanie), \n *edit (modyfikacja), " +
                    "\n *delete (usunięcie), \n *quit (koniec programu).");
            command = scanner.nextLine();
        }
        System.out.println("Zakończono edycję zadań");
    }


    public static void printExercise(Connection conn) throws SQLException {
        System.out.println("\nLista wszystkich zadań: ");
        String sql = "select * from exercise";
        PreparedStatement printstat = conn.prepareStatement(sql);
        ResultSet resultSet = printstat.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String title = resultSet.getString(2);
            String description = resultSet.getString(3);
            System.out.println(" " + id + " | " + title + ", " + description);
        }
    }

    private static int getId(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("\nError: Podaj wartość id jako liczbę całkowitą!");
            scanner.nextLine();
        }
        return scanner.nextInt();
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
