package org.example.db;

import org.example.db.records.KPac;
import org.example.db.records.KPacSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.*;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;

/**
 * Defines {@link SqlQuery} and {@link SqlUpdate} for use in {@link org.springframework.stereotype.Repository} classes.
 * @see KPacsRepository
 * @see SetsRepository
 */
@Configuration
public class SqlOperationsConfiguration {
    private final DataSource dataSource;

    @Autowired
    public SqlOperationsConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private KPac kPacFromResultSet(ResultSet resultSet) throws SQLException{
        return new KPac(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                Optional.ofNullable(resultSet.getDate(4))
                        .map(Date::toLocalDate).orElse(null)
        );
    }

    @Bean
    public SqlQuery<KPac> allKPacsQuery() {
        var sqlQuery = new MappingSqlQuery<KPac>(dataSource,
                "select * from kpacs"
        ) {
            @Override
            protected KPac mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return kPacFromResultSet(resultSet);
            }
        };
        sqlQuery.compile();
        return sqlQuery;
    }

    @Bean
    public SqlQuery<KPacSet> allSetsQuery() {
        var sqlQuery = new MappingSqlQuery<KPacSet>(dataSource,
                "select * from kpac_sets"
        ) {
            @Override
            protected KPacSet mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return new KPacSet(
                        resultSet.getInt(1),
                        resultSet.getString(2)
                );
            }
        };
        sqlQuery.compile();
        return sqlQuery;
    }

    @Bean
    public SqlQuery<KPac> kPacsInSetQuery() {
        var sqlQuery = new MappingSqlQueryWithParameters<KPac>(dataSource,
                "select kpacs.id, title, description, creationDate from kpacs " +
                        "join kpac_set_connections ksi on kpacs.id = ksi.kpac_id " +
                        "where ksi.set_id = :id;"
        ) {
            @Override
            protected KPac mapRow(ResultSet resultSet, int rowNum,
                                  Object[] parameters, Map<?, ?> context) throws SQLException {
                return kPacFromResultSet(resultSet);
            }
        };
        sqlQuery.declareParameter(new SqlParameter(Types.INTEGER));
        sqlQuery.compile();
        return sqlQuery;
    }

    @Bean
    public SqlQuery<KPac> kPacByIdQuery() {
        var sqlQuery = new MappingSqlQueryWithParameters<KPac>(dataSource,
                "select * from kpacs where id = :id;"
        ) {
            @Override
            protected KPac mapRow(ResultSet resultSet, int rowNum,
                                  Object[] parameters, Map<?, ?> context) throws SQLException {
                return kPacFromResultSet(resultSet);
            }
        };
        sqlQuery.declareParameter(new SqlParameter(Types.INTEGER));
        sqlQuery.setRowsExpected(1);
        sqlQuery.compile();
        return sqlQuery;
    }

    @Bean
    public SqlUpdate addKPacQuery() {
        var sqlUpdate = new SqlUpdate(
                dataSource,
                "insert into kpacs(title, description) " +
                        "values (:title, :description)",
                new int[]{Types.VARCHAR, Types.VARCHAR}, 1
        );
        sqlUpdate.setReturnGeneratedKeys(true);
        sqlUpdate.compile();
        return sqlUpdate;
    }

    @Bean
    public SqlUpdate deleteKPacQuery() {
        var sqlUpdate = new SqlUpdate(
                dataSource, "delete from kpacs where id = :id",
                new int[]{Types.INTEGER}, 1
        );
        sqlUpdate.compile();
        return sqlUpdate;
    }

    @Bean
    public SqlUpdate addSetQuery() {
        var sqlUpdate = new SqlUpdate(
                dataSource,
                "insert into kpac_sets(title) values (:title)",
                new int[]{Types.VARCHAR}, 1
        );
        sqlUpdate.setReturnGeneratedKeys(true);
        sqlUpdate.compile();
        return sqlUpdate;
    }

    @Bean
    public SqlUpdate deleteSetQuery() {
        var sqlUpdate = new SqlUpdate(
                dataSource, "delete from kpac_sets where id = :id",
                new int[]{Types.INTEGER}, 1
        );
        sqlUpdate.compile();
        return sqlUpdate;
    }

    @Bean
    public BatchSqlUpdate addMultipleKPacSetConnectionsQuery() {
        var sqlUpdate = new BatchSqlUpdate(
                dataSource,
                "insert into kpac_set_connections(kpac_id, set_id) values (:kpac_id, :set_id)",
                new int[]{Types.INTEGER, Types.INTEGER}
        );
        sqlUpdate.setMaxRowsAffected(1);
        sqlUpdate.compile();
        return sqlUpdate;
    }
}
