// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/** Contains information about a token. */
public final class TokenInfo {

	private final long created = System.currentTimeMillis();
	private final String token;
	private final UserDetails userDetails;
	// TODO expiration etc

	public TokenInfo(String token, UserDetails userDetails) {
		this.token = token;
		this.userDetails = userDetails;
	}

	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return "TokenInfo{" +
			"token='" + token + '\'' +
			", userDetails" + userDetails +
			", created=" + new Date(created) +
			'}';
	}
}
