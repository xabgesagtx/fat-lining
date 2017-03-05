package com.github.xabgesagtx.fatlining.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 
 * Service to get messages for the current locale
 *
 */
@Component
public class MessagesService {
	
	@Autowired
	private MessageSource messages;
	
	/**
	 * Get message for current locale
	 * @param key of the message to get
	 * @param params of the message
	 * @return the full message with params included
	 */
	public String getMessage(String key, Object... params) throws NoSuchMessageException {
		return messages.getMessage(key, params, LocaleContextHolder.getLocale());
	}

}
