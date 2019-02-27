package pl.application.management;

import pl.application.models.Exercise;
import pl.application.models.User;
import pl.application.models.UserGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ManageExercise {


    public static void ManageExercise(Connection conn) throws SQLException {
        printExercise(conn);
        System.out.println();
        System.out.println("Edycja zadań. Wybierz jedną z opcji: add (dodanie), edit (modyfikacja), " +
                "delete (usunięcie), quit (koniec programu).");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (!"quit".equals(command)) {
            if ("add".equals(command)) {
                Exercise exAdd = new Exercise();
                System.out.println("Podaj tytuł zadania");
                exAdd.setTitle(scanner.nextLine());
                System.out.println("Podaj opis zadania");
                exAdd.setDescription(scanner.nextLine());
                exAdd.saveExerciseToDB(conn);
                System.out.println("Zadanie zostało zapisane :)");
                printExercise(conn);
            }
            else if ("edit".equals(command)) {
                System.out.println("Podaj id zadania, którego dane będą edytowane.");
                int idExEdit = scanner.nextInt();
                scanner.nextLine();
                Exercise exEdit = Exercise.loadExerciseById(conn, idExEdit);
                System.out.println("Podaj nowy tytuł zadania");
                exEdit.setTitle(scanner.nextLine());
                System.out.println("Podaj nowy opis zadania");
                exEdit.setDescription(scanner.nextLine());
                exEdit.saveExerciseToDB(conn);
                System.out.println("Zadanie zostało zmodyfikowane :)");
                printExercise(conn);
            }
            else if ("delete".equals(command)) {
                System.out.println("Podaj id zadania, które chcesz usunąć.");
                int idExDelete = scanner.nextInt();
                scanner.nextLine();
                Exercise exDelete = Exercise.loadExerciseById(conn, idExDelete);
                exDelete.deleteExercise(conn);
                System.out.println("Zadanie usunięto :)");
                printExercise(conn);
            } else {
                System.out.println("Nieprawidłowa komenda - spróbuj jeszcze raz!");
            }

            System.out.println();
            System.out.println("Edycja zadań. Wybierz jedną z opcji: add (dodanie), edit (modyfikacja), " +
                    "delete (usunięcie), quit (koniec programu).");
            command = scanner.nextLine();
        }
        System.out.println("Zakończono edycję zadań");
    }


    public static void printExercise(Connection conn) throws SQLException {
        String sql = "select * from exercise";
        PreparedStatement printstat = conn.prepareStatement(sql);
        ResultSet resultSet = printstat.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String title = resultSet.getString(2);
            String description = resultSet.getString(3);
            System.out.println(id + " | " + title + ", " + description);
        }
    }

}
