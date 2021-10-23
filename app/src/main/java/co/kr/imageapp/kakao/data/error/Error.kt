package co.kr.imageapp.kakao.data.error

class Error(val code: Int, val description: String) {
    constructor(exception: Exception) : this(code = DEFAULT_ERROR, description = exception.message
        ?: "")
}

const val NO_INTERNET_CONNECTION = -1
const val NETWORK_ERROR = -2
const val DEFAULT_ERROR = -3
const val CHECK_YOUR_FIELDS = -103
const val SEARCH_ERROR = -104
const val INSERT_ERROR = -105
const val SELECT_ERROR = -106
const val DELET_ERROR = -107