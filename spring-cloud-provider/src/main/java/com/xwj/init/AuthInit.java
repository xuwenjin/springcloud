package com.xwj.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.xwj.auth.AuthUtil;
import com.xwj.common.AuthConsts;
import com.xwj.common.RsaKey;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化鉴权参数
 */
@Slf4j
@Component
public class AuthInit implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("初始化鉴权参数");
		AuthUtil.blackLimit.add("55.55.10.1");
		AuthUtil.blackLimit.add("55.55.10.2");
		initDefaultRSAKey();
		initWxRSAKey();
	}

	/**
	 * 默认appId和Key做映射
	 */
	private void initDefaultRSAKey() {
		RsaKey appkey = new RsaKey();
		String requestPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+A+RXQMh7nEpKaOclUyrlQr4l4sT99QnyvM2/hRotFVLyvMXsgrXweWm9N5RU4NVnergdSn143uvYp5Ec6sHTu+s/MwHGG/d7EJR8up8GJHMtHkW44sGiYerl26lZxFo3AFrmrOSLELds5MBM+5CgCHKqyiYxxmvIiEuoemB4CQIDAQAB";
		String requestPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL4D5FdAyHucSkpo5yVTKuVCviXixP31CfK8zb+FGi0VUvK8xeyCtfB5ab03lFTg1Wd6uB1KfXje69inkRzqwdO76z8zAcYb93sQlHy6nwYkcy0eRbjiwaJh6uXbqVnEWjcAWuas5IsQt2zkwEz7kKAIcqrKJjHGa8iIS6h6YHgJAgMBAAECgYB5cUdZNMBtsGAS2qUQx+zchiG9WQgKP7hR+bWOOGWYds6C8X+WP5xEy3R3SLR24xPBhT9zCQ7UV7VozQAD+U5TBuDgsSWOtg3tnBHuTtwBK890ZbEv6z2EUP2QoK7iP+v5lJI1nTJpZtZ65OVKQv9GHeQ3TJp1ae27KT2svey/WQJBAPnoY2PTAODjbdM+l8pLml0f2Vd83nzVXK8OmQbkOdUQv7B+yHxt6zjGq9UMeOudnYObPFdS/81oRN/YGaZpSm8CQQDCpbqu3KDbBVxmZchhJfYHfvCiujMHfIa81fVYTK3rdahZlDR9J4ifh2yvbYXnCh4yyDfKpCVw67hr7U49mwEHAkAGe4S4fiyzqLKcnC8LzFJAwCa/IjoTOuWglNxbVWg6oqiWR3Oj5qYHXv/uEtjAI+KGG2zBRyHjjiTbOZvQuUJ5AkEAqSfRtsjx2aUtCagGnbaZuyXsBd7/HdBwX4cpMlVhB7E2XrLXcrR6nPjZ0RLDPWejmso5AhfomdugZ9rRFeSw3QJADB2KQH5Nt7bhLedJvNthEqtqVE6zuXBraiOGWwYsFSv5h109AgeuVZQZiY7Sl2WUnYHIH//003dllZRwwdzl5A==";
		appkey.setRequestPublicKey(requestPublicKey);
		appkey.setRequestPrivateKey(requestPrivateKey);

		String responsePublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt3g2eaNhWfirOH7BNGZ3BPkk/qWMbVQB3OopbxQuoc7wc7HKRoVvqFTeXlm/l9xb/LzL2iI0dIroyzuTJnhYiHXXJSFHC0IKEhDTqeQ7IfoPk64C0154Bze7O/t8McmtqhVbXIMLPCcgf1uB3FT2+7QChg+Ko6l4ZYd+/5btzmQIDAQAB";
		String responsePrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAK3eDZ5o2FZ+Ks4fsE0ZncE+ST+pYxtVAHc6ilvFC6hzvBzscpGhW+oVN5eWb+X3Fv8vMvaIjR0iujLO5MmeFiIddclIUcLQgoSENOp5Dsh+g+TrgLTXngHN7s7+3wxya2qFVtcgws8JyB/W4HcVPb7tAKGD4qjqXhlh37/lu3OZAgMBAAECfzcDC0wT4F99jA2r+Rxr/n4mNwbtkW4UdxFCdISU1Bt4gwyjw5xQccSe8fgEoWMhyyTyyi03B28wjbmFvN4OJvIdoaGVifDvmoEUahpXG0ckrE5oex/1RiXIDNKB+Fr0Kr+fQKr00u7SW+5+9QklwbWNvRCTUStcT1QEQiPlgdECQQDxt6uk2dfMnsDerae1DX2kCoBWx16VMzeHrVd549N8ma9in5l+rM1dqdsx8zeqx1P8m7T13g34J8XORSpUKbi9AkEAuCQNJ6v8ekRZMNjbPpoz8MCp2u1hg4bmiylvb4OOiLY6ONXt9d13uv9xWW0lzRgOTctEDmQ5/VbqP5PEfI96DQJBANLtiQ3IvySi4AExHmjCxgGw3D9dqK6fy/RMkkoeQf24CrEQPpyo5Gi4gTt8VvZjDGoh4e6vgBctddJCzuY0pi0CQHnTqDxqBhViaNvvbUZCwUB0RyxHxy88rgS9+jL+B+wdG/IEX3Y9+vvmCrkOhGbnlncTl0gqOU+KFFrRybpbNnUCQQCP2qX2TZie9vGdtKTrAPp2fQwD7e8aQNS2i7kRCleZKG8TQLr6h9gxlp2r3vZKyTgILPlJbeEelIooS1uDnaII";
		appkey.setResponsePublicKey(responsePublicKey);
		appkey.setResponsePrivateKey(responsePrivateKey);

		AuthUtil.rsaKeyMap.put(AuthConsts.DEFAULT_APPID, appkey);
	}

	/**
	 * 将AppId和Key做映射
	 */
	private void initWxRSAKey() {
		RsaKey appkey = new RsaKey();
		String requestPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCIsx4Vy/27i6tTiJoeuhazDVNb+B8ikvdCgQKolRtVU+KvTGmof/v66VEJ6/rdm2IzT2UdnsTRZmaY5mDLEsrqyj9svoSb923Yfky+APc2nX8Mz8RY6oWCs+NcdZDN7oFNV4/MYnD7flLK+xQ7GQaO0t81Oq4Kv/1iR8uNmyc0wIDAQAB";
		String requestPrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIIizHhXL/buLq1OImh66FrMNU1v4HyKS90KBAqiVG1VT4q9Maah/+/rpUQnr+t2bYjNPZR2exNFmZpjmYMsSyurKP2y+hJv3bdh+TL4A9zadfwzPxFjqhYKz41x1kM3ugU1Xj8xicPt+Usr7FDsZBo7S3zU6rgq//WJHy42bJzTAgMBAAECgYAF61lLnAOlgr4CygwMnpKVFrbR9+XJJG1A96SpayrV8kx07hglETdYDWruB7QbzL48u8EyUJVWkhLjj+Y6rjgZCsrgc26sJ34v3q2THIUudhwFmYnmzI7z2lq+YRGl/vODWoGMSxgXdsEEZagIupNEy7pimqlqP4szg9S2cjzsoQJBALszhHrsXp21QXblUsB4xnYNqNj5PNE53hLMNIG1OS0b3VJ79MlMwp4XZoNDmjjzaSBDofto3jSwR2qQT6n91aMCQQCx9mWywvBZpJuhRVv1OI+gOPJDYunqe9zTosInWIKlwRTn349PuvBoUOtioc93lYBT3akfaN5uajYPbJScza8RAkAWJhBEovbG3g0yzgsubpu8l/0kPsjtUFVwD+0ec5yiM6vJ+JSLxKM6JTahzTvQBSyo/peJsWyo+zpX518lk7zVAkApMjO6nPvMlJleTNMLwislWOlkBgrGOCQXbc7qEsgznK29O3hOaaTrUQgcTf7b3OTXBTH4TmtnPfkDuwvv/IExAkAkdLo8y20llEQ4iWQOyKv9151rO2HmRRH6dDeTstFci83/RU8SRFLXkNG8KCLvTBGczLF8dzapi9XoaJiG7O2q";
		appkey.setRequestPublicKey(requestPublicKey);
		appkey.setRequestPrivateKey(requestPrivateKey);

		String responsePublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBrZoTIgeC/FzAsqE80ZfUe/fPqLq7gfUKMMFo7BFJi871xsTDR5Zm2p2TOYv59cyKi+JaJZTl1+F6kzHsEWJq+h0tWNyF9vp8xB1LvCZNELvE2qyzL+fuQt2Pl66Ok/MWO8hhuHLahJAWQ9zXV4xhj626B7yqACA7uliLb8CRNQIDAQAB";
		String responsePrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMGtmhMiB4L8XMCyoTzRl9R798+ouruB9QowwWjsEUmLzvXGxMNHlmbanZM5i/n1zIqL4lollOXX4XqTMewRYmr6HS1Y3IX2+nzEHUu8Jk0Qu8TarLMv5+5C3Y+Xro6T8xY7yGG4ctqEkBZD3NdXjGGPrboHvKoAIDu6WItvwJE1AgMBAAECgYA3xX3wtIPlPDaZZtb3ZIOMNIaPzc634Bxn6tJHHHN98jyOmcZfVWYCCEIH+zJLsHROESoFO309EoVjfq+JgxgLpdPJ6w3utLhYOeL3JfLGHo3j7hzNTW2QDGb1Qc10cpif+PRgUsOrv2tCqPNZFBitagT4uQtDOTgf5CqixY1WAQJBAOeA9Jt6dBlkSRcEBlES1Ya1EE98lnzIRgnP3QIS6qbwslP7NxS/pQwtwtiTEb0gZhXjNqTX+HVo3s3wRlNzA6UCQQDWLAPMJNcetkHA6ijBpj12cCdN3Sf0oZcokmVOtQ7RYFWPMI3WwlA7oENNCbeckYKMGUMPftfluDdRMEmqdqJRAkBEM0Zcg3+ud0/c+u+NdNn43GCYuiBvVGTlwRnf4YjFc4Vlnk2EzEyoQNb1DKaeK+xHKG/Rslpc5G83BkEBlpGNAkAUfyHd56iux8KDM+WyorY/H3yjdwbb4psUxu3rGmjQoOePTJZGd1I2YqAOP1/THBniToiccwc2dOrWWkiyh6gBAkBicSPEpxd5ZzZIBQ6UcCmfjDL53m5Se2Kr8fCHoGzAtkbm6/smbQv8gn5sftwCvhBehqBmc5o3XepNQ3WVwIZR";
		appkey.setResponsePublicKey(responsePublicKey);
		appkey.setResponsePrivateKey(responsePrivateKey);

		AuthUtil.rsaKeyMap.put(AuthConsts.WX_APPID, appkey);
	}

}
