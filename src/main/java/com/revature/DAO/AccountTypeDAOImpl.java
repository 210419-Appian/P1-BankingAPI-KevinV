package com.revature.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.models.AccountType;
import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

public class AccountTypeDAOImpl implements AccountTypeDAO {

	@Override
	public AccountType getAccTypeById(int id) {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM acctype WHERE typeid = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();

			AccountType type = new AccountType();

			//resultSet's cursor means we can use result.next to return true and move the cursor
			while(result.next()) {
				//populate each avenger Obj with columns from the database table
				type.setTypeID(result.getInt("typeid"));
				type.setTypeName(result.getString("typename"));
				
			}
			return type;

		}catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
