package gitlin.kothub.github.api

class GithubApiException(val errors: List<String>, val statusCode: Int) : Exception() {

    constructor(message: String, statusCode: Int) : this(listOf(message), statusCode)
}