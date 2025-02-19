package jeonghwan.app.favorite.ui.theme.root.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jeonghwan.app.favorite.domain.usecase.ContentUseCaseInterface
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val contentUseCaseImpl: ContentUseCaseInterface
) : ViewModel() {

}