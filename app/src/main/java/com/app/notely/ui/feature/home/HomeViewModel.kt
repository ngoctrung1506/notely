package com.app.notely.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.notely.domain.model.Note
import com.app.notely.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    // TODO: update this after finishing Add note feature
    val pagedNotes: Flow<PagingData<Note>> = flowOf(PagingData.from(mockNotes))
        .cachedIn(viewModelScope)

    companion object {
        private val now = System.currentTimeMillis()
        private const val DAY = 86_400_000L

        val mockNotes = listOf(
            Note(
                id = 6,
                title = "Meeting Notes: Backend Architecture",
                content = "Discussion on moving to a modular microservices approach for the tagging engine. Need to ensure low latency for global search.",
                createdAt = now,
                updatedAt = now,
                color = "#B2EBF2"
            ),
            Note(
                id = 5,
                title = "Vision Statement",
                content = "Empowering creatives to build better mental models through organized, distraction-free digital journaling and systematic thinking.",
                createdAt = now - DAY,
                updatedAt = now - DAY,
                color = "#E1BEE7"
            ),
            Note(
                id = 4,
                title = "Coffee Brewing Guide",
                content = "V60 method: 20g coffee to 300g water. Bloom for 45s. Total brew time around 3 minutes. Aim for 94°C.",
                createdAt = now - 2 * DAY,
                updatedAt = now - 2 * DAY,
                color = "#FFCCBC"
            ),
            Note(
                id = 3,
                title = "Design System Philosophy",
                content = "Why systematic quietness matters. Interfaces should recede, letting content lead. High-quality typography is the soul of UI.",
                createdAt = now - 3 * DAY,
                updatedAt = now - 3 * DAY,
                color = "#C8E6C9"
            ),
            Note(
                id = 2,
                title = "Grocery List",
                content = "Organic kale, almond milk, sourdough starter, sea salt, dark chocolate (85%), fresh basil, cherry tomatoes.",
                createdAt = now - 4 * DAY,
                updatedAt = now - 4 * DAY,
                color = "#B3E5FC"
            ),
            Note(
                id = 1,
                title = "Product Roadmap 2024",
                content = "Focusing on deep work features and cognitive ease. Key milestones include offline mode and improved search indexing for large collections.",
                createdAt = now - 5 * DAY,
                updatedAt = now - 5 * DAY,
                color = "#FFF9C4"
            )
        )
    }
}
