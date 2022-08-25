/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package sharding.test.repository;


import javax.sql.DataSource;
import java.sql.*;

public class RawJdbcRepository {

    private final DataSource dataSource;

    public RawJdbcRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void demo() throws SQLException {
        insertDataTest();
        // dropTable();
    }

    public void demoRead() throws SQLException {
        readDataTest();
        // dropTable();
    }

    private void readDataTest() throws SQLException {
        for (int i = 1; i < 2; i++) {
            // executeRead(dataSource, "select *  from  t_order where order_id = 475720725806710785");
            executeRead(dataSource, "select * from  t_order where order_id in(475720725806710785,475720726045786112) order by user_id desc");

        }
    }

    private void executeRead(DataSource dataSource, String sql) {
        try {
            Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.print("order_id:" + resultSet.getLong(1) + ", ");
                System.out.print("user_id:" + resultSet.getLong(2) + ", ");
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertDataTest() throws SQLException {
        for (int i = 1; i < 10; i++) {
            executeAndGetGeneratedKey(dataSource, String.format("INSERT INTO t_order (user_id, status) VALUES (%s, 'INIT')", i + ""));
        }
    }


    public void dropTable() throws SQLException {
        execute(dataSource, "DROP TABLE t_order_item");
        execute(dataSource, "DROP TABLE t_order");
    }


    private void execute(final DataSource dataSource, final String sql) throws SQLException {
        try (
                Connection conn = dataSource.getConnection();
                Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
    }

    private long executeAndGetGeneratedKey(final DataSource dataSource, final String sql) throws SQLException {
        long result = -1;
        try (
                Connection conn = dataSource.getConnection();
                Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getLong(1);
            }
        }
        return result;
    }
}
