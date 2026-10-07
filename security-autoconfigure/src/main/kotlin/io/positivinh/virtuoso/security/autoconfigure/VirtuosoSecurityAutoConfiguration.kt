package io.positivinh.virtuoso.security.autoconfigure

import io.positivinh.virtuoso.security.autoconfigure.configuration.MethodSecurityConfiguration
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

/**
 * Enables method security and policy-based permission evaluation. Does not require a web application.
 */
@AutoConfiguration
@ConditionalOnClass(EnableMethodSecurity::class)
@Import(MethodSecurityConfiguration::class)
class VirtuosoSecurityAutoConfiguration
