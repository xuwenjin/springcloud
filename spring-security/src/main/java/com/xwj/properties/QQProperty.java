package com.xwj.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QQProperty extends SocialProperties {

	private String providerId = "qq";

}
