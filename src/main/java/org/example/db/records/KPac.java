package org.example.db.records;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

/**
 * Record class for the K-PACs in-memory.
 * @param id ID of the K-PAC and primary key in the database.
 * Ignored for {@link KPac} that are not yet inserted into the database.
 * @param title Title of the K-PAC.
 * @param description Description of the K-PAC.
 * @param creationDate Creation date of the K-PAC, primary key in the database.
 * Ignored for {@link KPac} that are not yet inserted into the database.
 */
public record KPac(
        @NonNull  int id,
        @Nullable String title,
        @Nullable String description,
        @Nullable LocalDate creationDate
) {
    public KPac(String title, String description, LocalDate creationDate) {
        this(0, title, description, creationDate);
    }

    public KPac(int id, KPac copyFrom) {
        this(id, copyFrom.title(), copyFrom.description(), copyFrom.creationDate());
    }
}
