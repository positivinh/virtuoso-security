package io.positivinh.virtuoso.security.spring

import io.positivinh.virtuoso.security.core.Requester
import io.positivinh.virtuoso.security.core.RequesterProvider
import org.springframework.security.core.context.SecurityContextHolder

/**
 * [RequesterProvider] backed by Spring Security's [SecurityContextHolder].
 */
class SecurityContextRequesterProvider : RequesterProvider {

    override fun currentRequester(): Requester? {

        return SecurityContextHolder.getContext().authentication?.toRequester()
    }
}
