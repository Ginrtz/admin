package com.gin.admin.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 发起HTTP请求工具类
 *
 * @author o1760
 *
 */
public class HttpUtil {
	/**
	 * 发起GET请求
	 *
	 * @param url 请求地址
	 * @return 响应报文
	 */
	public static String doGet(String url) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String result = "";
		try {
			// 通过址默认配置创建一个httpClient实例
			httpClient = HttpClients.createDefault();
			// 创建httpGet远程连接实例
			HttpGet httpGet = new HttpGet(url);
			// 设置配置请求参数
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
					.setConnectionRequestTimeout(35000)// 请求超时时间
					.setSocketTimeout(60000)// 数据读取超时时间
					.build();
			// 为httpGet实例设置配置
			httpGet.setConfig(requestConfig);
			// 执行get请求得到返回对象
			response = httpClient.execute(httpGet);
			// 通过返回对象获取返回数据
			HttpEntity entity = response.getEntity();
			// 通过EntityUtils中的toString方法将结果转换为字符串
			result = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 发起POST请求
	 *
	 * @param url      请求地址
	 * @param paramMap 请求参数
	 * @return 响应报文
	 */
	public static String doPost(String url, Map<String, Object> paramMap) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		String result = "";
		// 创建httpClient实例
		httpClient = HttpClients.createDefault();
		// 创建httpPost远程连接实例
		HttpPost httpPost = new HttpPost(url);
		// 配置请求参数实例
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
				.setConnectionRequestTimeout(35000)// 设置连接请求超时时间
				.setSocketTimeout(60000)// 设置读取数据连接超时时间
				.build();
		// 为httpPost实例设置配置
		httpPost.setConfig(requestConfig);
		// 设置请求头
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		// 封装post请求参数
		if (null != paramMap && paramMap.size() > 0) {
			List<NameValuePair> nvps = new ArrayList<>();
			// 通过map集成entrySet方法获取entity
			Set<Entry<String, Object>> entrySet = paramMap.entrySet();
			// 循环遍历，获取迭代器
			Iterator<Entry<String, Object>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> mapEntry = iterator.next();
				nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
			}
			// 为httpPost设置封装好的请求参数
			try {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
				formEntity.setContentType("application/x-www-form-urlencoded");
				httpPost.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		try {
			// httpClient对象执行post请求,并返回响应参数对象
			httpResponse = httpClient.execute(httpPost);
			// 从响应对象中获取响应内容
			HttpEntity entity = httpResponse.getEntity();
			result = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != httpResponse) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 判断请求是否是ajax请求
	 *
	 * @param request HttpServletRequest对象
	 * @return 是否是ajax请求，true or false
	 */
	public static boolean isAjax(HttpServletRequest request) {
		return request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
	}
}
