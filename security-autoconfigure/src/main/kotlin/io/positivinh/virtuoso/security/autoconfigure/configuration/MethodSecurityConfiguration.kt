package io.positivinh.virtuoso.security.autoconfigure.configuration

import io.positivinh.virtuoso.security.core.PermissionPolicy
import io.positivinh.virtuoso.security.core.RequesterProvider
import io.positivinh.virtuoso.security.spring.PolicyPermissionEvaluator
import io.positivinh.virtuoso.security.spring.SecurityContextRequesterProvider
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

/**
 * Method security for any application type (web, batch, messaging), with `hasPermission(...)` resolved by the
 * application's [PermissionPolicy] beans.
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class MethodSecurityConfiguration {

    /**
     * Permission evaluator used by `hasPermission(...)` expressions. Declare a bean with the same name to replace it.
     */
    @Bean(name = [PERMISSION_EVALUATOR_BEAN_NAME])
    @ConditionalOnMissingBean(name = [PERMISSION_EVALUATOR_BEAN_NAME])
    fun appCustomPermissionEvaluator(policies: ObjectProvider<PermissionPolicy>): PermissionEvaluator {

        return PolicyPermissionEvaluator(policies.orderedStream().toList())
    }

    @Bean
    @ConditionalOnMissingBean
    fun methodSecurityExpressionHandler(
        @Qualifier(PERMISSION_EVALUATOR_BEAN_NAME) permissionEvaluator: PermissionEvaluator
    ): MethodSecurityExpressionHandler {

        return DefaultMethodSecurityExpressionHandler()
            .apply { setPermissionEvaluator(permissionEvaluator) }
    }

    @Bean
    @ConditionalOnMissingBean
    fun requesterProvider(): RequesterProvider {

        return SecurityContextRequesterProvider()
    }

    companion object {

        const val PERMISSION_EVALUATOR_BEAN_NAME = "appCustomPermissionEvaluator"
    }
}
