package com.orwa.gatherin.present.common.contact

import com.orwa.gatherin.model.chat.Option
import com.orwa.gatherin.model.chat.Question

interface TextQuestionClickListener {
    fun onQuestionClicked(
        question: Question,
        option: Option,
        itemPos: Int,
        optionPos: Int,
        callBack: (itemPos: Int, optionPos: Int, wasSelected: Boolean) -> Unit
    )
}