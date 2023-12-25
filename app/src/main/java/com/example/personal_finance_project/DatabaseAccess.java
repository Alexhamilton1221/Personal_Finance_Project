package com.example.personal_finance_project;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess extends AsyncTask<String, Void, List<String>> {
    private Context context;
    private ListView listView;

    // Constructor to pass context and listView
    public DatabaseAccess(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected List<String> doInBackground(String... userEmails) {
        // Log the passed-in email
        Log.d("MyApp", "User Email Passed In: " + userEmails[0]);

        List<String> dataList = new ArrayList<>();

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String jdbcUrl = "jdbc:jtds:sqlserver://personal-finance.ctthoc6soess.us-east-2.rds.amazonaws.com:1433;databaseName=finance_objects";
            String username = "admin";
            String password = "Chufa4677";

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // First Query: Get group_id for the specified email
            String emailQuery = "SELECT group_id FROM dbo.[user] WHERE email=?";
            try (PreparedStatement emailStatement = connection.prepareStatement(emailQuery)) {
                emailStatement.setString(1, userEmails[0]);  // Assuming userEmails[0] contains the email
                ResultSet emailResult = emailStatement.executeQuery();

                // Check if there is a result
                if (emailResult.next()) {
                    int groupId = emailResult.getInt("group_id");

                    // Log the result of the first query
                    Log.d("MyApp", "Group ID for Email " + userEmails[0] + ": " + groupId);

                    // Second Query: Get user details for the specified email and group_id
                    String userQuery = "SELECT * FROM dbo.[user] WHERE group_id=? ORDER BY group_id";
                    try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                        userStatement.setInt(1, groupId);
                        ResultSet resultSet = userStatement.executeQuery();

                        // Process the result set and populate the dataList
                        while (resultSet.next()) {
                            String userEmail = resultSet.getString("email");
                            int userGroupId = resultSet.getInt("group_id");

                            String item = "Email: " + userEmail + ", Group ID: " + userGroupId;
                            dataList.add(item);
                        }
                    }
                }

                // Close resources for the first query
                emailResult.close();
            }

            // Close resources for the connection
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("MyApp", "Database connection or query failed. Exception: " + e.getMessage());
        }

        return dataList;
    }

    @Override
    protected void onPostExecute(List<String> result) {
        super.onPostExecute(result);
        // Update the ListView with the data obtained from the background task
        if (result != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, result);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Data retrieval failed", Toast.LENGTH_SHORT).show();
        }
    }
}
