package com.felipe.uniroom.controller;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<Object> getObjects(String campo, String ordem) {
        final String query = "SELECT * FROM Objects ORDER BY " + campo + " " + ordem;
        try {
            Database.getConnection();

            ResultSet rs = Database.execute(query, new Object[]{}, false);

            List<Object> Objects = new ArrayList<>();

            while (rs.next()) {
                Object p = new Object();

                // p.setId(rs.getInt("id_Object"));
                // p.setName(rs.getString("name"));
                // p.setNumber(rs.getInt("number"));
                // p.setPosition(rs.getString("position"));
                // p.setScore(rs.getInt("score"));
                // p.setUser(rs.getString("user"));
                // p.setScore(rs.getInt("score"));
                // p.setBestObjectFromDb(rs.getInt("best_Object"));
                // p.setBeautifulScoreFromDb(rs.getInt("beautiful_score"));

                Objects.add(p);
            }

            return Objects;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static List<Object> getObjectes() {
        final String query = "SELECT * FROM Objectes ORDER BY id_Object ASC";
        try {
            Database.getConnection();

            ResultSet rs = Database.execute(query, new Object[]{}, false);

            List<Object> Objectes = new ArrayList<>();

            while (rs.next()) {
                Object m = new Object();

                String[] bests = getBests(rs.getInt("id_best_Object"), rs.getInt("id_beautiful_score"));

                // m.setId(rs.getInt("id_Object"));
                // m.setDateTimeObject(rs.getString("date_time_Object"));
                // m.setScoreA(rs.getInt("team_a_score"));
                // m.setScoreB(rs.getInt("team_b_score"));
                // m.setWinner(rs.getString("winner"));
                // m.setBestObject(bests[0]);
                // m.setObjectBeautifulScore(bests[1]);

                Objectes.add(m);
            }

            return Objectes;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static String[] getBests(int idBestObject, int idBeautifulScore) {
        final String query = "SELECT id_Object, name FROM Objects WHERE id_Object IN (?, ?) ORDER BY id_Object ASC";

        try {
            Database.getConnection();

            ResultSet rs = Database.execute(query, new Object[]{
                    idBestObject,
                    idBeautifulScore
            }, false);

            String[] bests = new String[2];

            while (rs.next()) {
                if (idBestObject == rs.getInt("id_Object")) {
                    bests[0] = rs.getString("name");
                } else {
                    bests[1] = rs.getString("name");
                }
            }

            return bests;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static Object getObject(int id) {
        final String query = "SELECT id_Object, name, number, position, score FROM Objects WHERE id_Object = ?";
        try {
            Database.getConnection();
            ResultSet rs = Database.execute(query, new Object[]{id}, false);

            Object p = new Object();
            while (rs.next()) {
                // p.setId(rs.getInt("id_Object"));
                // p.setName(rs.getString("name"));
                // p.setNumber(rs.getInt("number"));
                // p.setPosition(rs.getString("position"));
                // p.setScore(rs.getInt("score"));
            }
            return p;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static void deleteObject(int id) {
        final String query = "DELETE FROM Objects WHERE id_Object = ?";
        try {
            Database.execute(query, new Object[]{id}, true);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getSecretPhrase(String username) {
        final String query = "SELECT secret_phrase FROM Objects WHERE user = ?";
        try {
            Database.getConnection();
            ResultSet rs = Database.execute(query, new Object[]{username}, false);
            if (rs.next()) {
                return rs.getString("secret_phrase");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static boolean checkObjectExistence(String username) {
        final String query = "SELECT user FROM Objects WHERE user = ?";
        try {
            Database.getConnection();
            ResultSet rs = Database.execute(query, new Object[]{username}, false);
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static void updateObjects(DefaultTableModel model, List<Object> ObjectList) {
        model.setRowCount(0);

        for (Object Object : ObjectList) {
            model.addRow(new Object[]{
                    // Object.getName(),
                    // Object.getScore(),
                    // Object.getTeam()
            });
        }
    }

    public static String[] getObjectsWithScore(List<Object> Objects) {
        String[] ObjectsName = new String[Objects.size()];

        for (int i = 0; i < Objects.size(); i++) {
            // ObjectsName[i] = Objects.get(i).getName();
        }

        return ObjectsName;
    }

    public static ResultSet saveObject(String dateTimeObject, String teamAScore, String teamBScore,
                                      int idBeautifulScore, int idBestObject) {
        final String queryInsert = "INSERT INTO Objectes (date_time_Object, team_a_score, team_b_score, winner, id_best_Object, id_beautiful_score) VALUES (?, ?, ?, ?, ?, ?)";
        final String querySelect = "SELECT id_Object, id_beautiful_score, id_best_Object FROM Objectes WHERE date_time_Object = ? AND team_a_score = ? AND team_b_score = ?";
        try {
            Database.execute(queryInsert, new Object[]{
                    dateTimeObject,
                    teamAScore,
                    teamBScore,
                    getWinner(teamAScore, teamBScore),
                    idBestObject,
                    idBeautifulScore
            }, true);

            return Database.execute(querySelect, new Object[]{
                    dateTimeObject,
                    teamAScore,
                    teamBScore
            }, false);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public static void relateObjectObject(int idObject, int idObject2, int score) {
        final String query = "INSERT INTO Objectes_Objects (id_Object, id_Object, Object_Object_score) VALUES (?, ?, ?)";
        try {
            Database.execute(query, new Object[]{
                    idObject,
                    idObject,
                    score
            }, true);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void updateScores(int idObject, int idObject2) {
        final String query = "UPDATE Objects SET score = score + (SELECT Object_Object_score FROM Objectes_Objects WHERE id_Object = ? AND id_Object = ?) WHERE id_Object = ?";

        try {
            Database.execute(query, new Object[]{
                    idObject,
                    idObject,
                    idObject
            }, true);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void updateBeautifulScoreBestObject(int idObject) {
        final String querySelect = "SELECT id_beautiful_score, id_best_Object FROM Objectes WHERE id_Object = ?";
        final String queryUpdateBeautifulScore = "UPDATE Objects SET beautiful_score = beautiful_score + 1 WHERE id_Object = ?";
        final String queryUpdateBestObject = "UPDATE Objects SET best_Object = best_Object + 1 WHERE id_Object = ?";

        try {
            ResultSet rs = Database.execute(querySelect, new Object[]{
                    idObject
            }, false);

            if (rs.next()) {
                Database.execute(queryUpdateBeautifulScore, new Object[]{
                        rs.getInt("id_beautiful_score"),
                }, true);

                Database.execute(queryUpdateBestObject, new Object[]{
                        rs.getInt("id_best_Object"),
                }, true);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static char getWinner(String teamAScore, String teamBScore) {
        if (Integer.parseInt(teamAScore) > Integer.parseInt(teamBScore)) {
            return 'A';
        } else if (Integer.parseInt(teamAScore) < Integer.parseInt(teamBScore)) {
            return 'B';
        } else {
            return 'E';
        }
    }
}