import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Parser
{
  public static void main(String[] args)
  {
    URL url;
    InputStream is = null;
    String html = "";
    String[] results = new String[10];
    Integer i;
    String text = "";
    for (i=0;i<args.length;i++)
        text += (i==args.length-1)?args[i]:args[i]+"+";
    try {
        url = new URL("http://yandex.ru/yandsearch?text="+text);
        is = url.openStream(); // throws an IOException
        Scanner s = new Scanner(is).useDelimiter("\\A");
        html = s.next();
        Pattern document = Pattern.compile("<a class=\"b-serp-item__title-link\" href=\"([^\"]*).*?<span>(.*?)</span>.*?<div class=\"b-serp-item__text\">(.*?)</div>.*?<span class=\"b-serp-url b-serp-url_inline_yes\">(.*?)(?:</span><span class=\"b-serp-item__hover\">|</div>)");
        Matcher matcher = document.matcher(html);
        for (i=0;i<10;i++) {
            matcher.find();
            System.out.println("Document "+(i+1)+":");
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2).replaceAll("<.*?>",""));
            System.out.println(matcher.group(3).replaceAll("<.*?>",""));
            System.out.println(matcher.group(4).replaceAll("<.*?>",""));
            System.out.println();
            results[i] = matcher.group(1);
        }
    } catch (java.net.MalformedURLException mue) {
        mue.printStackTrace();
    } catch (java.io.IOException ioe) {
        ioe.printStackTrace();
    } finally {
        try {
            is.close();
            OutputStream outStream = null;
            File resultsDir = new File("results");
            if (!resultsDir.exists())
                resultsDir.mkdir(); // create results folder if it does not exist
            int ByteRead;
            for (i=0;i<10;i++)
            try {
                    byte[] buf;
                    url = new URL(results[i]);
                    outStream = new BufferedOutputStream(new FileOutputStream("results\\"+(i+1)+".html"));
                    is = url.openStream();
                    buf = new byte[1024];
                    while ((ByteRead = is.read(buf)) != -1) {
                        outStream.write(buf, 0, ByteRead);
                    }
            } catch (java.net.MalformedURLException mue) {
                mue.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    outStream.close();
                } catch (java.io.IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }
  }
}