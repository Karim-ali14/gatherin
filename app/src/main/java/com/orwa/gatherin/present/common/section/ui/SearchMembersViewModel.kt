///*
// * Copyright (C) 2018 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.orwa.gatherin.present.common.section.ui
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.paging.PagingData
//import androidx.paging.cachedIn
//import androidx.paging.insertSeparators
//import androidx.paging.map
//import com.orwa.gatherin.model.section.Member
//import com.orwa.gatherin.present.common.section.data.GithubRepository
//import com.orwa.gatherin.present.common.section.model.Repo
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.distinctUntilChanged
//import kotlinx.coroutines.flow.distinctUntilChangedBy
//import kotlinx.coroutines.flow.filterIsInstance
//import kotlinx.coroutines.flow.flatMapLatest
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.shareIn
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
///**
// * ViewModel for the [SearchRepositoriesActivity] screen.
// * The ViewModel works with the [GithubRepository] to get the data.
// */
//class SearchMembersViewModel(
//    private val repository: GithubRepository,
//) : ViewModel() {
//
//    /**
//     * Stream of immutable states representative of the UI.
//     */
//    val state: StateFlow<UiState>
//
//    /**
//     * Processor of side effects from the UI which in turn feedback into [state]
//     */
//    val accept: (UiAction) -> Unit
//
//    init {
////        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
////        val lastQueryScrolled: String = savedStateHandle.get(LAST_QUERY_SCROLLED) ?: DEFAULT_QUERY
//        val actionStateFlow = MutableSharedFlow<UiAction>()
//        val searches = actionStateFlow
//            .filterIsInstance<UiAction.Search>()
//            .distinctUntilChanged()
//            .onStart { emit(UiAction.Search(DEFAULT_DEPT_ID)) }
//        val queriesScrolled = actionStateFlow
//            .filterIsInstance<UiAction.Scroll>()
//            .distinctUntilChanged()
//            // This is shared to keep the flow "hot" while caching the last query scrolled,
//            // otherwise each flatMapLatest invocation would lose the last query scrolled,
//            .shareIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
//                replay = 1
//            )
//            .onStart { emit(UiAction.Scroll(DEFAULT_DEPT_ID)) }
//
//        state = searches
//            .flatMapLatest { search ->
//                combine(
//                    queriesScrolled,
//                    searchRepo(departmentId = search.departmentId),
//                    ::Pair
//                )
//                    // Each unique PagingData should be submitted once, take the latest from
//                    // queriesScrolled
//                    .distinctUntilChangedBy { it.second }
//                    .map { (scroll, pagingData) ->
//                        UiState(
//                            departmentId = search.departmentId,
//                            pagingData = pagingData,
//                            currentDepartmentId = scroll.currentDepartmentId,
//                            // If the search query matches the scroll query, the user has scrolled
//                            hasNotScrolledForCurrentSearch = search.departmentId != scroll.currentDepartmentId
//                        )
//                    }
//            }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
//                initialValue = UiState()
//            )
//
//        accept = { action ->
//            viewModelScope.launch { actionStateFlow.emit(action) }
//        }
//    }
//
////    override fun onCleared() {
////        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
////        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
////        super.onCleared()
////    }
//
//    private fun searchRepo(departmentId: Int): Flow<PagingData<UiModel>> =
//        repository.getSearchResultStream(departmentId)
//            .map { pagingData -> pagingData.map { UiModel.RepoItem(it) } }
//            .map {
//                it.insertSeparators { before, after ->
//                    if (after == null) {
//                        // we're at the end of the list
//                        return@insertSeparators null
//                    }
//
////                    if (before == null) {
////                        // we're at the beginning of the list
////                        return@insertSeparators UiModel.SeparatorItem("${after.roundedStarCount}0.000+ stars")
////                    }
//                    // check between 2 items
//                    if (true) {
//                            UiModel.SeparatorItem("< 10.000+ stars")
//                    }
//                    else {
//                        // no separator
//                        null
//                    }
//                }
//            }
//            .cachedIn(viewModelScope)
//}
//
//sealed class UiAction {
//    data class Search(val departmentId: Int) : UiAction()
//    data class Scroll(val currentDepartmentId: Int) : UiAction()
//}
//
//data class UiState(
//    val departmentId: Int = DEFAULT_DEPT_ID,
//    val currentDepartmentId: Int = DEFAULT_DEPT_ID,
//    val hasNotScrolledForCurrentSearch: Boolean = false,
//    val pagingData: PagingData<UiModel> = PagingData.empty()
//)
//
//sealed class UiModel {
//    data class RepoItem(val repo: Member) : UiModel()
//    data class SeparatorItem(val description: String) : UiModel()
//}
//
////private val UiModel.RepoItem.roundedStarCount: Int
////    get() = this.repo.stars / 10_000
//
//private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
//private const val LAST_SEARCH_QUERY: String = "last_search_query"
//private const val DEFAULT_QUERY = "Android"
//private const val DEFAULT_DEPT_ID = -1