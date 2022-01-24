package Database.Tables;

import Database.Connection.DB_Connection;
import Database.mainClasses.Account;
import Database.mainClasses.Company;
import Database.mainClasses.Merchant;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MerchantTable implements DBTable {

    private Gson gson;
    private Connection con;
    private Statement stmt;
    private ResultSet rs;


    public MerchantTable() {

        gson = new Gson();
    }

    public String accountToJSON(Merchant merchant) {

        return gson.toJson(merchant);
    }

    public void addAccountFromJSON(String json) throws SQLException, ClassNotFoundException {

        Merchant merchant = gson.fromJson(json, Merchant.class);
        merchant.initfields(0);
        addNewAccount(merchant);
    }

    public void addNewAccount(Merchant merchant) throws SQLException, ClassNotFoundException {

        String insertQuery = "INSERT INTO "
                + " merchants (name,username,password,comission,profit,amount_due)"
                + " VALUES ("
                + "'" + merchant.getName() + "',"
                + "'" + merchant.getUsername() + "',"
                + "'" + merchant.getPassword() + "',"
                + "'" + merchant.getComission() + "',"
                + "'" + merchant.getProfit() + "',"
                + "'" + merchant.getAmount_due() + "'"
                + ")";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.executeUpdate(insertQuery);
        System.out.println("# The merchant was successfully added in the database.");

        stmt.close();
    }

    public void createTable() throws SQLException, ClassNotFoundException {

        String sql = "CREATE TABLE merchants "
                + "(account_id INTEGER not NULL AUTO_INCREMENT, "
                + "name VARCHAR (40) not null,"
                + "username VARCHAR (20) not null unique, "
                + "password VARCHAR (20) not null,"
                + "comission DOUBLE, "
                + "profit DOUBLE, "
                + "amount_due DOUBLE, "
                + "PRIMARY KEY ( account_id ))";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    @Override
    public void buy(int cli_id, int amount) throws UserNotFoundException, ClassNotFoundException {

    }

    public Merchant findAccount(String username, String password) throws SQLException, ClassNotFoundException {
        
        Merchant user;

        String query   = "SELECT username, password FROM merchants WHERE username = '" + 
        username + "' AND password = '" + password +"'";

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);

        if ( !rs.next() )
            return null;

        user = gson.fromJson(DB_Connection.getResultsToJSON(rs), Merchant.class);
        stmt.close();
        con.close();

        return user;
    }

    public JSONObject getAll() throws SQLException, ClassNotFoundException {

        con = DB_Connection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT account_id, name FROM merchants");

        if ( !rs.next() )
            return null;

        stmt.close();
        con.close();

        return new JSONObject(DB_Connection.getResultsToJSON(rs));
    }
}
