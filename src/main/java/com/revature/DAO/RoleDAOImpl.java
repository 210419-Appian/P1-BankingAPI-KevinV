package com.revature.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.revature.models.Role;
import com.revature.utils.ConnectionUtil;

public class RoleDAOImpl implements RoleDAO {

	@Override
	public Role findRoleByID(int roleID) {
		try(Connection conn = ConnectionUtil.getConnection()){

			String sql = "SELECT * FROM roles WHERE roleid=?;";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, roleID);
			
			ResultSet result = statement.executeQuery();

			Role r = new Role();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each home Obj with columns from the database table
				r.setRoleID(result.getInt("roleID"));
				r.setRoleName(result.getString("rolename"));
			}
			return r;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
