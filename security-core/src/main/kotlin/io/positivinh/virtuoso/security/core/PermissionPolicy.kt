package io.positivinh.virtuoso.security.core

/**
 * Access rules for one or more target types, declared by a domain module.
 *
 * `hasPermission(targetId, targetType, operation)` expressions are resolved by asking every policy that
 * [supports] the target type. Access is granted only when at least one policy supports it and all of them grant it.
 */
interface PermissionPolicy {

    /**
     * Whether this policy decides access to targets of [targetType].
     */
    fun supports(targetType: String): Boolean

    /**
     * Whether [requester] may perform [operation] on the target of type [targetType] identified by [targetId].
     * Implementations must fail closed: unknown operations are denied.
     */
    fun isGranted(requester: Requester, targetType: String, targetId: String, operation: String): Boolean
}
