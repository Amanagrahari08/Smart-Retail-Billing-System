import util.DBConnection;
import java.sql.Connection;

public class Test {
    public static void main(String[] args) {
        Connection con = DBConnection.getConnection();
    }
}
