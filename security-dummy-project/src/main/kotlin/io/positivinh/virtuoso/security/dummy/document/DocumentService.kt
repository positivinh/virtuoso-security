package io.positivinh.virtuoso.security.dummy.document

import org.springframework.security.access.prepost.PreAuthorize

interface DocumentService {

    @PreAuthorize("hasPermission(#owner, '${DocumentPermissionConstants.DOCUMENT}', '${DocumentPermissionConstants.READ}')")
    fun readDocument(owner: String): String

    /**
     * Protected by a target type no policy supports: always denied.
     */
    @PreAuthorize("hasPermission(#owner, 'UNKNOWN', '${DocumentPermissionConstants.READ}')")
    fun readUnknown(owner: String): String

    /**
     * The username of the caller, read through the RequesterProvider.
     */
    fun whoAmI(): String?
}
