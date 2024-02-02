package lk.ijse.dao;


import lk.ijse.Modle.UserDto;
import lk.ijse.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao{

    @Override
    public boolean saveCustomer(UserDto userDto) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO user (userName,password ) VALUES (?,?)");
        pstm.setString(1, userDto.getUserName());
        pstm.setInt(2, Integer.parseInt(userDto.getPassword()));

        return pstm.executeUpdate() >0 ;
    }
    @Override
    public boolean existUser(String name) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT userName FROM user WHERE userName =?");
        pstm.setString(1, name);
        return pstm.executeQuery().next();
    }

    @Override
    public boolean validUser(String name, String pass) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        String sql = "SELECT * FROM user WHERE userName = ? AND password = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, name);
        ptsm.setString(2,pass);

        ResultSet resultSet = ptsm.executeQuery();
        return resultSet.next();
    }


}
