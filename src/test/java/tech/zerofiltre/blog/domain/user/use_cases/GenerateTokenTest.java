package tech.zerofiltre.blog.domain.user.use_cases;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.junit.jupiter.*;
import tech.zerofiltre.blog.domain.user.*;
import tech.zerofiltre.blog.domain.user.model.*;
import tech.zerofiltre.blog.infra.security.model.*;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class GenerateTokenTest {

    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String TOKEN = "token";

    GenerateToken generateToken;
    @MockBean
    private VerificationTokenProvider verificationTokenProvider;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private UserProvider userProvider;

    @BeforeEach
    void setUp() {
        generateToken = new GenerateToken(verificationTokenProvider, jwtTokenProvider, userProvider);

    }

    @Test
    void execute_generates_ProperlyFromRefreshToken() throws InvalidTokenException {
        //ARRANGE / Given
        User user = new User();
        VerificationToken verificationToken = new VerificationToken(user, REFRESH_TOKEN);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));

        when(verificationTokenProvider.ofToken(REFRESH_TOKEN)).thenReturn(Optional.of(verificationToken));
        when(jwtTokenProvider.generate(user)).thenReturn(
                new JwtToken("accessToken", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
        );

        //ACT / when
        Token token = generateToken.byRefreshToken(REFRESH_TOKEN);

        //ASSERT /then
        assertThat(token).isNotNull();
        assertThat(token.getRefreshToken()).isNotNull();
        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getTokenType()).isNotNull();
        assertThat(token.getTokenType()).isNotEmpty();
        assertThat(token.getTokenType()).isEqualTo("Bearer");
        assertThat(token.getRefreshTokenExpiryDateInSeconds()).isNotZero();
        assertThat(token.getAccessTokenExpiryDateInSeconds()).isNotZero();

    }


    @Test
    void execute_generates_ProperlyFromUser() {
        //ARRANGE
        User user = new User();
        VerificationToken verificationToken = new VerificationToken(user, TOKEN);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        when(verificationTokenProvider.generate(eq(user), anyLong())).thenReturn(verificationToken);
        when(jwtTokenProvider.generate(user))
                .thenReturn(new JwtToken("accessToken", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));

        //ACT
        Token token = generateToken.byUser(user);

        assertThat(token).isNotNull();

        assertThat(token.getRefreshToken()).isNotNull();
        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getTokenType()).isNotNull();
        assertThat(token.getTokenType()).isNotEmpty();
        assertThat(token.getRefreshTokenExpiryDateInSeconds()).isNotZero();
        assertThat(token.getAccessTokenExpiryDateInSeconds()).isNotZero();
    }
}