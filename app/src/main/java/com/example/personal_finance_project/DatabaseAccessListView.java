package com.example.personal_finance_project;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessListView extends AsyncTask<String, Void, List<String>> {
    private Context context;
    private ListView listView;
    private String queryType;

    public DatabaseAccessListView(Context context, ListView listView, String queryType) {
        this.context = context;
        this.listView = listView;
        this.queryType = queryType;
    }

    @Override
    protected List<String> doInBackground(String... userEmails) {
        List<String> dataList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://personal-finance-free.ctthoc6soess.us-east-2.rds.amazonaws.com:3306/finance";

            String username = "admin";
            String password = "Chufa4677";

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            if ("group_tab".equals(queryType)) {
                handleGroupTabQuery(userEmails[0], dataList, connection);
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
        String emailQuery = "SELECT group_id FROM `appuser` WHERE email = ?";
        try (PreparedStatement emailStatement = connection.prepareStatement(emailQuery)) {
            emailStatement.setString(1, userEmail);
            ResultSet emailResult = emailStatement.executeQuery();

            if (emailResult.next()) {
                Integer groupId = emailResult.getInt("group_id");

                if (emailResult.wasNull()) {
                    groupId = -1;
                }
                if (groupId == null || groupId.equals(-1)) {
                    // User is not in a group, add a special entry
                    String notInGroupItem = "User is not in a group";
                    dataList.add(notInGroupItem);
                } else {
                    // User is in a group, retrieve user information from Firebase
                    String userQuery = "SELECT email FROM appuser WHERE group_id=? ORDER BY group_id";

                    try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                        userStatement.setInt(1, groupId);
                        ResultSet resultSet = userStatement.executeQuery();

                        while (resultSet.next()) {
                            String userEmailResult = resultSet.getString("email");
                            dataList.add(userEmailResult);
                        }
                    }
                }
            } else {
                // No result found for user email
                Log.d("MyApp", "No result found for user email: " + userEmail);
            }

            emailResult.close();
        } catch (SQLException e) {
            Log.e("MyApp", "Error handling group tab query: " + e.getMessage());
            throw e;
        }
    }

}
