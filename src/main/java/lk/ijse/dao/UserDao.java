package lk.ijse.dao;

import lk.ijse.Modle.UserDto;

import java.sql.SQLException;

public interface UserDao {
    public boolean saveCustomer(UserDto userDto) throws SQLException, ClassNotFoundException ;
    public boolean existUser(String name) throws SQLException, ClassNotFoundException ;

    public boolean validUser(String name, String pass) throws SQLException, ClassNotFoundException;

}
