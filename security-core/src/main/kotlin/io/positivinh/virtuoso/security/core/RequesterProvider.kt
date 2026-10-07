package io.positivinh.virtuoso.security.core

/**
 * Gives domain code access to the caller of the current use case.
 */
interface RequesterProvider {

    /**
     * The caller of the current use case, or `null` when it is anonymous.
     */
    fun currentRequester(): Requester?
}
