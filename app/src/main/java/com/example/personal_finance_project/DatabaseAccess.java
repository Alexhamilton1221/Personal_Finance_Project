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
        List<String> dataList = new ArrayList<>();

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String jdbcUrl = "jdbc:jtds:sqlserver://personal-finance.ctthoc6soess.us-east-2.rds.amazonaws.com:1433;databaseName=finance_objects";
            String username = "admin";
            String password = "Chufa4677";

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a Statement
            Statement statement = connection.createStatement();

            // Execute the SELECT query
            String sqlQuery = "SELECT * FROM dbo.[user] ORDER BY group_id";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // Process the result set and populate the dataList
            while (resultSet.next()) {
                String userEmail = resultSet.getString("email");
                int groupId = resultSet.getInt("group_id");

                String item = "Email: " + userEmail + ", Group ID: " + groupId;
                dataList.add(item);
            }

            // Close resources
            resultSet.close();
            statement.close();
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

