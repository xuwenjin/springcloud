package com.xwj.common;

import lombok.Getter;
import lombok.Setter;

/**
 * RSA秘钥
 */
@Getter
@Setter
public class RsaKey {

	private String requestPublicKey;

	private String requestPrivateKey;

	private String responsePublicKey;

	private String responsePrivateKey;

}
