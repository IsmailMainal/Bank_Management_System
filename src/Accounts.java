import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;
    private Scanner scanner;

    public  Accounts(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public long open_account(String email){
        if (!account_exist(email)){    //check account exist or not
            String open_account_query = "insert into bank_management.accounts(account_number,full_name,email,balance,security_pin,phone) values(?,?,?,?,?,?)";
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your name");
            String full_name = scanner.nextLine();

            System.out.println("Enter Initial Amount ");
            double balance = scanner.nextDouble();

            System.out.println("Enter Security pin");
            String security_pin = scanner.nextLine();

            System.out.print("Enter Your Phone : ");
            int phone = scanner.nextInt();

            try {
                connection.setCatalog("bank_management");
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security_pin);
                preparedStatement.setInt(6,phone);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0){
                    return account_number;
                }else {
                    throw new RuntimeException("Account Creation Failed!");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Account Already Exist");
    }



    public long getAccount_number(String email){
        String getAccount_number_query = "select account_number from accounts where email=?";
        try {
            connection.setCatalog("bank_management");
            PreparedStatement preparedStatement = connection.prepareStatement(getAccount_number_query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Account Number is not Exist");
    }



    private  long generateAccountNumber() {
        try{
            connection.setCatalog("bank_management");
            Statement statement = connection.createStatement();
            ResultSet resultSet =statement.executeQuery ("select account_number from accounts order by account_number desc limit 1");
            if (resultSet.next()){
                long last_account_number = resultSet.getLong("account_number");
                return  last_account_number + 1;
            }else {
                return 10000100;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
       // return 10000100;
    }



    public boolean account_exist(String email) {
        String query = "select account_number from accounts where email = ?";
        try{
            connection.setCatalog("bank_management");
            PreparedStatement prepareStatement = connection.prepareStatement(query);
            prepareStatement.setString(1,email);
            ResultSet resultSet = prepareStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //return false;
    }
}
