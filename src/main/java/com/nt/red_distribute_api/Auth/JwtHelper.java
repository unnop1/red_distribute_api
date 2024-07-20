package com.nt.red_distribute_api.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nt.red_distribute_api.dto.req.auth.JwtRequest;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.entity.UserEntity;
import com.nt.red_distribute_api.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // token expire date

    @Autowired
    private UserService userService;

    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf"; // secret code

    // Retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    } // user to get name

    // Retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    } // get Expiration Date from token

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {     // i d k
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // For retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) { 
        System.out.println("token:"+token);                                   // i d k
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public VerifyAuthResp verifyToken(String bearerToken){
        VerifyAuthResp vrf = new VerifyAuthResp();
        try{
            if (bearerToken.startsWith("Bearer")){
                String token = bearerToken.substring(7);
                String emailClaim = getClaimFromToken(token, claims -> (String) claims.get("email"));
                String usernameClaim = getClaimFromToken(token, claims -> (String) claims.get("username"));
                String deviceClaim = getClaimFromToken(token, claims -> (String) claims.get("device"));
                String browserClaim = getClaimFromToken(token, claims -> (String) claims.get("browser"));
                String systemClaim = getClaimFromToken(token, claims -> (String) claims.get("system"));
                System.out.println("emailClaim:"+emailClaim);
                System.out.println("usernameClaim:"+usernameClaim);
                UserEntity userDetails = userService.loadUserByUsername(usernameClaim);
                if (userDetails != null ){
                    vrf.setEmail(emailClaim);
                    vrf.setUsername(usernameClaim);
                    vrf.setUserInfo(userDetails);
                    vrf.setDevice(deviceClaim);
                    vrf.setBrowser(browserClaim);
                    vrf.setSystem(systemClaim);
                }
            }else{
                vrf.setError(bearerToken);
            }
            return vrf;
        }catch(Exception e){
            return null;
        }
        
    }   

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {                                      // checking expire
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Generate token for user
    public String generateToken(JwtRequest req, String email) {
        Map<String, Object> claims = new HashMap<>();                           // geenrate token
        claims.put("email",  email);
        claims.put("username",  req.getUsername());
        claims.put("device", req.getDevice());
        claims.put("browser", req.getBrowser());
        claims.put("system", req.getSystem());
        return doGenerateToken(claims, req.getUsername());
    }

    // While creating the token -
    // 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key
    // 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //    compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                // .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // Validate token
    public Boolean validateToken(String token, UserEntity userDetails) {
        final String username = getUsernameFromToken(token);
        return token.equals(userDetails.getCurrentToken()) && (username.equals(userDetails.getUsername()));
    }
}
