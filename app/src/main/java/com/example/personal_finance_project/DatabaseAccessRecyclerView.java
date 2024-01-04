package com.example.personal_finance_project;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessRecyclerView extends AsyncTask<String, Void, List<ShoppingItem>> {
    private WeakReference<Context> contextReference;
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private String queryType;

    public DatabaseAccessRecyclerView(Context context, RecyclerView recyclerView, ShoppingListAdapter adapter, String queryType) {
        this.contextReference = new WeakReference<>(context);
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.queryType = queryType;
    }

    @Override
    protected List<ShoppingItem> doInBackground(String... userEmails) {
        List<ShoppingItem> dataList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://personal-finance-free.ctthoc6soess.us-east-2.rds.amazonaws.com:3306/finance";
            String username = "admin";
            String password = "Chufa4677";

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            if ("group_tab".equals(queryType)) {
                handleGroupTabQuery(userEmails[0], dataList, connection);
            } else if ("another_tab".equals(queryType)) {
                handleAnotherTabQuery(userEmails[0], dataList, connection);
            } else {
                Log.e("MyApp", "Unknown query type: " + queryType);
            }

            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("MyApp", "Exception in doInBackground: " + e.getMessage());
        }

        return dataList;
    }


    @Override
    protected void onPostExecute(List<ShoppingItem> result) {
        //super.onPostExecute(result);
        try {
            Context context = contextReference.get();

            if (context != null) {
                Log.d("MyApp", "Updating adapter with result size: " + result.size());
                adapter.setItems(result);
                recyclerView.post(() -> adapter.notifyDataSetChanged());

                Log.d("MyApp", "Adapter updated successfully");
            } else {
                Log.e("MyApp", "Data retrieval failed or returned an empty list");
                Toast.makeText(context, "Data retrieval failed", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            Log.e("MyApp", "Normal flow");
        }
    }


    // Implement your query handling methods similar to the ListView version
    // ...

    private void handleGroupTabQuery(String userEmail, List<ShoppingItem> dataList, Connection connection) throws SQLException {
        try {
            String emailQuery = "SELECT group_id FROM appuser WHERE email=?";
            try (PreparedStatement emailStatement = connection.prepareStatement(emailQuery)) {
                emailStatement.setString(1, userEmail);
                ResultSet emailResult = emailStatement.executeQuery();

                if (emailResult.next()) {
                    int groupId = emailResult.getInt("group_id");
                    Log.d("MyApp", "Group ID for Email " + userEmail + ": " + groupId);

                    String itemQuery = "SELECT * FROM items WHERE group_id=? ORDER BY group_id";
                    try (PreparedStatement itemStatement = connection.prepareStatement(itemQuery)) {
                        itemStatement.setInt(1, groupId);
                        ResultSet resultSet = itemStatement.executeQuery();

                        while (resultSet.next()) {
                            int itemId = resultSet.getInt("item_id");
                            String itemName = resultSet.getString("name");

                            // Handle null values for price and quantity
                            double price = resultSet.getDouble("price");
                            if (resultSet.wasNull()) {
                                price = 0.0; // Default value for price when it is null
                            }

                            int quantity = resultSet.getInt("quantity");
                            if (resultSet.wasNull()) {
                                quantity = 0; // Default value for quantity when it is null
                            }

                            // Create a ShoppingItem object and add it to the dataList
                            ShoppingItem item = new ShoppingItem(itemId, groupId, itemName, price, quantity);
                            dataList.add(item);
                        }


                        // Update the adapter to reflect changes in dataList
                        recyclerView.post(() -> adapter.notifyDataSetChanged());

                        Log.d("MyApp", "Adapter updated successfully");
                    }
                }

                emailResult.close();
            }
        } catch (SQLException e) {
            Log.e("MyApp", "Error in handleGroupTabQuery: " + e.getMessage());
            throw e; // rethrow the exception after logging
        }
    }



    private void handleAnotherTabQuery(String userEmail, List<ShoppingItem> dataList, Connection connection) {
        // Handle another_tab query
        // ...
    }
}
