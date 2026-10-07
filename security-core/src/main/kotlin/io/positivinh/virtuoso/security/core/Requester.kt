package io.positivinh.virtuoso.security.core

/**
 * The authenticated caller of a use case, independent of any security framework.
 *
 * @property username the caller's username
 * @property authorities the caller's authorities, with the Spring `ROLE_` prefix (e.g. `ROLE_ADMIN`)
 */
data class Requester(

    val username: String,

    val authorities: Set<String> = emptySet()
) {

    fun hasAnyAuthority(vararg authorities: String): Boolean = authorities.any { it in this.authorities }
}
