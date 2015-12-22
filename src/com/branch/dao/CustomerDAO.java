package com.branch.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.branch.data.Customer;
import com.branch.jdbc.JDBCConnection;

public class CustomerDAO {

	public Customer identifyCustomerUsingTag(String tagId) {

		PreparedStatement statement = null;
		Customer cust = null;
		Connection conn = null;
		try {
			conn = JDBCConnection.getConnection();
			statement = conn.prepareStatement("select * from customers where PHOTO_TAG_ID = ?");
			statement.setString(1, tagId);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {

				cust = new Customer();

				cust.setPartyId(rs.getString("PARTY_ID"));
				cust.setCity(rs.getString("CITY"));
				cust.setFirstName(rs.getString("FIRST_NAME"));
				cust.setLastName(rs.getString("LAST_NAME"));
				cust.setPhotoTagId(rs.getString("PHOTO_TAG_ID"));
				cust.setTwitterHandle(rs.getString("TWITTER_HANDLE"));
			}
			JDBCConnection.closeConnection(conn);
		} catch (SQLException sqlException) {

			JDBCConnection.closeConnection(conn);
		}

		return cust;
	}
}
