package tech.zerofiltre.blog.domain.user.use_cases;

import tech.zerofiltre.blog.domain.user.*;
import tech.zerofiltre.blog.domain.user.model.*;
import tech.zerofiltre.blog.infra.security.model.*;

import java.time.*;

public class GenerateToken {

    public static final long DURATION_IN_SECONDS = 86400 * 2L;
    private final VerifyToken verifyToken;
    private final JwtTokenProvider jwtTokenProvider;
    private final VerificationTokenProvider verificationTokenProvider;
    private final UserProvider userProvider;


    public GenerateToken(VerificationTokenProvider verificationTokenProvider, JwtTokenProvider jwtTokenProvider, UserProvider userProvider) {
        this.verifyToken = new VerifyToken(verificationTokenProvider);
        this.verificationTokenProvider = verificationTokenProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProvider = userProvider;
    }

    public Token byRefreshToken(String refreshToken) {
        return null;
    }
















    public Token byUser(User user) {

        return null;
    }

    public Token byEmail(String email) throws UserNotFoundException {
        User user = userProvider.userOfEmail(email)
                .orElseThrow(() -> new UserNotFoundException("The connected user was not found", email));
        return byUser(user);
    }

}
