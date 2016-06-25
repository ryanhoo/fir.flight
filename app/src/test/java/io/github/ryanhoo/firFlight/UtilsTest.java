package io.github.ryanhoo.firFlight;

import io.github.ryanhoo.firFlight.util.HtmlUtils;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 10:26 PM
 * Desc: UtilsTest
 */
public class UtilsTest {

    @Test
    public void testGetUrlsFromHtml() {
        final String html = "<p><a href=\\\"http://blog.fir.im/turbo-qiniu/\"><font color=\"#3AB2A7\" size=6px >&quot;一行命令 优化上传速度&quot;</font><font color=\"#33";
        List<String> urls = HtmlUtils.getUrlsFromHtml(html);
        for (String url : urls) {
            System.out.println("url: " + url);
        }
        Assert.assertEquals(urls.size(), 1);
    }
}
