package io.positivinh.virtuoso.security.spring

import com.crabshue.commons.kotlin.logging.getLogger
import io.positivinh.virtuoso.security.core.PermissionPolicy
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import java.io.Serializable

/**
 * Resolves `hasPermission(targetId, targetType, operation)` by delegating to the [PermissionPolicy] beans.
 *
 * Fails closed: access is denied when the caller is anonymous, when no policy supports the target type, or when any
 * supporting policy denies it. `hasPermission(domainObject, operation)` is not supported and always denied.
 */
class PolicyPermissionEvaluator(
    private val policies: List<PermissionPolicy>
) : PermissionEvaluator {

    private val log = getLogger()

    override fun hasPermission(
        authentication: Authentication,
        targetDomainObject: Any?,
        permission: Any
    ): Boolean {

        return false
    }

    override fun hasPermission(
        authentication: Authentication,
        targetId: Serializable,
        targetType: String,
        permission: Any
    ): Boolean {

        val requester = authentication.toRequester()
        val supportingPolicies = policies.filter { it.supports(targetType) }

        val granted = requester != null
                && supportingPolicies.isNotEmpty()
                && supportingPolicies.all {
            it.isGranted(requester, targetType, targetId.toString(), permission.toString())
        }

        return granted
            .also {
                log.debug(
                    "Evaluated permission [{}] for [{}] on [{}] with id [{}] for [{}]",
                    it, permission, targetType, targetId, authentication.name
                )
            }
    }
}
