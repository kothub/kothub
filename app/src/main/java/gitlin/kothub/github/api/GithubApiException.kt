package gitlin.kothub.github.api

class GithubApiException(val errors: List<String>) : Exception()