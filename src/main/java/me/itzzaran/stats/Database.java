package me.itzzaran.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public final Main plugin;

    public Database(Main plugin) {
        this.plugin = plugin;
    }

    public static Connection getConnection() throws SQLException {
        String dbUrl = "";
        String user = "";
        String password = "";

        Connection connection = DriverManager.getConnection(dbUrl, user, password);
        connection.setAutoCommit(true);
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ignored) {
        }
    }

    public static void closeResulSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException ignored) {
        }
    }

    public static List topten(String type){
        List<Playerdata> toplist = new ArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            connection = Database.getConnection();
            String sql = "SELECT * FROM playerdata ORDER BY `"+type+"` DESC LIMIT 10";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                int mob_kills = resultSet.getInt("mob_kills");
                int player_kills = resultSet.getInt("player_kills");
                int times_eaten = resultSet.getInt("times_eaten");
                int distance_walked = resultSet.getInt("distance_walked");
                toplist.add(Main.instance.rankedplayer(uuid, mob_kills, player_kills, times_eaten, distance_walked));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            Database.closeResulSet(resultSet);
            Database.closeStatement(statement);
            Database.closeConnection(connection);
        }
        return toplist;
    }

    public static void saveData(Player p) {
        Playerdata playerdata = Main.instance.getPlayerData(p);
        Connection connection = null;
        Statement statement = null;
        String uuid = playerdata.getPlayer().getUniqueId().toString();
        try {
            connection = Database.getConnection();
//            System.out.println("Database connection succesfull for data loading");

            String sql = "UPDATE `playerdata` SET" +
                    " player_kills = '" + playerdata.getPlayer_kills() + "'," +
                    " mob_kills = '" + playerdata.getMob_kills() + "'," +
                    " times_eaten = '" + playerdata.getTimes_eaten() + "'," +
                    " distance_walked = '" + playerdata.getDistance_walked() + "'" +
                    " WHERE uuid = '" + uuid + "'";
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeStatement(statement);
            Database.closeConnection(connection);
        }
    }

    public static void loadData(Player p) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String uuid = p.getUniqueId().toString();
        try {
            connection = Database.getConnection();
//            System.out.println("Database connection succesfull for data loading");

            String sql = "SELECT * FROM playerdata WHERE uuid='" + uuid + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                String updatesql = "INSERT INTO playerdata (uuid, mob_kills, player_kills, times_eaten, distance_walked)  VALUES ('" + uuid + "', '0', '0', '0', '0')";
                statement.executeUpdate(updatesql);
                Main.instance.createnewplayer(p);
            } else {
                Database.closeResulSet(resultSet);
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    int mob_kills = resultSet.getInt("mob_kills");
                    int player_kills = resultSet.getInt("player_kills");
                    int times_eaten = resultSet.getInt("times_eaten");
                    int distance_walked = resultSet.getInt("distance_walked");

                    Main.instance.loadplayerdata(p, mob_kills, player_kills, times_eaten, distance_walked);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeResulSet(resultSet);
            Database.closeStatement(statement);
            Database.closeConnection(connection);
        }
    }

    public static void createTable() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = Database.getConnection();
//            System.out.println("Database connection succesfull for table creation");

            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS playerdata (id int NOT NULL AUTO_INCREMENT, uuid VARCHAR(120) NOT NULL, mob_kills int NOT NULL, player_kills int NOT NULL, times_eaten int NOT NULL, distance_walked int NOT NULL, PRIMARY KEY (id), UNIQUE KEY (uuid))";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeStatement(statement);
            Database.closeConnection(connection);
        }
    }


}
