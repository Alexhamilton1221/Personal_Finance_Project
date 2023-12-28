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
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess extends AsyncTask<String, Void, List<String>> {
    private Context context;
    private ListView listView;
    private String queryType;

    public DatabaseAccess(Context context, ListView listView, String queryType) {
        this.context = context;
        this.listView = listView;
        this.queryType = queryType;
    }

    @Override
    protected List<String> doInBackground(String... userEmails) {
        List<String> dataList = new ArrayList<>();

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String jdbcUrl = "jdbc:jtds:sqlserver://personal-finance.ctthoc6soess.us-east-2.rds.amazonaws.com:1433;databaseName=finance_objects";
            String username = "admin";
            String password = "Chufa4677";

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            if ("group_tab".equals(queryType)) {
                handleGroupTabQuery(userEmails[0], dataList, connection);
            } else if ("another_tab".equals(queryType)) {
                handleAnotherTabQuery(userEmails[0], dataList, connection);
            } else {
                // Handle other query types if needed
                // ...
            }

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
        if (result != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, result);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Data retrieval failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleGroupTabQuery(String userEmail, List<String> dataList, Connection connection) throws SQLException {
        String emailQuery = "SELECT group_id FROM dbo.[appuser] WHERE email=?";
        try (PreparedStatement emailStatement = connection.prepareStatement(emailQuery)) {
            emailStatement.setString(1, userEmail);
            ResultSet emailResult = emailStatement.executeQuery();

            if (emailResult.next()) {
                int groupId = emailResult.getInt("group_id");
                Log.d("MyApp", "Group ID for Email " + userEmail + ": " + groupId);

                String userQuery = "SELECT * FROM dbo.[appuser] WHERE group_id=? ORDER BY group_id";
                try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                    userStatement.setInt(1, groupId);
                    ResultSet resultSet = userStatement.executeQuery();

                    while (resultSet.next()) {
                        String userEmailResult = resultSet.getString("email");
                        int userGroupId = resultSet.getInt("group_id");

                        String item = "Email: " + userEmailResult + ", Group ID: " + userGroupId;
                        dataList.add(item);
                    }
                }
            }

            emailResult.close();
        }
    }

    private void handleAnotherTabQuery(String userEmail, List<String> dataList, Connection connection) {
        // Implement logic for another query type
        // ...
    }
}
