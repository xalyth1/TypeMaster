import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Database {

    private String dbName;
    private String databasePath;
    private SQLiteDataSource dataSource = new SQLiteDataSource();

    public Database() {
        dbName = "database.db";
        databasePath = "jdbc:sqlite:" + dbName;
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        dataSource.setUrl(databasePath);
    }

    public Database(String dbName) {
        this.dbName = dbName;
        this.databasePath = "jdbc:sqlite:" + dbName;;
        dataSource.setUrl(databasePath);
    }

//    public Database(String absoluteDatabaseFilePath) {
//
//    }

    ArrayList<String> loadDataFromDatabase() throws SQLException {
        File file = new File(System.getProperty("user.dir") + "\\" + dbName);
        System.out.println(file.exists());
        if (!file.exists()) {
            System.out.println("database file does not exist");
            //JOptionPane.showMessageDialog(new Frame(), "FILE DOES NOT EXIST!");
        }

        ArrayList<String> al = new ArrayList<>();

        try (Connection con = dataSource.getConnection()) {
            if (con.isValid(5)) {
                System.out.println("Connection is valid.");
                DatabaseMetaData metaData = con.getMetaData();
                //ResultSet rs = metaData.getTables(null, null, "%", null);

                String tableName = "TEXTS";
                ResultSet data_texts = con.createStatement().executeQuery(
                        "SELECT * FROM " + tableName + " WHERE subject_id = 2;");
                //ResultSetMetaData rsmd = rs.getMetaData();
                //int numberOfColumns = rsmd.getColumnCount();
                int rowCounted = data_texts.getInt(1);
                while (data_texts.next()) {
                    //System.out.println(data_texts.getString(2));
                    al.add(data_texts.getString(1));
                }
            }
        }

        System.out.println(al);
        return al;
    }


    SQLiteDataSource getDataSource() {
        return dataSource;
    }

    Optional<String[]> getPossibilities() {
        HashMap<String, Integer> subjects = new HashMap<>();
        String[] possibilities = null;

        try (Connection con = dataSource.getConnection()) {
            if (con.isValid(5)) {
                System.out.println("Connection is valid.");
                String tableName = "SUBJECTS";
                ResultSet subjectsRS = dataSource.getConnection().createStatement().executeQuery("SELECT * FROM " + tableName);
                while (subjectsRS.next()) {
                    subjects.put(subjectsRS.getString(2), Integer.parseInt(subjectsRS.getString(1)));
                }
                possibilities = subjects.keySet().toArray(new String[0]);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.of(possibilities);
    }

//    void createPossibility(String text, int subject_id) {
//        try (Connection con = dataSource.getConnection()) {
//            con.createStatement().executeQuery("INSERT INTO Texts(text, subject_id) VALUES ('" + jTextArea.getText() +
//                    "'," + subject_id + ");");
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//    }

    boolean insertText(int subjectCategoryId, String text) {
        boolean result;
        try (Connection con = dataSource.getConnection()) {
            con.createStatement().executeUpdate("INSERT INTO Texts(text, subject_id) VALUES ('" + text +
                    "'," + subjectCategoryId + ");");
            result = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }
}
