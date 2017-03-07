package net.rokyinfo.basecommon.util;

import org.apache.commons.lang.StringEscapeUtils;
import sun.security.action.GetPropertyAction;

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessController;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:<br>
 * 处理String的工具类
 * 
 * @author
 * @version: 1.0
 */
public class StringUtils
{
	/**
	 * 比较两个字符串的大小
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static final int compare(String value1, String value2)
	{
		byte[] buf1 = value1.getBytes();
		byte[] buf2 = value2.getBytes();
		int len = Math.min(buf1.length, buf2.length);

		for (int i = 0; i < len; i++)
		{
			if (buf1[i] != buf2[i])
				return buf1[i] - buf2[i];
		}

		return buf1.length - buf2.length;
	}

	/**
	 * 把字符串中的全角字符转换成半角字符
	 * 
	 * @param src
	 * @return
	 */
	public static final String sbcToDbc(String src)
	{
		String outStr = "";
		String tempStr = "";
		byte[] b = null;

		for (int i = 0; i < src.length(); i++)
		{
			try
			{
				tempStr = src.substring(i, i + 1);
				b = tempStr.getBytes("unicode");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}

			if (b[3] == -1)
			{
				b[2] = (byte) (b[2] + 32);
				b[3] = 0;

				try
				{
					outStr = outStr + new String(b, "unicode");
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			}
			else
				outStr = outStr + tempStr;
		}

		return outStr;
	}

	public static String rep(Object s)
	{
		if (s == null)
			return "";
		if (s instanceof String)
		{
			String new_name = (String) s;
			return new_name.trim();
		}
		return s + "";
	}

	public static boolean isEmpty(Object s)
	{
		if (s == null)
			return true;
		if (s instanceof String)
		{
			String str = (String) s;
			if ("".equals(str))
				return true;
		}
		return false;
	}

	/**
	 * 把文本编码为Html代码
	 * 
	 * @param target
	 * @return 编码后的字符串
	 */
	public static String escapeHtmlJson(String str)
	{
		return StringUtils.escapeHtml(str).replaceAll("\\\\", "\\\\\\\\");
	}
	
	public static String escapeHtml(String str)
	{
		if (str == null)
			return "";
		String s = StringEscapeUtils.escapeHtml(str);
		s = s.replaceAll("\r", "");
		s = s.replaceAll("\n", "<br/>");
		s = s.replaceAll("	", "&nbsp;&nbsp;&nbsp;&nbsp;");

		return s;

	}

	public static String formatString(String str)
	{
		if (isEmpty(str))
			return str;
		String formatStr = str.replaceAll("<", "&lt;");
		formatStr = formatStr.replaceAll(">", "&gt;");
		formatStr = formatStr.replaceAll("\\n", "<br>");
		formatStr = formatStr.replaceAll("\\s", "&nbsp;");
		return formatStr;
	}

	/**
	 * 截取文件名
	 * 
	 * @param fileName
	 *            文件名
	 * @param length
	 *            长度
	 * @return
	 */
	public static String subFileName(String fileName, int length)
	{
		if (isEmpty(fileName))
			return fileName;
		// 省略号长度
		int ellipsisSize = 2;
		int elBackSize = 2;
		// 文件扩展名长度（包括.）
		int expandSize;
		// 文件名去除扩展名后的长度
		int surplusSiz;

		// 省略号内容
		String ellipsisStr = " ... ";
		// 文件扩展名（包括.）
		String expandStr;
		if (fileName.length() <= length)
			return fileName;
		expandSize = fileName.lastIndexOf(".");
		surplusSiz = fileName.length() - expandSize;
		if ((length - ellipsisSize - surplusSiz - elBackSize) > 0 && surplusSiz > (length - ellipsisSize - expandSize - elBackSize))
		{
			expandStr = fileName.substring(expandSize, fileName.length());
			String str = "";
			String backStr = "";
			backStr = fileName.substring(expandSize - 2, expandSize);
			str = fileName.substring(0, (length - surplusSiz - ellipsisSize));
			return str + ellipsisStr + backStr + expandStr;
		}
		return ellipsisStr;
	}

	/**
	 * 根据传递的字符串和长度，将内容分割为2部分，并添加展开和伸缩的a标签
	 * 
	 * @param content
	 *            内容
	 * @param length
	 *            长度
	 * @return 处理后的Html内容
	 */
	public static String getSuffixHtml(String content, int length, String[] symbol)
	{
		if (symbol.length != 5)
			return content;
		if (content.length() < length + 1)
			return content;
		String lt = symbol[0];
		String gt = symbol[1];
		String a_expand = symbol[2];
		String a_shrink = symbol[3];
		String span_shrink = symbol[4];
		String str = content.substring(0, length);
		StringBuffer sb = new StringBuffer();
		sb.append(lt + "span" + gt);
		sb.append(str);
		if (length < content.length())
		{
			sb.append(a_expand);
			sb.append(MessageReader.getMessage("comment.expand") + lt + "/a" + gt);
		}
		sb.append(lt + "/span" + gt);

		if (length == content.length() - 1)
			return sb.toString();

		sb.append(span_shrink);
		sb.append(content.substring(length, content.length()));
		sb.append(a_shrink);
		sb.append(MessageReader.getMessage("comment.shrink") + lt + "/a" + gt);
		sb.append(lt + "/span" + gt);
		return sb.toString();
	}

	/**
	 * 统计字符串中包含的某个字符的个数
	 * 
	 * @param str
	 *            要查找的字符串
	 * @param includeStr
	 *            包含的字符串
	 * @return 个数
	 */
	public static int getStringCount(String str, String includeStr)
	{
		if (str == null)
			return 0;
		Pattern p = Pattern.compile(includeStr, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		int count = 0;
		while (m.find())
		{
			count++;
		}
		return count;
	}

	public static String subString(String str, int start, int end)
	{
		if (str == null)
			return "";
		return org.apache.commons.lang.StringUtils.substring(str, start, end);
	}

	static BitSet dontNeedEncoding;
	static final int caseDiff = ('a' - 'A');
	static String dfltEncName = null;

	static
	{
		dontNeedEncoding = new BitSet(256);
		int i;
		for (i = 'a'; i <= 'z'; i++)
		{
			dontNeedEncoding.set(i);
		}
		for (i = 'A'; i <= 'Z'; i++)
		{
			dontNeedEncoding.set(i);
		}
		for (i = '0'; i <= '9'; i++)
		{
			dontNeedEncoding.set(i);
		}
		dontNeedEncoding.set(' '); /*
									 * encoding a space to a + is done in the
									 * encode() method
									 */
		dontNeedEncoding.set('-');
		dontNeedEncoding.set('_');
		dontNeedEncoding.set('.');
		dontNeedEncoding.set('*');

		dfltEncName = (String) AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
	}

	public static String encode(String s, String enc) throws UnsupportedEncodingException
	{

		boolean needToChange = false;
		StringBuffer out = new StringBuffer(s.length());
		Charset charset;
		CharArrayWriter charArrayWriter = new CharArrayWriter();

		if (enc == null)
			throw new NullPointerException("charsetName");

		try
		{
			charset = Charset.forName(enc);
		}
		catch (IllegalCharsetNameException e)
		{
			throw new UnsupportedEncodingException(enc);
		}
		catch (UnsupportedCharsetException e)
		{
			throw new UnsupportedEncodingException(enc);
		}

		for (int i = 0; i < s.length();)
		{
			int c = (int) s.charAt(i);
			// System.out.println("Examining character: " + c);
			if (dontNeedEncoding.get(c))
			{
				if (c == ' ')
				{
					needToChange = true;
				}
				// System.out.println("Storing: " + c);
				out.append((char) c);
				i++;
			}
			else
			{
				// convert to external encoding before hex conversion
				do
				{
					charArrayWriter.write(c);
					/*
					 * If this character represents the start of a Unicode
					 * surrogate pair, then pass in two characters. It's not
					 * clear what should be done if a bytes reserved in the
					 * surrogate pairs range occurs outside of a legal surrogate
					 * pair. For now, just treat it as if it were any other
					 * character.
					 */
					if (c >= 0xD800 && c <= 0xDBFF)
					{
						/*
						 * System.out.println(Integer.toHexString(c) +
						 * " is high surrogate");
						 */
						if ((i + 1) < s.length())
						{
							int d = (int) s.charAt(i + 1);
							/*
							 * System.out.println("\tExamining " +
							 * Integer.toHexString(d));
							 */
							if (d >= 0xDC00 && d <= 0xDFFF)
							{
								/*
								 * System.out.println("\t" +
								 * Integer.toHexString(d) +
								 * " is low surrogate");
								 */
								charArrayWriter.write(d);
								i++;
							}
						}
					}
					i++;
				}
				while (i < s.length() && !dontNeedEncoding.get((c = (int) s.charAt(i))));

				charArrayWriter.flush();
				String str = new String(charArrayWriter.toCharArray());
				byte[] ba = str.getBytes(charset);
				for (int j = 0; j < ba.length; j++)
				{
					out.append('%');
					char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
					// converting to use uppercase letter as part of
					// the hex value if ch is a letter.
					if (Character.isLetter(ch))
					{
						ch -= caseDiff;
					}
					out.append(ch);
					ch = Character.forDigit(ba[j] & 0xF, 16);
					if (Character.isLetter(ch))
					{
						ch -= caseDiff;
					}
					out.append(ch);
				}
				charArrayWriter.reset();
				needToChange = true;
			}
		}

		return (needToChange ? out.toString() : s);
	}

	/**
	 * 将一段带有空格分隔字符串的单词首字符大写
	 * 
	 * @param str 字符串
	 * @return
	 */
	public static String toFirstCapsByStr(String str)
	{
		str = str.replaceAll("\\s{1,}", " ");
		String[] array = str.split(" ");
		StringBuffer sb = new StringBuffer();
		for (String s : array)
			sb.append(s.substring(0, 1).toUpperCase() + s.substring(1) + " ");
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * 例：toStr("2", "0-abc,2-123") 返回123
	 * 
	 * @param val
	 * @param rule
	 * @return
	 */
	public static String toStr(String val, String rule)
	{
		if(StringUtils.isEmpty(rule))
			return "";
		String[] obj = rule.split(",");
		Map<String, String> map = new HashMap<String, String>();
		for(String str : obj)
		{
			if(str.indexOf("-") == -1)
				continue;
			String[] array = str.split("-");
			map.put(array[0], array[1]);
		}
		return map.get(val);
	}
	/**
	 * 把字符串转成utf8编码，保证中文文件名不会乱码
	 * 
	 * @param 
	 * @return
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * @description 去除字符串中的回车、换行符、制表符（正则表达式）
	 * @param str
	 * @return
	 */
	public static String replaceTab(String str) {
            String dest = str;
            if (str != null) {
            	//不同操作系统间的换行符 win:\r\n ,linux :\n
            	String line = System.getProperty("line.separator");
                Pattern p = Pattern.compile(line);
                Matcher m = p.matcher(str);
                if(m.find()) {
                	dest = m.replaceFirst("");
                }
            }
            
            return dest;
        }

	/**
	 * 根据数值转换为里程数
	 * @param num
	 * @return
	 */
	public static String convertToStrFor2Pt(double num){
		DecimalFormat f = new DecimalFormat("0.00");
		if(num > 1000){
			return f.format(num/1000)+"公里";
		}else{
			return f.format(num) + "米";
		}
	}
	
	/**
	 * 以当天为基准，获得间隔一个月前的日期
	 * @return String 
	 * @throws ParseException
	 */
	public static String getPreOneMonth() throws ParseException {
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DATE, -1);    //得到前一天
	    calendar.add(Calendar.MONTH, -1);    //得到前一个月
	    int year = calendar.get(Calendar.YEAR);
	    int month = calendar.get(Calendar.MONTH)+1;
	    int day = calendar.get(Calendar.DATE);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String t = year + "-" + month +"-"+day;
	    Date val = sdf.parse(t);
	    return sdf.format(val);
	}
	
	/**
	 * 根据提供的日期集合获取除提供的日期集合外的剩余日期的集合
	 * @param dateStrs List<String>
	 * @return List<String>
	 * @throws ParseException
	 */
	public static List<String> getRemainDate(List<String> dateStrs) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String curtMonth = "";
		List<String> curtDateList = new ArrayList<String>();
		for(String str : dateStrs){
			//获取当前日期所在的本月所有日期，并排除当前日期，如果已获取过本月日期直接进行排除操作
			if(curtMonth.equals("")){
				curtMonth = str.substring(5,7);
				curtDateList = getAllTheDateOftheMonth(sdf.parse(str));
			}else{
				String curtMonth_temp = str.substring(5,7);
				if(!curtMonth_temp.equals(curtMonth)){
					List<String> curtNewDateList = getAllTheDateOftheMonth(sdf.parse(str));
					for(String curtNewDate : curtNewDateList){
						curtDateList.add(curtNewDate);
					}
					curtMonth = curtMonth_temp;
				}
			}
			
			int i = 0;
			for(String curtDate : curtDateList){
				if(str.equals(curtDate)){
					curtDateList.remove(i);
					break;
				}
				i++;
			}
			
		}
		return curtDateList;
	}
	
	/**
	 * 获取当前日期所属的当月所有日期集合，字符型表示
	 * @param curtDate
	 * @return
	 */
	public static List<String> getAllTheDateOftheMonth(Date curtDate) {
		List<String> res = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(curtDate);
		cal.set(Calendar.DATE, 1);

		int month = cal.get(Calendar.MONTH);
		while(cal.get(Calendar.MONTH) == month){
			list.add(cal.getTime());
			cal.add(Calendar.DATE, 1);
		}
		
		for(Date date: list){
			res.add(sdf.format(date));
		}
		return res;
	}

	/**
	 * 将十六进制字符串转换为字节数组
	 *
	 * @param str
	 * @return 字符数组
	 */
	public static byte[] stringToByteArray(String str) {

		int len = str.length();
		if ((len & 0x01) != 0) {
			throw new RuntimeException("odd number of str");
		}

		byte[] out = new byte[len >> 1];

		for (int i = 0, j = 0; j < len; i++) {

			int f = Character.digit(str.charAt(j), 16) << 4;
			j++;
			f = f | Character.digit(str.charAt(j), 16);
			j++;

			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}
}
