package net.rokyinfo.basecommon.util;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * Description:<br>
 * 读取i18n的配置信息
 * 
 * @author  
 * @version: 1.0
 */
public class MessageReader
{
	//private static final Log logger = LogFactory.getLog(MessageReader.class);

	/**
	 * 根据key值读取消息
	 * 
	 * @param key
	 * @return i18n的文本
	 */
	public static String getMessage(String key)
	{
		String value = getMessage(key, (Object[]) null);
		return value;
	}

	/**
	 * 根据key值读取消息
	 * 
	 * @param key 消息的key值
	 * @param params 消息的参数
	 * @return
	 */
	public static String getMessage(String key, Object[] params)
	{
		Locale locale = MessageReaderWarpper.getInstance().getLocale();
		return getMessage(key, params, locale);
	}
	

	/**
	 * 根据key值读取消息
	 * 
	 * @param key 消息的key值
	 * @param params 消息的参数
	 * @return
	 */
	public static String getMessage(String key, String[] params)
	{
		if (params == null)
			return getMessage(key);
		Object[] objParams = new Object[params.length];
		for (int i = 0; i < params.length; i++)
			objParams[i] = params[i];

		return getMessage(key, objParams);
	}

	/**
	 * 根据key值读取消息
	 * 
	 * @param key 消息的key值
	 * @param params 消息的参数
	 * @param locale 区域属性
	 * @return
	 */
	public static String getMessage(String key, Object[] params, Locale locale)
	{
		MessageSource messageSource = MessageReaderWarpper.getInstance().getMessageSource();
		String s = null;
		try
		{
			//assert messageSource != null;
			s = messageSource.getMessage(key, params, locale);
		}
		catch (Exception e)
		{
			return "[" + key + "]";
		}
		return s;
	}
}
