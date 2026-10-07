package io.positivinh.virtuoso.security.spring

import io.positivinh.virtuoso.security.core.PermissionPolicy
import io.positivinh.virtuoso.security.core.Requester
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils

class PolicyPermissionEvaluatorTest {

    private val authentication = UsernamePasswordAuthenticationToken.authenticated(
        OWNER, null, AuthorityUtils.createAuthorityList("ROLE_USER")
    )

    @Test
    fun hasPermission_whenPolicyGrants() {

        val evaluator = PolicyPermissionEvaluator(listOf(OwnerPolicy(TARGET_TYPE)))

        Assertions.assertThat(evaluator.hasPermission(authentication, OWNER, TARGET_TYPE, OPERATION)).isTrue()
    }

    @Test
    fun hasPermission_whenPolicyDenies_reject() {

        val evaluator = PolicyPermissionEvaluator(listOf(OwnerPolicy(TARGET_TYPE)))

        Assertions.assertThat(evaluator.hasPermission(authentication, "other", TARGET_TYPE, OPERATION)).isFalse()
    }

    @Test
    fun hasPermission_whenNoPolicySupportsTarget_reject() {

        val evaluator = PolicyPermissionEvaluator(listOf(OwnerPolicy("OTHER_TARGET")))

        Assertions.assertThat(evaluator.hasPermission(authentication, OWNER, TARGET_TYPE, OPERATION)).isFalse()
    }

    @Test
    fun hasPermission_whenNoPolicy_reject() {

        val evaluator = PolicyPermissionEvaluator(emptyList())

        Assertions.assertThat(evaluator.hasPermission(authentication, OWNER, TARGET_TYPE, OPERATION)).isFalse()
    }

    @Test
    fun hasPermission_whenOneSupportingPolicyDenies_reject() {

        val denyAll = object : PermissionPolicy {
            override fun supports(targetType: String) = targetType == TARGET_TYPE
            override fun isGranted(requester: Requester, targetType: String, targetId: String, operation: String) =
                false
        }
        val evaluator = PolicyPermissionEvaluator(listOf(OwnerPolicy(TARGET_TYPE), denyAll))

        Assertions.assertThat(evaluator.hasPermission(authentication, OWNER, TARGET_TYPE, OPERATION)).isFalse()
    }

    @Test
    fun hasPermission_whenAnonymous_reject() {

        val anonymous =
            AnonymousAuthenticationToken("key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"))
        val evaluator = PolicyPermissionEvaluator(listOf(OwnerPolicy(TARGET_TYPE)))

        Assertions.assertThat(evaluator.hasPermission(anonymous, OWNER, TARGET_TYPE, OPERATION)).isFalse()
    }

    @Test
    fun hasPermission_onDomainObject_reject() {

        val evaluator = PolicyPermissionEvaluator(listOf(OwnerPolicy(TARGET_TYPE)))

        Assertions.assertThat(evaluator.hasPermission(authentication, Any(), OPERATION)).isFalse()
    }

    private class OwnerPolicy(private val supportedType: String) : PermissionPolicy {

        override fun supports(targetType: String) = targetType == supportedType

        override fun isGranted(requester: Requester, targetType: String, targetId: String, operation: String) =
            requester.username == targetId
    }

    companion object {

        private const val OWNER = "owner"
        private const val TARGET_TYPE = "DOCUMENT"
        private const val OPERATION = "READ"
    }
}
