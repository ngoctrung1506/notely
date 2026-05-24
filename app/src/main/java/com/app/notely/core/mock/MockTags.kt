package com.app.notely.core.mock

import com.app.notely.domain.model.Tag

/**
 * Provides mock tag data for the app.
 * Tags are not persisted in the database; this singleton serves as the single source of truth
 * for all mock tag data across the app.
 */
object MockTags {

    val all: List<Tag> = listOf(
        Tag(1, "Work",     "#1565C0"),
        Tag(2, "Personal", "#2E7D32"),
        Tag(3, "Ideas",    "#E65100"),
        Tag(4, "Finance",  "#6A1B9A"),
        Tag(5, "Travel",   "#C62828"),
        Tag(6, "Health",   "#00838F"),
        Tag(7, "Learning", "#AD1457"),
        Tag(8, "Shopping", "#4E342E")
    )

}
