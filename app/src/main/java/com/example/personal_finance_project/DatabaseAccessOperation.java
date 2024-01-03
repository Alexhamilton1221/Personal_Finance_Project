package com.example.personal_finance_project;

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
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String jdbcUrl = "jdbc:jtds:sqlserver://personal-finance.ctthoc6soess.us-east-2.rds.amazonaws.com:1433;databaseName=finance_objects";
            String username = "admin";
            String password = "Chufa4677";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                if ("insert_shopping_item".equals(queryType)) {
                    result = insertRecord(userEmails[0], connection);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("MyApp", "Database connection or query failed. Exception: " + e.getMessage());
        }

        return result;
    }


    private int findUserGroupId(String userEmail, Connection connection) throws SQLException {
        String query = "SELECT group_id FROM dbo.appuser WHERE email = ?";
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
        String query = "SELECT MAX(item_id) AS max_id FROM dbo.items";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("max_id");
            } else {
                return 0; // Return 0 if there are no existing records
            }
        }
    }

    private int insertRecord(String userEmail, Connection connection) throws SQLException {
        String query = "INSERT INTO dbo.items (item_id, group_id, name, price, quantity) VALUES (?, ?, ?, ?, ?)";


        int groupId=findUserGroupId(userEmail,connection);
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


}
