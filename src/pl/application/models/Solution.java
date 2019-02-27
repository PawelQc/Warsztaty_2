package pl.application.models;

import java.sql.*;
import java.util.ArrayList;

public class Solution {

    private int id;
    private Timestamp created;
    private Timestamp updated;
    private String description;
    private Exercise exercise;
    private User user;

    public Solution() {
    }

    public Solution(String description, Exercise exercise, User user, Timestamp created) {
        this.description = description;
        this.exercise = exercise;
        this.user = user;
        this.created = created;
    }

    static public Solution loadSolutionById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM solution where id=?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Solution loadedSolution = new Solution();
            loadedSolution.id = resultSet.getInt("id");
            loadedSolution.created = resultSet.getTimestamp("created");
            loadedSolution.updated = resultSet.getTimestamp("updated");
            loadedSolution.description = resultSet.getString("description");
            loadedSolution.exercise = Exercise.loadExerciseById(conn, resultSet.getInt("exercise_id"));
            loadedSolution.user = User.loadUserById(conn, resultSet.getInt("users_id"));
            return loadedSolution;
        }
        return null;
    }

    static public Solution[] loadAllSolutions(Connection conn) throws SQLException {
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        String sql = "SELECT * FROM solution";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Solution loadedSolution = new Solution();
            loadedSolution.id = resultSet.getInt("id");
            loadedSolution.created = resultSet.getTimestamp("created");
            loadedSolution.updated = resultSet.getTimestamp("updated");
            loadedSolution.description = resultSet.getString("description");
            loadedSolution.exercise = Exercise.loadExerciseById(conn, resultSet.getInt("exercise_id"));
            loadedSolution.user = User.loadUserById(conn, resultSet.getInt("users_id"));
            solutions.add(loadedSolution);
        }
        Solution[] sArray = new Solution[solutions.size()];
        sArray = solutions.toArray(sArray);
        return sArray;
    }

    static public Solution[] loadAllSolutionsByUserId(Connection conn, int id) throws SQLException {
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        String sql = "SELECT * FROM solution where users_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Solution loadedSolution = new Solution();
            loadedSolution.id = resultSet.getInt("id");
            loadedSolution.created = resultSet.getTimestamp("created");
            loadedSolution.updated = resultSet.getTimestamp("updated");
            loadedSolution.description = resultSet.getString("description");
            loadedSolution.exercise = Exercise.loadExerciseById(conn, resultSet.getInt("exercise_id"));
            loadedSolution.user = User.loadUserById(conn, resultSet.getInt("users_id"));
            solutions.add(loadedSolution);
        }
        Solution[] sArray = new Solution[solutions.size()];
        sArray = solutions.toArray(sArray);
        return sArray;
    }

    static public Solution[] loadAllSolutionsByExerciseId(Connection conn, int id) throws SQLException {
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        String sql = "select * from solution where exercise_id=? order by updated desc";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Solution loadedSolution = new Solution();
            loadedSolution.id = resultSet.getInt("id");
            loadedSolution.created = resultSet.getTimestamp("created");
            loadedSolution.updated = resultSet.getTimestamp("updated");
            loadedSolution.description = resultSet.getString("description");
            loadedSolution.exercise = Exercise.loadExerciseById(conn, resultSet.getInt("exercise_id"));
            loadedSolution.user = User.loadUserById(conn, resultSet.getInt("users_id"));
            solutions.add(loadedSolution);
        }
        Solution[] sArray = new Solution[solutions.size()];
        sArray = solutions.toArray(sArray);
        return sArray;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Solution " + id + ", " + description + ", utworzono: " + created + " | ";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void saveSolutionToDB(Connection conn) throws SQLException {
        if (this.id == 0) {
            String sql = "INSERT INTO solution(created, updated, description, exercise_id, users_id) VALUES (?, ?, ?, ?, ?)";
            String[] generatedColumns = {"ID"};
            PreparedStatement preparedStatement = conn.prepareStatement(sql, generatedColumns);
            preparedStatement.setTimestamp(1, this.created);
            preparedStatement.setTimestamp(2, this.updated);
            preparedStatement.setString(3, this.description);
            preparedStatement.setInt(4, this.exercise.getId());
            preparedStatement.setInt(5, this.user.getId());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE solution SET created=?, updated=?, description=?, exercise_id=?, users_id=? where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setTimestamp(1, this.created);
            preparedStatement.setTimestamp(2, this.updated);
            preparedStatement.setString(3, this.description);
            preparedStatement.setInt(4, this.exercise.getId());
            preparedStatement.setInt(5, this.user.getId());
            preparedStatement.setInt(6, this.id);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteSolution(Connection conn) throws SQLException {
        if (this.id != 0) {
            String sql = "DELETE FROM solution WHERE id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }


}

