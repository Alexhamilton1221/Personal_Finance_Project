package com.example.personal_finance_project;

import static java.sql.Types.NULL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessOperation extends AsyncTask<String, Void, Integer> {

    private Context context;
    private String queryType;
    private OnTaskCompleteListener listener;

    public EditText nameEditText;
    public EditText priceEditText;
    public EditText quantityEditText;

    public interface OnTaskCompleteListener {
        void onTaskComplete(int result);
    }

    public void setOnTaskCompleteListener(OnTaskCompleteListener listener) {
        this.listener = listener;
    }

    public DatabaseAccessOperation(Context context, String queryType, EditText nameEditText, EditText priceEditText, EditText quantityEditText) {
        this.context = context;
        this.queryType = queryType;
        this.nameEditText = nameEditText;
        this.priceEditText = priceEditText;
        this.quantityEditText = quantityEditText;
    }

    @Override
    protected Integer doInBackground(String... userEmails) {
        int result = -1;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://personal-finance-free.ctthoc6soess.us-east-2.rds.amazonaws.com:3306/finance";
            String username = "admin";
            String password = "Chufa4677";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                if ("insert_shopping_item".equals(queryType)) {
                    result = insertItemRecord(userEmails[0], connection);
                } else if ("create_group".equals(queryType)) {
                    result = insertGroupRecord(userEmails[0], connection);
                } else if ("register".equals(queryType)) {
                    result = register_user(userEmails[0], connection);
                } else if ("join_group".equals(queryType)) {
                    result = join_group_with_gname(userEmails[0], connection);


                }


            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("MyApp", "Database connection or query failed. Exception: " + e.getMessage());
        }

        return result;
    }

    private int register_user(String userEmail, Connection connection) throws SQLException {

        String query = "INSERT INTO appuser (email, group_id) VALUES (?,?)";


        int groupId = 1;

        try (PreparedStatement insertStatement = connection.prepareStatement(query)) {
            insertStatement.setString(1, userEmail);
            insertStatement.setInt(2, groupId);

            // Log the values being used for the insert
            Log.d("Register user", "Values for insert: " + userEmail + groupId);

            // Execute the insert query
            int rowsAffected = insertStatement.executeUpdate();

            // Log the number of rows affected
            Log.d("InsertGroup", "Rows affected: " + rowsAffected);

            // Now move current user into group
            join_group(userEmail, groupId, connection);

            return 0;

        }
    }

    private int findUserGroupId(String userEmail, Connection connection) throws SQLException {
        String query = "SELECT group_id FROM `appuser` WHERE email = ?";

        try (PreparedStatement emailStatement = connection.prepareStatement(query)) {
            emailStatement.setString(1, userEmail);
            ResultSet emailResult = emailStatement.executeQuery();

            if (emailResult.next()) {
                int groupId = emailResult.getInt("group_id");
                Log.d("MyApp", "Group ID for Email " + userEmail + ": " + groupId);
                return groupId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static int findHighestItemId(Connection connection) throws SQLException {
        String query = "SELECT MAX(item_id) AS max_id FROM items";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("max_id");
            } else {
                return 0; // Return 0 if there are no existing records
            }
        }
    }

    private static int findHighestGroupId(Connection connection) throws SQLException {
        String query = "SELECT MAX(group_id) AS max_id FROM group_table";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("max_id");
            } else {
                return 0; // Return 0 if there are no existing records
            }
        }
    }


    private int insertItemRecord(String userEmail, Connection connection) throws SQLException {
        String query = "INSERT INTO items (item_id, group_id, name, price, quantity) VALUES (?, ?, ?, ?, ?)";

        int groupId = findUserGroupId(userEmail, connection);
        int newItemId = findHighestItemId(connection) + 1;
        String name = nameEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String quantityStr = quantityEditText.getText().toString().trim();

        double price = Double.parseDouble(priceStr);
        int quantity = Integer.parseInt(quantityStr);

        try (PreparedStatement insertStatement = connection.prepareStatement(query)) {
            insertStatement.setInt(1, newItemId);
            insertStatement.setInt(2, groupId);
            insertStatement.setString(3, name);

            // Set NULL for price if it's 0 or empty, otherwise set the actual value
            if (price == 0 || Double.isNaN(price)) {
                insertStatement.setNull(4, java.sql.Types.DECIMAL);
            } else {
                insertStatement.setBigDecimal(4, BigDecimal.valueOf(price));
            }

            // Set NULL for quantity if it's 0, otherwise set the actual value
            if (quantity == 0) {
                insertStatement.setNull(5, java.sql.Types.INTEGER);
            } else {
                insertStatement.setInt(5, quantity);
            }

            // Log the values being used for the insert
            Log.d("InsertShoppingItem", "Values for insert: " + newItemId + ", " + groupId + ", " + name + ", " + price + ", " + quantity);

            // Execute the insert query
            int rowsAffected = insertStatement.executeUpdate();

            // Log the number of rows affected
            Log.d("InsertShoppingItem", "Rows affected: " + rowsAffected);

            // Check if the insertion was successful
            return 0;
        }

    }

    private int insertGroupRecord(String userEmail, Connection connection) throws SQLException {
        String query = "INSERT INTO group_table (group_id, group_name) VALUES (?,?)";

        int groupId = findHighestGroupId(connection) + 1;
        String name = nameEditText.getText().toString().trim();


        try (PreparedStatement insertStatement = connection.prepareStatement(query)) {
            insertStatement.setInt(1, groupId);
            insertStatement.setString(2, name);


            // Log the values being used for the insert
            Log.d("InsertGroup", "Values for insert: " + groupId + name);

            // Execute the insert query
            int rowsAffected = insertStatement.executeUpdate();

            // Log the number of rows affected
            Log.d("InsertGroup", "Rows affected: " + rowsAffected);

            // Now move current user into group
            join_group(userEmail, groupId, connection);

            return 0;

        }


    }

    private int getGroupIdFromGroupName(String group_name, Connection connection) throws SQLException {
        String query = "SELECT group_id FROM group_table WHERE group_name = ?";

        try (PreparedStatement selectStatement = connection.prepareStatement(query)) {
            // Set values for parameters
            selectStatement.setString(1, group_name);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Extract the groupId from the result set
                    return resultSet.getInt("group_id");
                } else {

                    Log.e("JoinGroup", "Group with name '" + group_name + "' does not exist.");

                    return -1;
                }
            }
        } catch (SQLException e) {
            // Handle exceptions
            Log.e("JoinGroup", "Error getting groupId: " + e.getMessage());
            throw e;
        }
    }


    private int join_group(String userEmail, int groupId, Connection connection) throws SQLException {
        String query = "UPDATE appuser SET group_id = ? WHERE email = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(query)) {
            // Set values for parameters
            updateStatement.setInt(1, groupId);
            updateStatement.setString(2, userEmail);

            // Log the values being used for the update
            Log.d("JoinGroup", "Values for update: Group ID - " + groupId + ", Email - " + userEmail);

            // Execute the update query
            int rowsAffected = updateStatement.executeUpdate();

            // Log the number of rows affected
            Log.d("JoinGroup", "Rows affected: " + rowsAffected);


            return rowsAffected;

        } catch (SQLException e) {
            // Handle exceptions
            Log.e("JoinGroup", "Error joining group: " + e.getMessage());
            throw e;
        }
    }

    private int join_group_with_gname(String userEmail, Connection connection) throws SQLException {

        String group_name = nameEditText.getText().toString().trim();
        int groupId = getGroupIdFromGroupName(group_name, connection);

        join_group(userEmail, groupId, connection);

        return 0;

    }
}