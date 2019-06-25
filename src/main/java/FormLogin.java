import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FormLogin {

    public static void main(String[] args) throws Exception {
        CloseableHttpClient client = HttpClients.custom().build();
        String code = "";
        String regex = "\\d{4}";
        while (!Pattern.matches(regex, code)){
            HttpGet get = new HttpGet("http://182.242.231.254:7001/GXGLPT/validate.jsp");

            HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                //得到实体
                HttpEntity entity = response.getEntity();

                byte[] data = EntityUtils.toByteArray(entity);

                //图片存入磁盘
                FileOutputStream fos = new FileOutputStream("src/main/resources/img/1.jpg");
                fos.write(data);
                fos.close();

                code = getCode().trim();
            }
        }

        System.out.println(code);

        HttpPost post = new HttpPost("http://182.242.231.254:7001/GXGLPT/LoginActionLogin.do");

        //6, List<BasicNameValuePair>
        List<BasicNameValuePair> parameters = new ArrayList<>();
        BasicNameValuePair p1 = new BasicNameValuePair("yhdm", "");
        parameters.add(p1);

        BasicNameValuePair p2 = new BasicNameValuePair("yhmm", "");
        parameters.add(p2);

        BasicNameValuePair p3 = new BasicNameValuePair("yhlx", "1");
        parameters.add(p3);

        BasicNameValuePair p4 = new BasicNameValuePair("yzm", code);
        parameters.add(p4);

        HttpEntity entity = new UrlEncodedFormEntity(parameters);

        post.setEntity(entity);

        HttpResponse response = client.execute(post);

        if(response.getStatusLine().getStatusCode() ==200) {
            //得到响应的实体
            HttpEntity responseEntity = response.getEntity();

            String str = EntityUtils.toString(responseEntity);

            System.out.println("响应的内容为 : " + str);
        }

        HttpGet personLoginGet = new HttpGet("http://182.242.231.254:7001/GXGLPT/PersonLogin.do?flag=1");
        HttpResponse responsePersonLogin = client.execute(personLoginGet);

        if (responsePersonLogin.getStatusLine().getStatusCode() == 200) {
            //得到实体
            HttpEntity responsePersonEntity = responsePersonLogin.getEntity();

            String str = EntityUtils.toString(responsePersonEntity);

            System.out.println("响应的内容为 : " + str);
        }

        HttpGet view1Get = new HttpGet("http://182.242.231.254:7001/GXGLPT/JXGL/CJGL/CJGL_GLY/CjglglyCjxhViewCs.do");
        HttpResponse view1Login = client.execute(view1Get);

        if (view1Login.getStatusLine().getStatusCode() == 200) {
            //得到实体
            HttpEntity view1Entity = view1Login.getEntity();

            String str = EntityUtils.toString(view1Entity);

            System.out.println("响应的内容为 : " + str);
        }

        HttpPost search1Post = new HttpPost("http://182.242.231.254:7001/GXGLPT/JXGL/CJGL/CJGL_GLY/CjglglyCjxhViewSearch.do");

        //6, List<BasicNameValuePair>
        List<BasicNameValuePair> searchParameters = new ArrayList<>();
        BasicNameValuePair searchP1 = new BasicNameValuePair("kkxnSearch", "2010-2011");
        searchParameters.add(searchP1);

        BasicNameValuePair searchP2 = new BasicNameValuePair("kkxqSearch", "1");
        searchParameters.add(searchP2);

        BasicNameValuePair searchP3 = new BasicNameValuePair("xhSearch", "");
        searchParameters.add(searchP3);

        BasicNameValuePair searchP4 = new BasicNameValuePair("xmSearch", "");
        searchParameters.add(searchP4);

        HttpEntity searchEntity = new UrlEncodedFormEntity(searchParameters);

        search1Post.setEntity(searchEntity);

        HttpResponse responseSearch = client.execute(search1Post);

        if(responseSearch.getStatusLine().getStatusCode() ==200) {
            //得到响应的实体
            HttpEntity responseEntity = responseSearch.getEntity();

            String str = EntityUtils.toString(responseEntity);

            System.out.println("响应的内容为 : " + str);
        }
    }

    public static String getCode(){
        File imageFile = new File("src/main/resources/img/1.jpg");
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
//         ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath("src/main/resources/tessdata"); // path to tessdata directory
        instance.setLanguage("eng");
        List<String> configs = new ArrayList<>();
        configs.add("digits");
        instance.setConfigs(configs);
        instance.setTessVariable("tessedit_char_whitelist", "0123456789");
        String result = "";
        try {
            Rectangle rect = new Rectangle(0, 0, 45, 18);
            result = instance.doOCR(imageFile, rect);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }


}
