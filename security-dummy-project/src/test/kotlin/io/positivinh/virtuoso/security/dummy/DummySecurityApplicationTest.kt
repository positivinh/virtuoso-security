package io.positivinh.virtuoso.security.dummy

import io.positivinh.virtuoso.security.dummy.document.DocumentService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class DummySecurityApplicationTest {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var documentService: DocumentService

    @Test
    fun isNotAWebApplication() {

        // spring-web is on the test classpath only (through spring-security-test)
        Assertions.assertThat(applicationContext).isNotInstanceOf(WebApplicationContext::class.java)
    }

    @Test
    @WithMockUser(username = OWNER)
    fun owner() {

        Assertions.assertThat(documentService.readDocument(OWNER)).isEqualTo("document of $OWNER")
    }

    @Test
    @WithMockUser(username = "other")
    fun whenOtherUser_reject() {

        Assertions.assertThatThrownBy { documentService.readDocument(OWNER) }
            .isInstanceOf(AccessDeniedException::class.java)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun roleAdmin() {

        Assertions.assertThat(documentService.readDocument(OWNER)).isEqualTo("document of $OWNER")
    }

    @Test
    fun unauthenticated_reject() {

        Assertions.assertThatThrownBy { documentService.readDocument(OWNER) }
            .isInstanceOf(AuthenticationCredentialsNotFoundException::class.java)
    }

    @Test
    @WithMockUser(username = OWNER)
    fun unknownTargetType_reject() {

        Assertions.assertThatThrownBy { documentService.readUnknown(OWNER) }
            .isInstanceOf(AccessDeniedException::class.java)
    }

    @Test
    @WithMockUser(username = OWNER)
    fun requesterProvider() {

        Assertions.assertThat(documentService.whoAmI()).isEqualTo(OWNER)
    }

    companion object {

        private const val OWNER = "owner"
    }
}
