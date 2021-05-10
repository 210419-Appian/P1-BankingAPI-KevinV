package com.revature.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.models.AccountStatus;
import com.revature.models.AccountType;
import com.revature.utils.ConnectionUtil;

public class AccountStatusDAOImpl implements AccountStatusDAO {

	@Override
	public AccountStatus getAccStatusById(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM accstatus WHERE statusid = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();

			AccountStatus status = new AccountStatus();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each avenger Obj with columns from the database table
				status.setStatusID(result.getInt("statusid"));
				status.setStatusName(result.getString("statusname"));
				
			}
			return status;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
