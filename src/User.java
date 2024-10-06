import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public void register(){
        scanner.nextLine();  //already Scanner Class is defined
        System.out.print("Enter Your Name : ");
        String full_name = scanner.nextLine();

        System.out.print("Enter Your Email : ");
        String email = scanner.nextLine();

        System.out.print("Enter Your Password : ");
        String password = scanner.nextLine();

        System.out.print("Enter Your Phone : ");
        int phone = scanner.nextInt();

        if (user_exist(email)){
            System.out.println("User is Already Exists for this Email");
            return;
        }
        String register_query = "insert into user(full_name,email,password,phone) values(?,?,?,?)";
        try {
            connection.setCatalog("bank_management");
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            preparedStatement.setInt(4,phone);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                System.out.println("Registration Successfull!");
            }
            else {
                System.out.println("Registration Failed!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public String login(){                   //retrive the query to check register user is present in DB
        scanner.nextLine();
        System.out.print("Enter Your Email : ");
        String email = scanner.nextLine();

        System.out.print("Enter Your Password : ");
        String password = scanner.nextLine();

        String login_query = "select * from user where email=? and password=?";
        try{
            connection.setCatalog("bank_management");
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){   //if get any entry from user
                return email;
            }else {
                return null;
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean user_exist(String email) {
        String user_exist_query = "select * from user where email=?";  //Check Db for user is Exist
        try {
            connection.setCatalog("bank_management");
            PreparedStatement preparedStatement = connection.prepareStatement(user_exist_query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else {
                return false;
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

          //return false;
    }
}
