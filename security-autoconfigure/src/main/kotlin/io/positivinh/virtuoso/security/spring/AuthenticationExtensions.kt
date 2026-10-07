package io.positivinh.virtuoso.security.spring

import io.positivinh.virtuoso.security.core.Requester
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication

/**
 * The [Requester] for this authentication, or `null` when it is anonymous or not authenticated.
 */
fun Authentication.toRequester(): Requester? {

    if (!isAuthenticated || this is AnonymousAuthenticationToken) {
        return null
    }

    return Requester(
        username = name,
        authorities = authorities.mapNotNull { it.authority }.toSet()
    )
}
