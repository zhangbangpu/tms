package com.chinaway.tms.utils.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.chinaway.tms.utils.json.JsonUtil;
 
public class HttpClientUtils {
 
    private static PoolingHttpClientConnectionManager connectionManager = null;
    private static HttpClientBuilder httpBulder = null;
    private static RequestConfig requestConfig = null;
 
    private static int MAXCONNECTION = 10;
 
    private static int DEFAULTMAXCONNECTION = 5;
 
//    private static String IP;
//    private static int PORT;
 
    static {
        //设置http的状态参数
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(1000*60*60)//时间单位：毫秒
                .setConnectTimeout(1000*60*60)
                .setConnectionRequestTimeout(1000*60*60)
                .build();
 
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAXCONNECTION);
        connectionManager.setDefaultMaxPerRoute(DEFAULTMAXCONNECTION);
        httpBulder = HttpClients.custom();
        httpBulder.setConnectionManager(connectionManager);
    }
 
    public static CloseableHttpClient getConnection(String ip, int port) {
        HttpHost target = new HttpHost(ip, port);
        connectionManager.setMaxPerRoute(new HttpRoute(target), 20);
        CloseableHttpClient httpClient = httpBulder.build();
        httpClient = httpBulder.build();
        return httpClient;
    }
 
    /**
     * 
     * @param map
     * @param url
     * @param method
     * @return
     */
    public static HttpUriRequest getRequestMethod(Map<String, String> map, String url, String method) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            String name = e.getKey();
            String value = e.getValue();
            NameValuePair pair = new BasicNameValuePair(name, value);
            params.add(pair);
        }
        HttpUriRequest reqMethod = null;
        if ("post".equals(method)) {
            reqMethod = RequestBuilder.post().setUri(url)
                    .addParameters(params.toArray(new BasicNameValuePair[params.size()]))
                    .setConfig(requestConfig).build();
            //reqMethod.setHeader("Content-Length", "10240");
        } else if ("get".equals(method)) {
            reqMethod = RequestBuilder.get().setUri(url)
                    .addParameters(params.toArray(new BasicNameValuePair[params.size()]))
                    .setConfig(requestConfig).build();
        }
        return reqMethod;
    }
 
    /**
     * 
     * @param map 参数
     * @param url 方法名
     * @param method 请求方式
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static HttpResponse getHttpResponse(Map<String, String> map, String urlRoot, String url, String method) throws ClientProtocolException, IOException {
//    	ReadProperties readProperties=new ReadProperties();
//		String urlRoot = readProperties.readProperties("payms.url");
		String url2 = urlRoot;
		if(urlRoot.startsWith("http://")){
			//substring(start,end)索引从0开始，[start,end),end没有默认是str.length()
			url2 = urlRoot.substring(7);
		}else if(urlRoot.startsWith("https://")){
			url2 = urlRoot.substring(8);
		}
		String ip = url2.substring(0, url2.indexOf(':'));
		String port = url2.substring(url2.indexOf(':')+1,url2.indexOf('/'));

        HttpClient client = getConnection(ip,Integer.parseInt(port));
        String urlPath = urlRoot + url;
        HttpUriRequest post = getRequestMethod(map, urlPath, method);
        HttpResponse response = client.execute(post);
        
        return response;
    }
    
    //测试
    public static void main(String args[]) throws Exception {
        //post请求的参数
    	Map<String, String> map = new HashMap<String, String>();
    	String param ="{\"address\":\"四川省成都市双流区天府四街软件园\",\"area\":\"0\",\"city\":\"成都\",\"code\":\"028\",\"createtime\":\"2016-08-02 17:28:21\",\"deptname\":\"1919酒类直供\",\"id\":0,\"isprivate\":\"私有\",\"name\":\"天府四街仓库\",\"province\":\"四川省\",\"types\":\"标点\",\"updatetime\":\"2016-08-02 17:28:21\"}";
        map.put("siteInfo", param);

        HttpResponse response = HttpClientUtils.getHttpResponse(map, "http://172.22.120.28:80/tms/","/ws/addSite", "post");
//        String ip = "10.0.13.190";
//        HttpClient client = getConnection(ip,80);
//        String urlPath = "http://10.0.13.190:80/payms/cxfws/addCompany";
//        HttpUriRequest post = getRequestMethod(map, urlPath, "post");
//        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            String message = EntityUtils.toString(entity, "utf-8");
            System.out.println(message);
            
            Map<String, Object> resultMap = JsonUtil.jsonStr2Map(message);
            boolean status = (boolean) resultMap.get("status");
            String msg = (String) resultMap.get("msg");
            if (!status) {
				throw new Exception(msg);
			}
            System.out.println(status);
        } else {
            System.out.println("请求失败");
        }
        
    }
}
