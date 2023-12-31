package com.orwa.gatherin.base

enum class AuthNetworkState {
    NONE,
    LOADING,
    SUCCESS,
    EMAIL_NOT_FOUND,
    EMAIL_ALREADY_REGISTERED,
    INVALID_CREDENTIALS,
    CONNECT_ERROR,
    FAILURE,
    USER_UNAUTHORIZED,
    SECTION_ALREADY_REGISTERED,
    MALFORMED_INPUT_DATA,
    FORBIDDEN,
    GROUP_ALREADY_REGISTERED,
    GROUP_MEMBERS_FULL,
    DEPARTMENTS_FULL_ERROR,
    DEPARTMENT_ADD_USER_ERROR

}