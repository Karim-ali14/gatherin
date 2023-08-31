package com.orwa.gatherin.model.group_results

import com.orwa.gatherin.model.teacher_home.Group


data class Value(
    val group: Group,
    var mark: Int?
)