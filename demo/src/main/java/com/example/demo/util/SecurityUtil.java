package com.example.demo.util;


import com.example.demo.dto.response.ResLoginDTO;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class SecurityUtil
{
    private final JwtEncoder jwtEncoder;
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    @Value("${demo.jwt.base64-secret}")
    private String jwtKey;

    @Value("${demo.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${demo.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
//    create token
    public String createAccessToken(String email, ResLoginDTO resDTO){
        ResLoginDTO.UserInsideToken userToken = new ResLoginDTO.UserInsideToken();
        userToken.setId(resDTO.getUser().getId());
        userToken.setEmail(resDTO.getUser().getEmail());
        userToken.setName(resDTO.getUser().getName());
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

//        hardmode permission
        List<String> listAuthority = new ArrayList<String>();
        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");
//        @formater:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("permission",listAuthority)
                .claim("user",userToken)
                .build();

        JwsHeader jwsHeader= JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claims)).getTokenValue();
    }
//    Refresh token
    public String  createRefreshToken(String email, ResLoginDTO restLoginDTO){
        ResLoginDTO.UserInsideToken userToken = new ResLoginDTO.UserInsideToken();
        userToken.setId(restLoginDTO.getUser().getId());
        userToken.setEmail(restLoginDTO.getUser().getEmail());
        userToken.setName(restLoginDTO.getUser().getName());
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

//        @formater:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now) //thời gian phát hành token
                .expiresAt(validity) // thời gian heets hạn
                .subject(email)// định danh người dùng
                .claim("user",userToken) //lấy thông tin người dùng
                .build();

        JwsHeader jwsHeader= JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claims)).getTokenValue();
    }

    public Jwt checkRefreshToken(String token){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecrectKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        try{
           return jwtDecoder.decode(token);

        }catch( Exception e ){
            System.out.println("RefreshToken error"+e.getMessage());
            throw e;
        }
    }

    private SecretKey getSecrectKey(){
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
    public static Optional<String> getCurrentUserLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication){
        if(authentication == null){
            return null;
        }else if(authentication.getPrincipal() instanceof UserDetails springSecurityUser){
            return springSecurityUser.getUsername();
        }else if(authentication.getPrincipal() instanceof Jwt jwt){
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
//     public static boolean isAuthenticated() {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
//     }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
//     public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         return (
//             authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
//         );
//     }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
//     public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
//         return !hasCurrentUserAnyOfAuthorities(authorities);
//     }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
//     public static boolean hasCurrentUserThisAuthority(String authority) {
//         return hasCurrentUserAnyOfAuthorities(authority);
//     }
//
//     private static Stream<String> getAuthorities(Authentication authentication) {
//         return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
//     }

}
