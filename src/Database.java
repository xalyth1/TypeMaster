import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {

    private String dbName;
    private String databasePath;


    public Database() {
        dbName = "database.db";
        databasePath = "jdbc:sqlite:" + dbName;
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
    }

    ArrayList<String> loadDataFromDatabase() {
        File file = new File(System.getProperty("user.dir") + "\\" + dbName);
        System.out.println(file.exists());
        if (!file.exists()) {
            System.out.println("database file does not exist");
            //JOptionPane.showMessageDialog(new Frame(), "FILE DOES NOT EXIST!");
        }

        ArrayList<String> al = new ArrayList<>();

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(databasePath);
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
                    al.add(data_texts.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //show user message
        }

        System.out.println(al);
        return al;
    }
}
