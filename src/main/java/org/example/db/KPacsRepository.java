package org.example.db;

import org.example.db.records.KPac;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Repository class that handles operations with K-PACs in Database.
 * Uses {@link SqlUpdate} and {@link SqlQuery} from {@link SqlOperationsConfiguration}.
 * Is transactional.
 * @see SqlOperationsConfiguration
 */
@Repository
@Transactional
public class KPacsRepository {
    @Resource(name = "allKPacsQuery")
    private SqlQuery<KPac> allKPacsQuery;
    @Resource(name = "kPacsInSetQuery")
    private SqlQuery<KPac> kPacsInSetQuery;
    @Resource(name = "kPacByIdQuery")
    private SqlQuery<KPac> kPacByIdQuery;
    @Resource(name = "addKPacQuery")
    private SqlUpdate addKPacQuery;
    @Resource(name = "deleteKPacQuery")
    private SqlUpdate deleteKPacQuery;

    /**
     * Executes query for all K-PACs in the database.
     */
    public List<KPac> getAll() {
        return allKPacsQuery.execute();
    }

    /**
     * Executes query for K-PACs within certain K-PAc Set.
     * @param id {@code id} of the K-PAC Set.
     */
    public List<KPac> getBySet(int id) {
        return kPacsInSetQuery.executeByNamedParam(Map.of("id", id));
    }

    /**
     * Executes query for K-PAC by ID.
     * @param id {@code id} of the K-PAC.
     */
    public KPac getById(int id) {
        return kPacByIdQuery.findObjectByNamedParam(Map.of("id", id));
    }

    /**
     * Inserts a new K-PAC to the database, and retrieves generated {@code id} and {@code creationDate}.
     * @param kPac {@link KPac} to be added. {@code id} and {@code creationDate} are ignored.
     * @return new {@link KPac} with generated {@code id} and {@code creationDate}.
     */
    public KPac add(KPac kPac) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", kPac.title());
        map.put("description", kPac.description());

        var keyHolder = new GeneratedKeyHolder();
        addKPacQuery.updateByNamedParam(map, keyHolder);

        return getById(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    /**
     * Deletes a K-PAC by {@code id}.
     * @param id id of the {@link KPac}.
     * @return {@code true}, when exactly 1 K-PAC was deleted.
     */
    public boolean delete(int id) {
        int numberRowsDeleted = deleteKPacQuery.updateByNamedParam(Map.of("id", id));

        return numberRowsDeleted == 1;
    }
}
