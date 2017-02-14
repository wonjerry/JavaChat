

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MultiChatClientDB {
	public boolean check(String id, String pw){
		Connection connection = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/ChatUsers" , "root", "dnjswo19");
            st = connection.createStatement();
 
            String sql;
            sql = "SELECT * FROM ChatUsers";
 
            ResultSet rs = st.executeQuery(sql);
 
            while (rs.next()) {
                String userId = rs.getString("id");
                String userPw = rs.getString("password");
                if(id.equals(userId)&& pw.equals(userPw)){
                	rs.close();
                    st.close();
                    connection.close();
                	return true;
                }
            }
 
            rs.close();
            st.close();
            connection.close();
            return false;
        } catch (SQLException se1) {
            se1.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
		
		return false;
	}
	public boolean newbie(String id, String pw){
		Connection connection = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/ChatUsers" , "root", "dnjswo19");
            st = connection.createStatement();
 
            String sql;
            sql = "INSERT INTO ChatUsers VALUES('"+id+"','"+pw+"')";
 
            if(st.executeUpdate(sql)!=1){
            	st.close();
                connection.close();
            	return false;
            }else{
            	st.close();
                connection.close();
                return true;
            }
        } catch (SQLException se1) {
            se1.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
		
		return false;
	}
}