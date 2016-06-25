package io.github.ryanhoo.firFlight.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 10:08 PM
 * Desc: HtmlUtils
 */
public class HtmlUtils {

    public static final String REGEX_URL = "http(s)?://(.[^\"><])*";

    public static List<String> getUrlsFromHtml(String htmlText) {
        List<String> urls = new ArrayList<>();
        Pattern p = Pattern.compile(REGEX_URL);
        Matcher m = p.matcher(htmlText);
        while (m.find()) {
            String urlStr = m.group();
            urls.add(urlStr);
        }
        return urls;
    }
}
