package org.example.db;

import org.example.db.records.KPac;
import org.example.db.records.KPacSet;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Repository class that handles operations with K-PAC Sets in Database.
 * Uses {@link SqlUpdate} and {@link SqlQuery} from {@link SqlOperationsConfiguration}.
 * Is transactional.
 * @see SqlOperationsConfiguration
 */
@Repository
@Transactional
public class SetsRepository {
    @Resource(name = "allSetsQuery")
    private SqlQuery<KPacSet> allSetsQuery;
    @Resource(name = "addSetQuery")
    private SqlUpdate addSetQuery;
    @Resource(name = "deleteSetQuery")
    private SqlUpdate deleteSetQuery;
    @Resource(name = "addMultipleKPacSetConnectionsQuery")
    private BatchSqlUpdate addMultipleKPacSetConnectionsQuery;

    /**
     * Executes query for all K-PAC Sets in the database.
     */
    public List<KPacSet> getAll() {
        return allSetsQuery.execute();
    }

    /**
     * Inserts a new K-PAC Set to the database, and retrieves generated {@code id}.
     * Also adds a connection for each K-PAc belonging to this Set.
     * @param set {@link KPacSet} to be added. {@code id} is ignored,
     while {@code kPacIds} are used and must correspond to {@code ids} of existing {@link KPac} in the table.
     * @return new {@link KPacSet} with generated {@code id}.
     */
    public KPacSet add(KPacSet set) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", set.title());

        var keyHolder = new GeneratedKeyHolder();
        addSetQuery.updateByNamedParam(map, keyHolder);

        var addedSet = new KPacSet(Objects.requireNonNull(keyHolder.getKey()).intValue(), set);
        addConnections(addedSet.id(), set.kPacIds());

        return addedSet;
    }

    /**
     * Deletes a K-PAC Set by {@code id}.
     * @param id id of the {@link KPacSet}.
     * @return {@code true}, when exactly 1 K-PAC Set was deleted.
     */
    public boolean delete(int id) {
        int numberRowsDeleted = deleteSetQuery.updateByNamedParam(Map.of("id", id));

        return numberRowsDeleted == 1;
    }

    /**
     * Creates connections between a K-PAc Set and K-PACs from a list.
     * Uses {@link BatchSqlUpdate}, and is synchronised with {@code addMultipleKPacSetConnectionsQuery} as monitor.
     * @param setId {@code id} of the K-PAc Set.
     * @param kPacIds List of {@code ids} of K-PACs.
     * @see BatchSqlUpdate
     */
    public void addConnections(int setId, List<Integer> kPacIds) {
        synchronized (addMultipleKPacSetConnectionsQuery) {
            addMultipleKPacSetConnectionsQuery.reset();
            kPacIds.forEach(kPacId -> addMultipleKPacSetConnectionsQuery
                    .updateByNamedParam(Map.of(
                            "kpac_id", kPacId,
                            "set_id", setId
                    ))
            );
            addMultipleKPacSetConnectionsQuery.flush();
        }
    }
}
