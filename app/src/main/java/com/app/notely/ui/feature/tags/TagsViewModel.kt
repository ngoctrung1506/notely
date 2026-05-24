package com.app.notely.ui.feature.tags

import androidx.lifecycle.ViewModel
import com.app.notely.core.mock.MockTags
import com.app.notely.domain.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor() : ViewModel() {
    val tags: List<Tag> = MockTags.all
}
