package examples;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SQLExamples {

  public void example1(SQLConnection connection) {
    connection.setAutoCommit(false, res -> {
      if (res.succeeded()) {
        // OK!
      } else {
        // Failed!
      }
    });
  }

  public void example2(SQLConnection connection) {
    connection.query("SELECT ID, FNAME, LNAME, SHOE_SIZE from PEOPLE", res -> {
      if (res.succeeded()) {
        // Get the result set
        ResultSet resultSet = res.result();
      } else {
        // Failed!
      }
    });
  }

  public void example3(ResultSet resultSet) {

    List<String> columnNames = resultSet.getColumnNames();

    List<JsonArray> results = resultSet.getResults();

    for (JsonArray row: results) {

      String id = row.getString(0);
      String fName = row.getString(1);
      String lName = row.getString(2);
      int shoeSize = row.getInteger(3);

    }

  }

  public void example3__1(ResultSet resultSet) {

    List<JsonObject> rows = resultSet.getRows();

    for (JsonObject row: rows) {

      String id = row.getString("ID");
      String fName = row.getString("FNAME");
      String lName = row.getString("LNAME");
      int shoeSize = row.getInteger("SHOE_SIZE");

    }

  }

  public void example3_1(SQLConnection connection) {

    String query = "SELECT ID, FNAME, LNAME, SHOE_SIZE from PEOPLE WHERE LNAME=? AND SHOE_SIZE > ?";
    JsonArray params = new JsonArray().add("Fox").add(9);

    connection.queryWithParams(query, params, res -> {

      if (res.succeeded()) {
        // Get the result set
        ResultSet resultSet = res.result();
      } else {
        // Failed!
      }
    });

  }

  public void example4(SQLConnection connection) {

    connection.update("INSERT INTO PEOPLE VALUES (null, 'john', 'smith', 9)", res -> {
      if (res.succeeded()) {

        UpdateResult result = res.result();
        System.out.println("Updated no. of rows: " + result.getUpdated());
        System.out.println("Generated keys: " + result.getKeys());

      } else {
        // Failed!
      }
    });


  }

  public void example5(SQLConnection connection) {

    String update = "UPDATE PEOPLE SET SHOE_SIZE = 10 WHERE LNAME=?";
    JsonArray params = new JsonArray().add("Fox");

    connection.updateWithParams(update, params, res -> {

      if (res.succeeded()) {

        UpdateResult updateResult = res.result();

        System.out.println("No. of rows updated: " + updateResult.getUpdated());

      } else {

        // Failed!

      }
    });

  }

  public void example6(SQLConnection connection) {

    String sql = "CREATE TABLE PEOPLE (ID int generated by default as identity (start with 1 increment by 1) not null," +
                 "FNAME varchar(255), LNAME varchar(255), SHOE_SIZE int);";

    connection.execute(sql, execute -> {
      if (execute.succeeded()) {
        System.out.println("Table created !");
      } else {
        // Failed!
      }
    });

  }

  public void example7(SQLConnection connection) {

    // Do stuff with connection - updates etc

    // Now commit

    connection.commit(res -> {
      if (res.succeeded()) {
        // Committed OK!
      } else {
        // Failed!
      }
    });

  }

  public void example8(SQLConnection connection) {
    // Assume that there is a SQL function like this:
    //
    // create function one_hour_ago() returns timestamp
    //    return now() - 1 hour;

    // note that you do not need to declare the output for functions
    String func = "{ call one_hour_ago() }";

    connection.call(func, res -> {

      if (res.succeeded()) {
        ResultSet result = res.result();
      } else {
        // Failed!
      }
    });
  }

  public void example9(SQLConnection connection) {
    // Assume that there is a SQL procedure like this:
    //
    // create procedure new_customer(firstname varchar(50), lastname varchar(50))
    //   modifies sql data
    //   insert into customers values (default, firstname, lastname, current_timestamp);

    String func = "{ call new_customer(?, ?) }";

    connection.callWithParams(func, new JsonArray().add("John").add("Doe"), null, res -> {

      if (res.succeeded()) {
        // Success!
      } else {
        // Failed!
      }
    });
  }

  public void example10(SQLConnection connection) {
    // Assume that there is a SQL procedure like this:
    //
    // create procedure customer_lastname(IN firstname varchar(50), OUT lastname varchar(50))
    //   modifies sql data
    //   select lastname into lastname from customers where firstname = firstname;

    String func = "{ call customer_lastname(?, ?) }";

    connection.callWithParams(func, new JsonArray().add("John"), new JsonArray().addNull().add("VARCHAR"), res-> {

      if (res.succeeded()) {
        ResultSet result = res.result();
      } else {
        // Failed!
      }
    });
  }

  public void example11(SQLConnection connection) {
    // Batch values
    List<JsonArray> batch = new ArrayList<>();
    batch.add(new JsonArray().add("joe"));
    batch.add(new JsonArray().add("jane"));

    connection.batchWithParams("INSERT INTO emp (name) VALUES (?)", batch, res -> {
      if (res.succeeded()) {
        List<Integer> result = res.result();
      } else {
        // Failed!
      }
    });
  }

  public void example12(SQLConnection connection) {
    // Batch values
    List<String> batch = new ArrayList<>();
    batch.add("INSERT INTO emp (NAME) VALUES ('JOE')");
    batch.add("INSERT INTO emp (NAME) VALUES ('JANE')");

    connection.batch(batch, res -> {
      if (res.succeeded()) {
        List<Integer> result = res.result();
      } else {
        // Failed!
      }
    });
  }
}
