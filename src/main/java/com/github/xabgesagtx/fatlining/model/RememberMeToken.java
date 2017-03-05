package com.github.xabgesagtx.fatlining.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * Remember me token to login without entering credentials again
 *
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName="of")
public class RememberMeToken {

    @Id
    private String series;
    private String username;
    private String tokenValue;
    private Date date;

    public PersistentRememberMeToken toPersistentRememberMeToken() {
    	return new PersistentRememberMeToken(username, series, tokenValue, date);
    }
	
}
