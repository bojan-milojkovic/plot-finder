package com.plot.finder.security.service;

import java.util.Date;
import java.util.Random;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;

@Service
public class JsonTokenEncoder {
	
	public static RSAKey rsaJWK;
	static{
		try {
			rsaJWK = new RSAKeyGenerator(2048)
				    .keyID("123")
				    .algorithm(JWSAlgorithm.RS256)
				    .generate();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
	}
	
	public String encodeJson(final String json){
		try {
			// init header :
			JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
						.contentType("text/plain")
						.keyID(rsaJWK.getKeyID())
						//.x5c(certChain)
						.build();
			
			// Prepare JWT with claims set :
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
			    .subject("APPL_ID")
			    .claim("jti", (new Random()).nextInt(10000) )
			    .expirationTime(new Date(new Date().getTime() + 60 * 1000))
			    .notBeforeTime(new Date())
			    .claim("fes:iv", "use iv from test cases")
			    .claim("fes:data", json)
			    .build();
			
			// create token object :
			JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
			
			// init signer :
			JWSSigner signer = new RSASSASigner(rsaJWK);
			
			//sign :
			jwsObject.sign(signer);
			
			return jwsObject.serialize();
			
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
