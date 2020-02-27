package com.xwj.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.xwj.common.RsaKey;
import com.xwj.interceptor.AuthUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化App的参数
 */
@Slf4j
@Component
public class AuthInit implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("初始化APP参数");
		AuthUtil.blackLimit.add("55.55.10.1");
		AuthUtil.blackLimit.add("55.55.10.2");
		initDefaultRSAKey();
		initRSAKey();
	}
	
	/**
	 * 默认appId和Key做映射
	 */
	private void initDefaultRSAKey() {
		RsaKey appkey = new RsaKey();
		String requestPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6PVGn6JsplXEJHtiLF5vM24sA\r\n"
				+ "+oYsz8umkE28Koy9yrj0jteOWjZ3zTIJdwwtBL2YgCFWmUqBjPL33X5t83/d4XAc\r\n"
				+ "dbrONccdLL+dUjmFZBWmHQA1AwHxbeDNMOku2rKwFY9t4Fm/5SOOrT8PAB/JycEp\r\n" + "pvuG1xUpG2JLROo0TwIDAQAB";
		String requestPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALo9UafomymVcQke\r\n"
				+ "2IsXm8zbiwD6hizPy6aQTbwqjL3KuPSO145aNnfNMgl3DC0EvZiAIVaZSoGM8vfd\r\n"
				+ "fm3zf93hcBx1us41xx0sv51SOYVkFaYdADUDAfFt4M0w6S7asrAVj23gWb/lI46t\r\n"
				+ "Pw8AH8nJwSmm+4bXFSkbYktE6jRPAgMBAAECgYB5bYWR/r08Da3Xjn8NoWT52qL1\r\n"
				+ "DmLxUMKtZSTp7Kw7CrQw7/lnXTZqQiW/uhj8OT0M823JMsU7VpUMOyQ1uC/Dc5Uw\r\n"
				+ "sZkSBVDg76e78MlyR4fqY+KZU2IqLKnQhclogoo4dq6MxZbUQMuBxjVwuWaTJOgj\r\n"
				+ "sFwFWfN5djMSPvKuQQJBAOyTALNX86w3hrH9LTTzaswuglvH1TWe5WWnNYLvJudV\r\n"
				+ "Btr4hF1eDaMfyxfk9GzBeQRVNs2ZLGYm2LdOov9GQP8CQQDJiDycHcpb6k27eLCZ\r\n"
				+ "eSE2s7Zk8HMhNJ5anV+OquYTxfvy258W26s27r5opqohKHz29d9QrkxcuVUKvwY2\r\n"
				+ "ZLyxAkEAxgHgMzupKvqqlW0Hmmsmd1FyMGmYrbGZ5TRHmJicYkELZNleyFsBZQgm\r\n"
				+ "T5CFeEWTCapdHUZYIAPhncOGr8zYdwJAQqyR8MxiUHp58RYCxyOt+10FcOukC05P\r\n"
				+ "PYdnP9oGeHA95KEIRxWx1WCzGghZrNKqVUIO+bBQjzS5j+6W7ZPyYQJBAJ+dfmek\r\n"
				+ "IINlufRXf2IPmuUT45vD5a8x8BH41Ph06kFcasnR+YVPdFtULPDYhM7fKawxd+Kp\r\n" + "msvJUTQXTtjFR9M=";
		appkey.setRequestPublicKey(requestPublicKey);
		appkey.setRequestPrivateKey(requestPrivateKey);
		String responsePublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4QEQsZ5UhHwerno7eURzdcUhA\r\n"
				+ "LHAgyn7dMSDFCCLQbRyOSgHnYGK8XJ/4A0jF9PypBZ+4lYuF5MTgOO4dOhebTPu8\r\n"
				+ "5JB3lokARat8xqV376KCgeeWKIPoAJjhX2mjvJNGUYhtozpEAFeP3pf40AFerixm\r\n" + "+vTO8sHeJQarCa9xfwIDAQAB";
		String responsePrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALhARCxnlSEfB6ue\r\n"
				+ "jt5RHN1xSEAscCDKft0xIMUIItBtHI5KAedgYrxcn/gDSMX0/KkFn7iVi4XkxOA4\r\n"
				+ "7h06F5tM+7zkkHeWiQBFq3zGpXfvooKB55Yog+gAmOFfaaO8k0ZRiG2jOkQAV4/e\r\n"
				+ "l/jQAV6uLGb69M7ywd4lBqsJr3F/AgMBAAECgYB9ZafAPOL+/rper/P2FGg7wcx1\r\n"
				+ "eIj7mDVNXxE5Z7ch+RCVOoMfMLciETwCNGID8B9A/YXdv1GM18yx+az9ETtcnAoX\r\n"
				+ "rGB2rX85UQqxSwBj6L3cPte49YP0PM9YZF6GPlcEDTZ8Ae4KvXlWlX1kUrbTUQ+o\r\n"
				+ "rFXG+2aih7opEAzBsQJBAN6pw/U8rA+Xm9GOnpkCvqEK+y+aOHRtbzeitvFOXvBK\r\n"
				+ "40SdD3sQ6XuTp1+xRv6NZxpJNgcIb2mVKlroX0DjmxkCQQDT1j3LhIR9P9vG2ROR\r\n"
				+ "rt/8Iiww53NPuKHLlkHTd40Qqu6rN9h/ii5m09RMn4Crhn592QuWq7LK17waoOCN\r\n"
				+ "NxxXAj8o4QIZBAqS0kLJNmXnsZlN97YmBypWNcE1daogo0LK2vTeo/czoOc8yN7x\r\n"
				+ "sppWIZ/MM6S3pdTOjZQ5HHBgeWkCQQDQxPeptzQLa//g/Na4YKwGBHegyrlt+/wY\r\n"
				+ "dEDzc4Lmxk7pFuSa7UfFt0YnLZrVcHsA9ALjvts55VtQsvQauBGJAkBRgmqNfjJu\r\n"
				+ "JH1LCaw2lkDVafFtHo/IteXj92DNj+VkzdvUsYKTT1T2SdHMM2cln8L7BDxJ8LxY\r\n" + "IZ5U62L4VNQ6";
		appkey.setResponsePublicKey(responsePublicKey);
		appkey.setResponsePrivateKey(responsePrivateKey);
		AuthUtil.rsaKeyMap.put("eyc_app_20190501", appkey);
	}

	/**
	 * 将AppId和Key做映射
	 */
	private void initRSAKey() {
		RsaKey appkey = new RsaKey();
		String requestPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDkW0N2lGrfWUiN6OSxM5DpMP7D\r\n"
				+ "UHewxsHuDlrOjDUE6P2pO4SdCcanNBBxAN0iXuVhlzm27AOVvCKg+JIpckT6t9tc\r\n"
				+ "k2FI1sVzP/80tx4J1ONtUROHrrc4GT3QXMIjP/0Rw0qnma59lnMtwQnHd7JqKr/k\r\n" + "WGd3vhpnJiUeAwO5qwIDAQAB";
		String requestPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAORbQ3aUat9ZSI3o\r\n"
				+ "5LEzkOkw/sNQd7DGwe4OWs6MNQTo/ak7hJ0Jxqc0EHEA3SJe5WGXObbsA5W8IqD4\r\n"
				+ "kilyRPq321yTYUjWxXM//zS3HgnU421RE4eutzgZPdBcwiM//RHDSqeZrn2Wcy3B\r\n"
				+ "Ccd3smoqv+RYZ3e+GmcmJR4DA7mrAgMBAAECgYBnMns1bnMxxlaMkzAuiIA3cgXf\r\n"
				+ "3FCK2fewDlqpNyGKu4RO42/VzCIzU8sOVnaY3svIo5/Yc6ZXF7OKjWr4QgS7cnOh\r\n"
				+ "s8bjftXmiSDjjXYKV8zxiMcrR4v+uwqBBqsdWBT6dH3Ju2akZ9dX35JEFbrB9G1k\r\n"
				+ "9ynCDDX32jmdKuCboQJBAPzzVjGMkqrK8ZOULofTeMEsFhorECRHeejKUXVZn7LP\r\n"
				+ "r7XNxMbOxB4TNXw2sBQL0DO3IV546tsk1UlFCeFyKPsCQQDnHAYlLoRi9STtMK6a\r\n"
				+ "mPU9zL+JmtH7amy2rUlsiX1Kr/D0ZKN4970Ci91p5WkJnGMnXd9iHdaOlFhWYCPb\r\n"
				+ "vzMRAkEAx49S2RSGQaiKnluxugbkpcIaTdrbiUO6siWrzmE+1Nt1TIFiVVaa8v9E\r\n"
				+ "mxKqJ6aKQ8Ke2OGcp/ePLZcCDJL8MQJBANSWvbxkHtlcgTxCRMrnGuhfOG2e4btw\r\n"
				+ "uVIM8vaJPpx+XZqiHJHA6be26tRneAikN71Vp498vQnowZ2q0DFqntECQHDgXC+8\r\n"
				+ "WouhZDq4APO4Zzb8Hoi+o6q8Jpt0D23btr/IwKuwJH9WQJWvhw7tYNlxnrW39OOp\r\n" + "aycRvDszMCKFEfo=";
		appkey.setRequestPublicKey(requestPublicKey);
		appkey.setRequestPrivateKey(requestPrivateKey);
		String responsePublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCowDs6P6bMEjT45ZRNC3ItoGNw\r\n"
				+ "Gc5gmO0M+VbxvDKaOg2CqP4wczA0ugfZ0mY+kwWlUu6gDB67O42cIDUL8wqyncUH\r\n"
				+ "tJNIgo3VRSDqN6kOD8HcdYU6OiK9hgSVpbqdzR69oZx/RMjDlK92XRlSyIB4hI71\r\n" + "vzgv5gkrkf1UkIAr/wIDAQAB";
		String responsePrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKjAOzo/pswSNPjl\r\n"
				+ "lE0Lci2gY3AZzmCY7Qz5VvG8Mpo6DYKo/jBzMDS6B9nSZj6TBaVS7qAMHrs7jZwg\r\n"
				+ "NQvzCrKdxQe0k0iCjdVFIOo3qQ4Pwdx1hTo6Ir2GBJWlup3NHr2hnH9EyMOUr3Zd\r\n"
				+ "GVLIgHiEjvW/OC/mCSuR/VSQgCv/AgMBAAECgYAa8ugCe1vFxzqSbQdr7mIDi1OV\r\n"
				+ "+WkN4B4rLj6GSEnEe3Tg4uAFGMAgC31pecI+R/hk2IOnJ4hY2j/ZKQOPrvaMIWbr\r\n"
				+ "iudaoGbbztqIWgNzbyO4tV5p/eu4zVEMU4CVtOAYSd1zMhmh4KjYEhPyJ7Y189ZG\r\n"
				+ "96MiyPYfRd+whkeD8QJBANEYastvzru5KRJLeb4qqBHi4aO6TQuQxmtoemIvAVaV\r\n"
				+ "aK5I5iPR2WV+Wzl1NdKKa0aBpgZNI5hDBrZiIDbquVsCQQDOmvp/45jlXp9v+c58\r\n"
				+ "bw0KRLzcFmCTpYQEmWyz5J3Yk97CjzTBWp1r1doNetGVPJpgVuq0OCLai2KL5DuX\r\n"
				+ "RXUtAkEArK3YjHeiqH9qvtQcD4OU12iiZZ+WvyVp6AwZffpJxaKQF9bWUOCeA+aN\r\n"
				+ "Ge+FaoGrxEePwAZ2jelUosx4xEGmnwJBALUDbZPdCZl0ZMZLnJDDDy6++KTvaiAR\r\n"
				+ "9O++qFGYbH9TVukpZPQ++wt+qvQCdluFZFAh9rJ9OHQ3iNi910P60+UCQA0Fz3TK\r\n"
				+ "SVkDJH7iZvs5n+5B2R400eGK/AXghwP2cpdXqppRHPuXuc/8J+d3YwEF2GQ9dd0a\r\n" + "JaV4pNnu4jVRczc=";
		appkey.setResponsePublicKey(responsePublicKey);
		appkey.setResponsePrivateKey(responsePrivateKey);
		AuthUtil.rsaKeyMap.put("eyc_app_20190918", appkey);
	}

}
