package org.example.db.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * Record class for the K-PAC Sets in-memory.
 * @param id ID of the K-PAC Set and primary key in the database.
 * Ignored for {@link KPacSet} that are not yet inserted into the database.
 * @param title Title of the K-PAc Set.
 * @param kPacIds List if integers that correspond to {@code ids} of {@link KPac}. May be ignored when inessential.
 */
public record KPacSet(
        @NonNull int id,
        @Nullable String title,
        @NonNull @JsonProperty("kpacs") List<Integer> kPacIds
) {
    public KPacSet(int id, @Nullable String title) {
        this(id, title, new LinkedList<>());
    }

    public KPacSet(@Nullable String title) {
        this(0, title);
    }

    public KPacSet(int id, KPacSet copyFrom) {
        this(id, copyFrom.title(), copyFrom.kPacIds());
    }

}
