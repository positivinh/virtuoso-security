package io.positivinh.virtuoso.security.spring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder

class SecurityContextRequesterProviderTest {

    private val provider = SecurityContextRequesterProvider()

    @AfterEach
    fun clearContext() {

        SecurityContextHolder.clearContext()
    }

    @Test
    fun currentRequester() {

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken.authenticated(
            "user", null, AuthorityUtils.createAuthorityList("ROLE_ADMIN")
        )

        val requester = provider.currentRequester()

        Assertions.assertThat(requester).isNotNull
        Assertions.assertThat(requester?.username).isEqualTo("user")
        Assertions.assertThat(requester?.authorities).containsExactly("ROLE_ADMIN")
    }

    @Test
    fun currentRequester_whenNoAuthentication() {

        Assertions.assertThat(provider.currentRequester()).isNull()
    }

    @Test
    fun currentRequester_whenAnonymous() {

        SecurityContextHolder.getContext().authentication = AnonymousAuthenticationToken(
            "key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
        )

        Assertions.assertThat(provider.currentRequester()).isNull()
    }

    @Test
    fun currentRequester_whenNotAuthenticated() {

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken.unauthenticated("user", "password")

        Assertions.assertThat(provider.currentRequester()).isNull()
    }
}
