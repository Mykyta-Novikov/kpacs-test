package org.example.db;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Configures the database by providing {@link DataSource} and {@link TransactionManager}.
 */
@Configuration
@EnableTransactionManagement
public class JdbcConfiguration {
    @Bean
    DataSource dataSource(
            @Value("${database.url}")      String url,
            @Value("${database.username}") String user,
            @Value("${database.password}") String password) {
        var dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}
