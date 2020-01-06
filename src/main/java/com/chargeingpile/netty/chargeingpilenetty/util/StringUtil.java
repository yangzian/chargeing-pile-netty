package com.chargeingpile.netty.chargeingpilenetty.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.Character.UnicodeBlock;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串操作工具类
 * 
 * @author summers
 * 
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {

	/**
	 * 编码转换 ISO2UTF8
	 * 
	 * @param oldString
	 * @return
	 */
	public static String filterBeforeDB(String oldString) {
		return ISO2UTF8(oldString);
	}

	/**
	 * 编码转换 UTF82ISO
	 * 
	 * @param oldString
	 * @return
	 */
	public static String filterAfterDB(String oldString) {
		return UTF82ISO(oldString);
	}

	/**
	 * 格式化日期对象，返回格式化后的字符串。
	 * 
	 * @param date
	 * @return
	 */
	public static String date2String(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String simpleDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 格式化日期对象，返回中文日期字符串
	 * 
	 * @param date
	 * @return 中文日期
	 */
	public static String simpleDateCH(Date date) {
		return formatDate(date, "yyyy年MM月dd日");
	}

	/**
	 * 编码转换 ISO2UTF8
	 * 
	 * @param oldString
	 * @return
	 */
	public static String ISO2UTF8(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("ISO8859_1"), "UTF-8");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * 编码转换 UTF82ISO
	 * 
	 * @param oldString
	 * @return
	 */
	public static String UTF82ISO(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("UTF-8"), "ISO8859_1");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * 编码转换 UTF82GB2312
	 * 
	 * @param oldString
	 * @return
	 */
	public static String UTF82GB2312(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("UTF-8"), "GB2312");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * 编码转换 GB23122UTF8
	 * 
	 * @param oldString
	 * @return
	 */
	public static String GB23122UTF8(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("GB2312"), "UTF-8");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * 编码转换 ISO2GB2312
	 * 
	 * @param oldString
	 * @return
	 */
	public static String ISO2GB2312(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("ISO8859_1"), "GB2312");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * GB23122ISO
	 * 
	 * @param oldString
	 * @return
	 */
	public static String GB23122ISO(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("GB2312"), "ISO8859_1");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * GBK2UTF8
	 * 
	 * @param oldString
	 * @return
	 */
	public static String GBK2UTF8(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("GBK"), "UTF-8");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * UTF82GBK
	 * 
	 * @param oldString
	 * @return
	 */
	public static String UTF82GBK(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("UTF-8"), "GBK");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * ISO2GBK
	 * 
	 * @param oldString
	 * @return
	 */
	public static String ISO2GBK(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("ISO8859_1"), "GBK");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * GBK2ISO
	 * 
	 * @param oldString
	 * @return
	 */
	public static String GBK2ISO(String oldString) {
		if (oldString == null)
			return null;
		try {
			return new String(oldString.getBytes("GBK"), "ISO8859_1");
		} catch (Exception e) {
			return oldString;
		}
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String openUrl(String url) {
		URLConnection conn = null;
		try {
			java.net.URL u = new java.net.URL(url);
			conn = u.openConnection();
			conn.connect();
			BufferedReader br = new BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream()));
			String line = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			return sb.toString();
		} catch (IOException ioe) {
		} finally {
		}
		return null;
	}

	/**
	 * 根据文件名，读取文件内容，文件内容返回字符串
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath, String charset) {
		if (charset == null)
			charset = "UTF-8";
		java.io.File file = null;
		java.io.BufferedReader br = null;
		try {
			file = new java.io.File(filePath);
			br = new java.io.BufferedReader(new java.io.InputStreamReader(
					new java.io.FileInputStream(file), charset));
			String line = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			return sb.toString();
		} catch (java.io.IOException ioe) {
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 字符串替换
	 * 
	 * @param oldString
	 * @param oldSubString
	 * @param newSubString
	 * @return
	 */
	public static String replaceString(String oldString, String oldSubString,
			String newSubString) {
		StringBuffer rtnValue = new StringBuffer();
		try {
			if (oldString.length() < oldSubString.length()) // 如果原始字符串比原始字符子串还小的话，返回原始字符串
				return oldString;
			String tempString = "";
			for (int oldstrCount = 0; oldstrCount <= oldString.length()
					- oldSubString.length(); oldstrCount++) {
				tempString = oldString.substring(oldstrCount, oldstrCount
						+ oldSubString.length());
				if (tempString.equals(oldSubString)) {
					rtnValue.append(newSubString);
					oldstrCount = oldstrCount + oldSubString.length() - 1;
					if (oldstrCount + 1 > oldString.length()
							- oldSubString.length())
						rtnValue.append(oldString.substring(oldstrCount + 1));
				} else {
					rtnValue.append(oldString.charAt(oldstrCount));
					if (oldstrCount == oldString.length()
							- oldSubString.length()) {
						rtnValue.append(oldString.substring(oldstrCount + 1));
					}
				}
			}
		} catch (Exception e) {
			return oldString;
		}
		return rtnValue.toString();
	}

	/**
	 * 返回自动生成的图片名称
	 * 
	 * @return
	 */
	public static final String genPictureName() {
		String prefix = formatDate(new Date(), "yyyyMMddHHmmss");
		prefix += randomNumber(6);
		return prefix + ".jpg";
	}

	/**
	 * 生成随机数字
	 * 
	 * @param length
	 * @return
	 */
	public static final String randomNumber(int length) {
		char[] numbersAndLetters = null;
		java.util.Random randGen = null;
		if (length < 1) {
			return null;
		}
		// Init of pseudo random number generator.
		if (randGen == null) {
			if (randGen == null) {
				randGen = new java.util.Random();
				// Also initialize the numbersAndLetters array
				numbersAndLetters = ("0123456789").toCharArray();
			}
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(9)];
		}
		return new String(randBuffer);
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static final String randomString(int length) {
		char[] numbersAndLetters = null;
		java.util.Random randGen = null;
		if (length < 1) {
			return null;
		}
		// Init of pseudo random number generator.
		if (randGen == null) {
			if (randGen == null) {
				randGen = new java.util.Random();
				// Also initialize the numbersAndLetters array
				numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
						+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
			}
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 根据JNDI返回组件
	 * 
	 * @param jndiURI
	 * @return
	 */
	public static Object getComponent(String jndiURI) {
		try {
			javax.naming.Context ctx = new javax.naming.InitialContext();
			return ctx.lookup(jndiURI);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串分割
	 * 
	 * @param oldString
	 * @param delim
	 * @return
	 */
	public static String[] split(String oldString, String delim) {
		if (oldString == null)
			return null;
		String[] newArray = null;
		java.util.StringTokenizer st = new java.util.StringTokenizer(oldString,
				delim);
		newArray = new String[st.countTokens()];
		int count = 0;
		while (st.hasMoreTokens()) {
			newArray[count] = st.nextToken().trim();
			count++;
		}
		return newArray;
	}

	/**
	 * 格式化日期
	 * 
	 * @param d
	 * @return
	 */
	public static String formatDate(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}

	/**
	 * 格式化日期
	 * 
	 * @param d
	 * @return
	 */
	public static String formatDatess(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return sdf.format(d);
	}

	/**
	 * 格式化日期
	 * 
	 * @param d
	 * @return
	 */
	public static String formatDates(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}

	/**
	 * 格式化日期
	 * 
	 * @param d
	 * @return
	 */
	public static String formatDateMM(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm");
		return sdf.format(d);
	}

	/*
	 * public static String formatDate(Date d) { SimpleDateFormat sdf = new
	 * SimpleDateFormat("EEE yyyy-MM-dd G HH:mm:ss z"); return sdf.format(d); }
	 */
	/**
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parseDate(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format,
				java.util.Locale.US);
		ParsePosition pos = new ParsePosition(0);
		Date ret = formatter.parse(date, pos);
		return ret;
	}

	/**
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parseDate2(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date ret = null;
		try {
			ret = sdf.parse(date);
		} catch (ParseException pe) {
			System.out.println(pe.getMessage());
		}
		return ret;
	}

	public static String format = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 根据给定格式，格式化日期
	 * 
	 * @param newDate
	 * @param format
	 * @return
	 */
	public static String formatDate(java.util.Date newDate, String format) {
		if (newDate == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(newDate);
	}

	/**
	 * MD5加密
	 * 
	 * @param orgString
	 * @return
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String md5Encrypt(String orgString) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(orgString.getBytes());
			byte[] b = md.digest();
			return byte2hex(b);
		} catch (java.security.NoSuchAlgorithmException ne) {
			throw new IllegalStateException(
					"System doesn't support your  Algorithm.");
		}
	}

	public static String md5Encrypt(String orgString, String charSet) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(orgString.getBytes(charSet));
			byte[] b = md.digest();
			return byte2hex(b);
		} catch (java.security.NoSuchAlgorithmException ne) {
			throw new IllegalStateException(
					"System doesn't support your  Algorithm.");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new IllegalStateException("System doesn't support chaset");
		}
	}

	/**
	 * 将字节数组转换成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) // 二行制转字符串

	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	/**
	 * 从数据库读出钱时，调用这个函数（数据库保存的金额为分，前台显示为元）
	 */
	public static String formatMoneyFromData(String price) {
		if (price == null)
			return price;
		if (price.indexOf(".") == 0)
			return "0";
		if (price.indexOf(".") > 0)
			price = price.substring(0, price.indexOf("."));
		int money = Integer.parseInt(price);
		int mod = money % 100;
		int imod = money / 100;
		String temp = "";
		// if ( mod == 0 )
		// temp = new String ("00");
		// else
		if ((mod < 10) && (mod > -10))
			temp = ".0" + mod;
		else
			temp = "." + mod;
		return "" + imod + temp;
	}

	/**
	 * 将分格式化为元，去掉小数点后的0
	 * 
	 * @param price
	 * @return
	 */
	public static String formatMoney(String price) {
		Long money = Long.valueOf(price) / 100;
		money.shortValue();
		return money.toString();
	}

	/**
	 * This method takes a string which may contain HTML tags (ie, &lt;b&gt;,
	 * &lt;table&gt;, etc) and converts the '&lt'' and '&gt;' characters to
	 * their HTML escape sequences.
	 * 
	 * @param input
	 *            the text to be converted.
	 * @return the input string with the characters '&lt;' and '&gt;' replaced
	 *         with their HTML escape sequences.
	 */
	public static final String escapeHTMLTags(String input) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (input == null || input.length() == 0) {
			return input;
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else if (ch == '&') {
				buf.append("&amp;");
			} else if (ch == '"') {
				buf.append("&quot;");
			} else if (ch == '\'') {
				buf.append("&#39;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * Returns the specified Cookie object, or null if the cookie does not
	 * exist.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            the name of the cookie.
	 * @return the Cookie object if it exists, otherwise null.
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies == null || name == null || name.length() == 0) {
			return null;
		}
		// Otherwise, we have to do a linear scan for the cookie.
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(name)) {
				return cookies[i];
			}
		}
		return null;
	}

	/**
	 * Returns the value of the specified cookie as a String. If the cookie does
	 * not exist, the method returns null.
	 * 
	 * @param request
	 *            the HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            the name of the cookie
	 * @return the value of the cookie, or null if the cookie does not exist.
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		if (cookie != null) {
			return cookie.getValue();
		}
		return null;
	}

	/**
	 * set post message cookie
	 * 
	 * @param res
	 *            , response
	 * @param name
	 *            , cookie name
	 * @param value
	 *            , cookie value
	 */
	public static void SetPostCookie(HttpServletResponse res, String name,
			String value) {
		Cookie c = new Cookie(name, value);
		c.setPath("/");
		res.addCookie(c);
	}

	/**
	 * remove post message cookie
	 * 
	 * @param res
	 *            , response
	 * @param name
	 *            , cookie name
	 */
	public static void RemoviePostCookie(HttpServletResponse res, String name) {
		Cookie c = new Cookie(name, "");
		c.setPath("/");
		res.addCookie(c);
	}

	/**
	 * 检查是否有POST信息，并且输出
	 * 
	 * @param req
	 *            , request
	 * @param res
	 *            , response
	 */
	public static String ShowMessage(HttpServletRequest req,
			HttpServletResponse res) {
		String tmpstr = "";
		if (GetCookieByName(req, "Message") != null)
			tmpstr = "<br><br><font size=3 color=red><b>"
					+ GetCookieByName(req, "Message") + "</b></font>";
		RemoviePostCookie(res, "Message");
		return tmpstr;
	}

	/**
	 * get the cookie value by name
	 * 
	 * @param res
	 *            , response
	 * @param name
	 *            , cookie name
	 */
	public static String GetCookieByName(HttpServletRequest req, String name) {

		Cookie[] c = req.getCookies();
		if (c != null) {
			for (int i = 0; i < c.length; i++) {
				String tmpstr = c[i].getName();
				if (tmpstr.compareTo(name) == 0) {
					if (c[i].getValue() != null)
						return c[i].getValue();
					else
						return null;
				}
			}
		}
		return null;
	}

	public static String convertSpecialCharForDB(String originalString) {
		String rtnValue = null;
		StringBuffer newString = new StringBuffer();
		if (originalString != null) {
			for (int i = 0; i < originalString.length(); i++) {
				switch (originalString.charAt(i)) {
				case '\'':
					newString.append(originalString.charAt(i) + "'");
					break;
				default:
					newString.append(originalString.charAt(i));
				}
			}
			rtnValue = newString.toString();
		}
		return rtnValue;
	}

	/**
	 * 将输入的原始字符串中的符合所给正则表达式的字符标红
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	public static StringBuffer markRedForString(String str, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "<font color=\"#cc0033\">" + m.group()
					+ "</font>");
		}
		m.appendTail(sb);
		return sb;
	}

	/**
	 * 获取批次号
	 * 
	 * @param number
	 * @param numberLength
	 * @return
	 */
	/*
	 * public static String generateBatchID(String prefix, List<ListOrderedMap>
	 * idLists, int length) { if (idLists == null || idLists.size() == 0) return
	 * prefix + getFormatString(0, length); Iterator<ListOrderedMap> ii =
	 * idLists.iterator(); int id = 0; while (ii.hasNext()) { ListOrderedMap map
	 * = ii.next(); String no = (String) map.getValue(0); if (no != null &&
	 * no.startsWith(prefix)) { no = no.substring(prefix.length()); if
	 * (no.length() > 0) { try { int currentId = Integer.parseInt(no); if
	 * (currentId > id) id = currentId; } catch (NumberFormatException nfe) { }
	 * } }
	 * 
	 * } return prefix + getFormatString(id + 1, length); }
	 */

	public static String getFormatString(int number, int numberLength) {
		String num = String.valueOf(number);
		int length = num.length();
		if (length == numberLength) // 如果数字的长度域最大长度相等，则返回原数字。
			return num;
		StringBuffer sb = new StringBuffer();
		for (int i = length; i < numberLength; i++)
			sb.append("0");
		return sb.toString() + num;
	}

	public static long getLongValue(Long var) {
		if (var == null) {
			return 0l;
		} else {
			return var.longValue();
		}
	}

	public static long getLongValue(String var) {
		if (isBlank(var)) {
			return 0l;
		} else {
			return Long.valueOf(var);
		}
	}

	public static java.util.Date addDay(java.util.Date date, int n) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		for (int i = 0; i < n; i++) {
			ca.roll(Calendar.DAY_OF_YEAR, true);
			int day = ca.get(Calendar.DAY_OF_YEAR);
			if (day == 1) {
				ca.roll(Calendar.YEAR, true);
			}
		}
		return ca.getTime();
	}

	public static java.util.Date MinusDay(java.util.Date date, int n) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		for (int i = 0; i < n; i++) {
			ca.roll(Calendar.DAY_OF_YEAR, false);
			int day = ca.get(Calendar.DAY_OF_YEAR);
			if (day == 1) {
				ca.roll(Calendar.YEAR, false);
			}
		}

		return ca.getTime();
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static long getCompareDate(String startDate, String endDate)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = formatter.parse(startDate);
		Date date2 = formatter.parse(endDate);
		long l = date2.getTime() - date1.getTime();
		long d = l / (24 * 60 * 60 * 1000);
		return d;
	}

	public static int longToInt(long value) {
		return Integer.valueOf(Long.valueOf(value).toString());
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String toSBC(String input) {
		if (input == null)
			return null;
		try {
			char c[] = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == ' ') {
					c[i] = '\u3000';
				} else if (c[i] < '\177') {
					c[i] = (char) (c[i] + 65248);

				}
			}
			return new String(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	@SuppressWarnings("unused")
	public static String toDBC(String input) {
		if (input == null)
			return null;
		try {
			char c[] = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == '\u3000') {
					c[i] = ' ';
				} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
					c[i] = (char) (c[i] - 65248);

				}
			}
			String returnString = new String(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	/**
	 * 特殊字符转义
	 * 
	 * @param input
	 *            String.
	 * @return 转义后的字符串
	 */
	public static String enCode(String content) {
		String str1 = content.replace("<", "&lt;");
		String str2 = str1.replace(">", "&gt;");
		String str3 = str2.replace("'", "&apos;");
		String str4 = str3.replace(" ", "&nbsp;");
		String str5 = str4.replace("\r\n", "<br>");
		String str6 = str5.replace("\"", "&quot;");
		String str7 = str6.replace("&", "&amp;");
		return str7;

	}

	/**
	 * 产品终极页链接地址
	 * 
	 * @param input
	 *            String.
	 * @return 转义后的字符串
	 */
	public static String getProductLinkUrl(String productId, String param) {
		String url = "/product/" + productId + ".html";
		if (param != null && "test".equals(param)) {
			url = "/goods/productDetail_v4.jsp?productId=" + productId;
		}
		return url;
	}

	public static String stringJoint(String oper, String... str) {
		String result = "";
		if (str != null) {
			for (int i = 0; i < str.length; i++) {
				result += str[i] + oper;
			}
		}
		if (result.length() > 1) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * 整数转成ip地址.
	 * 
	 * @param ipLong
	 * @return
	 */
	public static String long2ip(long ipLong) {
		// long ipLong = 1037591503;
		long mask[] = { 0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000 };
		long num = 0;
		StringBuffer ipInfo = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			num = (ipLong & mask[i]) >> (i * 8);
			if (i > 0)
				ipInfo.insert(0, ".");
			ipInfo.insert(0, Long.toString(num, 10));
		}
		return ipInfo.toString();
	}

	/**
	 * ip地址转成整数.
	 * 
	 * @param ip
	 * @return
	 */
	public static long ip2long(String ip) {
		String[] ips = ip.split("[.]");
		long num = 16777216L * Long.parseLong(ips[0]) + 65536L
				* Long.parseLong(ips[1]) + 256 * Long.parseLong(ips[2])
				+ Long.parseLong(ips[3]);
		return num;
	}

	/**
	 * 价格字符串格式化去尾数0
	 */
	public static String productPriceFormat(String str) {
		String format = str;
		String last1 = format.substring(format.indexOf(".") + 2);// 最后一位
		if (last1.equals("0")) {
			format = format.substring(0, format.length() - 1);// 最后一位是零的话去掉0
			String last2 = format.substring(format.indexOf(".") + 1);// 去掉0之后的字符串的最后一位
			if (last2.equals("0")) {
				format = format.substring(0, format.length() - 2);// 去掉0之后的字符串的最后一位仍是0的话去掉0和.
			}
		}
		return format;
	}

	/**
	 * 获取新版注册返回url
	 * 
	 * @param str
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getFormatTourl(String tourl, HttpServletRequest request) {
		if (tourl.indexOf("buycard.jsp") != -1) {
			StringBuffer param = new StringBuffer();
			StringBuffer temp = new StringBuffer();
			Enumeration enu = request.getParameterNames();
			int total = 0;
			while (enu.hasMoreElements()) {
				String name = (String) enu.nextElement();
				if (name.equals("tourl") || name.equals("money")
						|| name.equals("number") || name.equals("type")) {
					if (total == 0) {
						if (name.equals("tourl")) {
							temp.append(request.getParameter(name)).append("&");
						} else {
							param.append(name).append("=")
									.append(request.getParameter(name));
						}
					} else {
						if (name.equals("tourl")) {
							temp.append(request.getParameter(name)).append("&");
						} else {
							param.append("&").append(name).append("=")
									.append(request.getParameter(name));
						}
					}
					total++;
				}
			}// while
			String result = temp.toString() + param.toString();
			if (result.substring(result.length() - 1, result.length()).equals(
					"&")) {
				return result.substring(0, result.length() - 1);
			} else {
				return result;
			}
		} else {
			return tourl;
		}
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否为英文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEnglish(String str) {
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 转换为unicode编码
	 * 
	 * @author zhanglei
	 * @param sourceStr
	 * @return
	 */
	public static String toUnicode(String sourceStr) {
		char[] tmpBuffer = sourceStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tmpBuffer.length; i++) {
			UnicodeBlock ub = UnicodeBlock.of(tmpBuffer[i]);
			if (ub == UnicodeBlock.BASIC_LATIN) {
				sb.append(tmpBuffer[i]);
			} else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				int s = (int) tmpBuffer[i] - 65248;
				sb.append((char) s);
			} else {
				int s = (int) tmpBuffer[i];
				String hexStr = Integer.toHexString(s);
				String unicode = "\\u" + hexStr;
				sb.append(unicode.toLowerCase());
			}
		}
		return sb.toString();
	}

	/**
	 * 读取Excel文件 add by cuilili
	 * 
	 * @param is
	 *            excel文件
	 * @param sheetIndex
	 *            sheet索引
	 * @return
	 */
	/*
	 * @SuppressWarnings({ "rawtypes", "unchecked" }) public static List
	 * readExcelFile(InputStream is,int sheetIndex) { List list = new
	 * ArrayList(); try { Workbook workbook = Workbook.getWorkbook(is);
	 * jxl.Sheet sheet = workbook.getSheet(sheetIndex); for (int i = 1; i <
	 * sheet.getRows(); i++) { try { Map map = new HashMap(); for(int
	 * j=0;j<sheet.getColumns();j++){ map.put(j, sheet.getCell(j,
	 * i).getContents()); } list.add(map); }catch (Exception e) {
	 * System.out.println(e.getMessage()); } } }catch (Exception e) {
	 * System.out.println(e.getMessage()); return null; } return list; }
	 */

	/**
	 * 读取Url内容
	 * 
	 * @param url
	 * @return
	 */
	public static String readUrlContent(String url) {
		String urlContent = "";
		HttpURLConnection con = null;
		BufferedReader URLinput = null;
		try {
			URL u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setInstanceFollowRedirects(false);
			con.setUseCaches(false);
			con.setAllowUserInteraction(false);
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept-Language", "zh-cn");
			con.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) )");// 模拟windows200
																																			// IE6
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.connect();

			StringBuffer returnsb = new StringBuffer();
			String line = "";
			URLinput = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			while ((line = URLinput.readLine()) != null) {
				returnsb.append(line);
			}
			con.disconnect();
			urlContent = returnsb.toString();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("read url content error------------"
					+ e.getMessage());
		} finally {
			if (con != null) {
				con.disconnect();
			}
			if (URLinput != null) {
				try {
					URLinput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return urlContent;
	}

	/**
	 * 读取Url内容
	 * 
	 * @param url
	 * @return
	 */
	public static String readUrlContentByPost(String url) {
		String urlContent = "";
		HttpURLConnection con = null;
		BufferedReader URLinput = null;
		try {
			URL u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setInstanceFollowRedirects(false);
			con.setUseCaches(false);
			con.setAllowUserInteraction(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "zh-cn");
			con.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) )");// 模拟windows200
																																			// IE6
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.connect();

			StringBuffer returnsb = new StringBuffer();
			String line = "";
			URLinput = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			while ((line = URLinput.readLine()) != null) {
				returnsb.append(line);
			}
			con.disconnect();
			urlContent = returnsb.toString();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(urlContent + "=========urlContent");
			System.out.println("read url content error------------"
					+ e.getMessage());
		} finally {
			if (con != null) {
				con.disconnect();
			}
			if (URLinput != null) {
				try {
					URLinput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return urlContent;
	}

	/**
	 * @param xml
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static Map parseXmlByNodes(String xml) throws SAXException,
			IOException, ParserConfigurationException {
		Map<String, String> map = new HashMap<String, String>();
		StringReader read = new StringReader(xml);
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder;
		dombuilder = domfac.newDocumentBuilder();

		InputSource is = new InputSource(read);
		Document doc = dombuilder.parse(is);
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			setNodesInMap(map, node);
		}
		return map;
	}

	private static void setNodesInMap(Map map, Node node) {
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node childNode = childNodes.item(i);
			if (node.hasChildNodes()) {
				setNodesInMap(map, childNode);
			}
			// System.out.println(childNode.getParentNode().getNodeName() +
			// "------" +childNode.getNodeValue());
			map.put(childNode.getParentNode().getNodeName(),
					childNode.getNodeValue());
		}

	}

	/*
	 * public static String getUserName(String userName){ String
	 * result=userName; if(EmailValidator.getInstance().isValid(userName)){ int
	 * index=userName.indexOf("@"); String before=userName.substring(0,index);
	 * String after=userName.substring(index,userName.length());
	 * result=before.substring(0, 2)+"**"+after; }
	 * if(StringUtils.isNumeric(userName) && userName.length()==11){
	 * result=userName.substring(0, 3)+"****"+userName.substring(7, 11); }
	 * return result; }
	 */

	public static String getUserAvatar(String uid) {
		String result = "http://img1.imghm.com/uc/images/noavatar_middle.gif";
		if (!"0".equals(uid)) {
			int length = uid.length();
			String str = "";
			for (int i = 0; i < 9 - length; i++) {
				str += "0";
			}
			uid = str + uid;
			int index = random.nextInt(10) + 1;
			String imgurl = "http://img" + index + ".imghm.com/uc/avatar/"
					+ uid.substring(0, 3) + "/" + uid.substring(3, 5) + "/"
					+ uid.substring(5, 7) + "/" + uid.substring(7, 9)
					+ "_avatar_middle.jpg";
			if (checkImgUrl(imgurl)) {
				result = imgurl;
			}
		}
		return result;
	}

	public static boolean checkImgUrl(String img_url) {
		try {
			URL url = new URL(img_url);
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.setRequestMethod("GET");
			urlcon.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded");
			if (urlcon.getResponseCode() == 200) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("user avatar img url " + img_url
					+ " check error:" + e.getMessage());
			return false;
		}
	}

	/**
	 * 平均分样式
	 * 
	 * @param avgScore平均分
	 * @return
	 */
	public static String getStar(String avgScore) {
		String starCss = "";
		if (StringUtil.isNotBlank(avgScore)) {
			String[] strs = avgScore.split("\\.");
			// 如果分数有小数部分
			if (strs.length > 1) {
				int intStar = Integer.parseInt(strs[0]) * 2;
				// 小数部分
				int i = Integer.parseInt(strs[1]);
				if (i == 0) {
				} else {
					intStar += 1;
				}
				starCss = "" + intStar;
			} else {
				starCss = "" + (Integer.parseInt(avgScore) * 2);
			}
		}
		return starCss;

	}

	/**
	 * 是否包含SQL特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean sql_inj(String str) {
		String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|+|,";
		String inj_stra[] = split(inj_str, "|");
		for (int i = 0; i < inj_stra.length; i++) {
			if (str.indexOf(inj_stra[i]) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 截取字符串区分中英文
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String subString(String s, int length) {
		StringBuffer o = new StringBuffer();
		if (StringUtil.isBlank(s)) {
			return "";
		}
		int size = 0;
		for (int i = 0; i < s.length(); i++) {

			if (s.charAt(i) >= 0x0391 && s.charAt(i) <= 0xffe5) {
				size += 2;
				if (size > length) {
					break;
				} else {
					o.append(s.charAt(i));
				}
			} else {
				size += 1;
				if (size > length) {
					break;
				} else {
					o.append(s.charAt(i));
				}

			}
		}
		return o.toString();
	}

	/*
	 * public static void sendSMS(String mobile,String message){
	 * readUrlContent(UserConstants.SMS_URL + "&phone=" + mobile + "&content=" +
	 * message); }
	 */
	/**
	 * 判断是否第三方登录名
	 * 
	 * @return
	 */
	static List<String> unionList = new ArrayList<String>();
	static {

		unionList.add("t_sina_");
		unionList.add("t_139_");
		unionList.add("t_360_");
		unionList.add("t_alipay_");
		unionList.add("t_k001_");
		unionList.add("t_netease_");
		unionList.add("t_qq_");
		unionList.add("t_txqq_");
		unionList.add("t_renren_");
		unionList.add("t_wanwu_");
		unionList.add("t_qqcb_");
	}

	public static boolean isUnionLogin(String login) {

		for (String str : unionList) {
			if (login.startsWith(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 用于判断URL是否是乐峰的链接
	 * 
	 * @param url
	 *            需要判断的URL
	 * @author zhanghanlin
	 * @return
	 */
	static String[] whitelist = { "hm.com.cn", "hm.com", "hm.cn", "imghm.com",
			"hm.com", "fengxiangbiao.com", "tvj.com.cn" };

	public static boolean urlFilter(String url) {
		if (isNotBlank(url) && url.indexOf("http") > -1) {

			for (String key : whitelist) {
				if (url.contains(key)) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	// 登录名是否包含特殊字符
	public static boolean StringFilter(String str)
			throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		boolean b = m.find();

		return b;
	}

	/**
	 * 判断是否为中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinaString(String str) {

		// 字符串中是否有中文
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * FunName: isValidEmail Description: 判断输入字符串是否为一个有效的邮箱
	 * 
	 * @param email
	 *            输入字符串
	 * @return boolean true:有效邮箱，false:无效邮箱
	 */
	public static boolean isValidEmail(String email) {
		boolean result = false;
		String regexEmail = "[a-zA-Z0-9_-][\\.a-zA-Z0-9_-]*@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(email);
		result = matcher.matches();
		return result;
	}

	static Random random = new Random();

	/**
	 * 验证密码强度
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkPassword(String str) {
		if (StringUtil.isBlank(str)) {
			return false;
		} else if (StringUtil.isNumeric(str)) {
			return false;
		} else if (StringUtil.isEnglish(str)) {
			return false;
		}
		return true;
	}

	/**
	 * <pre>
	 * 发送带参数的POST的HTTP请求
	 * </pre>
	 * 
	 * @param reqUrl
	 *            HTTP请求URL
	 * @param parameters
	 *            参数映射表
	 * @return HTTP响应的字符串
	 */
	public static String doPost(String reqUrl, Map parameters,
			String recvEncoding) {
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Iterator iter = parameters.entrySet().iterator(); iter
					.hasNext();) {
				Entry<String, Object> element = (Entry) iter.next();
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(),
						recvEncoding));
				params.append("&");
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}

			URL url = new URL(reqUrl);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			url_con.setConnectTimeout(1000 * 2);// （单位：毫秒）jdk 1.5换成这个,连接超时
			url_con.setReadTimeout(1000 * 2);// （单位：毫秒）jdk 1.5换成这个,读操作超时
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return responseContent;
	}

	/**
	 * 返回商品图片地址
	 * 
	 * @param productId
	 *            商品ID
	 * @param group
	 *            图片组
	 * @param type
	 *            图片大小类型
	 * @return
	 */
	public static String product_imgUrl(String productId, int group, String type) {
		String uid = leftPad(productId, 15,
				"0");
		Random random1 = new Random();
		int index = 1 + random1.nextInt(3);
		String imgurl = "http://img" + index + ".imghm.com/images/product/"
				+ uid.substring(0, 3) + "/";
		if (productId.length() >= 12) {
			imgurl = "http://pimg3.imghm.com/images/product/"
					+ uid.substring(0, 3) + "/";
		}
		imgurl += uid.substring(3, 6) + "/" + uid.substring(6, 9) + "/"
				+ uid.substring(9, 12) + "/" + uid.substring(12, 15) + "/"
				+ productId + "_" + group + "_" + type + ".jpg";
		return imgurl;
	}

	/**
	 * 判断是否是手机号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNum(String str) {
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 获取当前时间的毫秒值
	 * 
	 * @return
	 */
	public static long getLongTimeByNow() {
		return new Date().getTime();
	}

	/**
	 * 查找数组中是否包含
	 * 
	 * @param arr
	 * @param num
	 * @return
	 */
	public static boolean isContainArr(int[] arr, int num) {
		for (int a : arr) {
			if (a == num) {
				return true;
			}
		}
		return false;
	}

	/*
	 * public static boolean isNotIdCardNum(String idcard){ boolean flag =
	 * false; Pattern p =
	 * Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$"); Matcher m =
	 * p.matcher(idcard);
	 * 
	 * List<String> umns6 = Arrays.asList(UserConstants.USER_IDCARD_NUMS6);
	 * if(isNotBlank(idcard) && m.find()){
	 * 
	 * if(!umns6.contains(idcard.substring(0, 2))){ //判断前两位是否正确 return true; }
	 * 
	 * if(idcard.length() == 15){ //一代身份证校验 判断 7-12位是否合规 ---- start int year =
	 * Integer.parseInt(idcard.substring(6,8)); int month =
	 * Integer.parseInt(idcard.substring(8,10)); int day =
	 * Integer.parseInt(idcard.substring(10,12));
	 * 
	 * if(year <= 0){ return true; } if(month <= 0 || month > 12){ return true;
	 * }
	 * 
	 * Calendar c = Calendar.getInstance(); c.set(Calendar.YEAR, year);
	 * c.set(Calendar.MONTH, month); c.set(Calendar.DAY_OF_MONTH, 0); if(day <=
	 * 0 || day > c.get(Calendar.DAY_OF_MONTH)){ return true; } 判断 7-12位是否合规
	 * ---- end
	 * 
	 * }else if(idcard.length() == 18){ //二代身份证校验
	 * 
	 * 判断 7-14位是否合规 ---- start int year =
	 * Integer.parseInt(idcard.substring(6,10)); int month =
	 * Integer.parseInt(idcard.substring(10,12)); int day =
	 * Integer.parseInt(idcard.substring(12,14));
	 * 
	 * if(year <= 0){ return true; } if(month <= 0 || month > 12){ return true;
	 * }
	 * 
	 * Calendar c = Calendar.getInstance(); c.set(Calendar.YEAR, year);
	 * c.set(Calendar.MONTH, month); c.set(Calendar.DAY_OF_MONTH, 0); if(day <=
	 * 0 || day > c.get(Calendar.DAY_OF_MONTH)){ return true; } 判断 7-14位是否合规
	 * ---- end
	 * 
	 * 根据第18位生成规则验证是否正确 ----- start int sumIndexNUm = 0; for(int
	 * i=0;i<idcard.length()-1;i++){ int indexNum =
	 * Integer.parseInt(String.valueOf(idcard.charAt(i))); sumIndexNUm +=
	 * indexNum * UserConstants.USER_IDCARD_NUMS17[i]; }
	 * 
	 * int num18key = sumIndexNUm % 11;
	 * 
	 * if(!String.valueOf(idcard.charAt(17)).equalsIgnoreCase(UserConstants.
	 * USER_IDCARD_NUM18[num18key])){ return true; } 根据第18位生成规则验证是否正确 ----- end
	 * } } return flag; }
	 */
	static HttpServletRequest request;

	// public static void main(String[] args){
	// System.out.println(StringUtil.md5Encrypt("1234567","UTF-8"));
	// System.out.println(isNotIdCardNum("22010219890307009x"));
	// 获取session e10adc3949ba59abbe56e057f20f883e
	// System.out.println(StringUtil.md5Encrypt("123456", "UTF-8"));

	// HttpServletRequest req = (HttpServletRequest) request;
	// HttpServletResponse resp = (HttpServletResponse) response;
	// HttpServletResponse resp = (HttpServletResponse) response;
	// HttpServletRequest request; HttpServletResponse response;
	// HttpSession session = req.getSession();
	// session.setAttribute("aa", 1);
	// System.out.println(session.getId().);
	// }

	/**
	 * 得到ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		// 验证ip地址是否合法
		return getLegitimateIP(ip.split(","));
	}

	/**
	 * 得到合法ip地址
	 * 
	 * @param ips
	 * @return
	 */
	public static String getLegitimateIP(String... ips) {
		Matcher matcher; // 以验证127.400.600.2为例
		if (null != ips && ips.length > 0) {
			for (String ip : ips) {
				matcher = pattern.matcher(ip);
				if (matcher.matches()) {
					return ip;
				}
			}
		}

		return "Illegal IP";
	}

	static Pattern pattern = Pattern
			.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

	public static String getNickNameByLogin(String login) {
		if (StringUtil.isBlank(login)) {
			return "默认名";
		}
		login = login.trim();
		StringBuilder sb = new StringBuilder();
		if (isPhoneNum(login)) {
			sb.append(login.substring(0, 3));
			sb.append("****");
			sb.append(login.substring(8));
		} else if (isValidEmail(login)) {
			sb.append(login.split("@")[0]);
		} else {
			sb.append(login);
		}
		return sb.toString();
	}

	public static Long hash(String key) {
		ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;

		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		long m = 0xc6a4a7935bd1e995L;
		int r = 47;

		long h = seed ^ (buf.remaining() * m);

		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h ^= k;
			h *= m;
		}
		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(
					ByteOrder.LITTLE_ENDIAN);
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}

		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;
		buf.order(byteOrder);
		return h;
	}

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	/**
	 * 生成图片视频名
	 * 
	 * @return
	 */
	public static final String genOrderNum() {
		String prefix = formatDate(new Date(), "yyyyMMddHHmmss");
		prefix += randomNumber(6);
		return "BL_" + prefix;
	}

	/**
	 * 返回抽验单号
	 * 
	 * @return
	 */
	public static final String genSamplingNum() {
		String prefix = formatDate(new Date(), "yyyyMMdd");
		prefix += randomNumber(4);
		return prefix;
	}

	/**
	 * 
	 * @return
	 */
	public static final String genyyyyMMddHHmmssDate() {
		String prefix = formatDate(new Date(), "yyyyMMddHHmmss");
		return prefix;
	}

	/**
	 * 从小到大排序
	 * 
	 * @param str
	 * @return
	 */
	public static Integer[] sort(String str) {
		String[] strs = str.split(",", 1000);
		Integer[] is = new Integer[strs.length];
		int i = 0;

		for (String s : strs) {
			if (isInteger(s)) {
				// 把字符转换成字符串
				int d = Integer.parseInt(s); // 把字符串转换成数字
				is[i] = d;
				i++;
			}
		}

		Arrays.sort(is);
		return is;
	}

	/**
	 * 判断一个String是否为Integer类型
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 获取当前系统时间转成字符串
	 * 
	 * @author 陈运江
	 */
	public String refFormatNowDate() {
		Date nowTime = new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String retStrFormatNowDate = sdFormatter.format(nowTime);
		return retStrFormatNowDate;
	}

	/**
	 * 取随机字符串
	 * 
	 * @param length
	 *            返回随机字符串的长度
	 * @param type
	 *            要取的字符串类型: i、取数字 l、取小写字母 u、取大写字母 s、取特殊字符
	 * @return String 随机字符串
	 */
	public String getRandomString(int length, String type) {
		String splitStr = " "; // 分割符
		String allStr = this.getString(type);
		String[] arrStr = allStr.split(splitStr);
		StringBuffer pstr = new StringBuffer();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				pstr.append(arrStr[new Random().nextInt(arrStr.length)]);
			}
		}
		return pstr.toString();
	}

	// 根据所取的字符串类型连接相应的字符串并返回
	private String getString(String type) {
		StringBuffer pstr = new StringBuffer();
		if (type.length() > 0) {
			if (type.indexOf('i') != -1)
				pstr.append(this.getNumberString());
			if (type.indexOf('l') != -1)
				pstr.append(this.getLowercase());
			if (type.indexOf('u') != -1)
				pstr.append(this.getUppercase());
			if (type.indexOf('s') != -1)
				pstr.append(this.getSpecialString());

		}
		return pstr.toString();
	}

	// 取数字字符串 用 splitStr 分割
	private String getNumberString() {
		String splitStr = " "; // 分割符

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 10; i++) {

			buf.append(String.valueOf(i));
			buf.append(splitStr);
		}
		return buf.toString();
	}

	// 取大写字母字符串 用 splitStr 分割
	private String getUppercase() {
		String splitStr = " "; // 分割符
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 26; i++) {
			buf.append(String.valueOf((char) ('A' + i)));
			buf.append(splitStr);
		}
		return buf.toString();
	}

	// 取小写字母字符串 用 splitStr 分割
	private String getLowercase() {
		String splitStr = " "; // 分割符
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 26; i++) {
			buf.append(String.valueOf((char) ('a' + i)));
			buf.append(splitStr);
		}
		return buf.toString();
	}

	// 取特殊字符串 用 splitStr 分割
	private String getSpecialString() {
		String splitStr = " "; // 分割符
		String str = "~@#$%^&*()_+|\\=-`";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			buf.append(str.substring(i, i + 1));
			buf.append(splitStr);
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		String str = new StringUtil().getRandomString(10, "ilu");
		System.out.println(str);
	}

	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
	
	public static List<Map<String,Object>> sortFinished(List<Map<String,Object>> lst) {
		
		
		Collections.sort(lst, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o2, Map<String, Object> o1) {
				return ((Date) o1.get("overTime")).compareTo((Date) o2.get("overTime"));
			}
		});
		
		return lst;
	}
	
	public static List<Map<String,Object>> sortUnfinished(List<Map<String,Object>> lst) {
		
		
		Collections.sort(lst, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return ((Date) o1.get("overTime")).compareTo((Date) o2.get("overTime"));
			}
		});
		
		return lst;
	}

}
