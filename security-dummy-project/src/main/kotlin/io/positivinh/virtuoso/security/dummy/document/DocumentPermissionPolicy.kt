package io.positivinh.virtuoso.security.dummy.document

import io.positivinh.virtuoso.security.core.PermissionPolicy
import io.positivinh.virtuoso.security.core.Requester
import org.springframework.stereotype.Component

/**
 * Admins may read any document; other callers only their own.
 */
@Component
class DocumentPermissionPolicy : PermissionPolicy {

    override fun supports(targetType: String): Boolean = targetType == DocumentPermissionConstants.DOCUMENT

    override fun isGranted(requester: Requester, targetType: String, targetId: String, operation: String): Boolean {

        if (requester.hasAnyAuthority(DocumentPermissionConstants.ROLE_ADMIN)) {
            return true
        }

        return when (operation) {
            DocumentPermissionConstants.READ -> requester.username == targetId
            else -> false
        }
    }
}
