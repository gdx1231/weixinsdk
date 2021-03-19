package com.gdxsoft.weixin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.gdxsoft.easyweb.utils.UFormat;
import com.gdxsoft.easyweb.utils.UNet;

public class Ssl {
	/**
	 * 腾讯双向加密通讯<br>
	 * 
	 * 微信支付接口中，涉及资金回滚的接口会使用到商户证书，包括退款、撤销接口。商家在申请微信支付成功后，收到的相应邮件后，按照指引下载API证书。
	 * 证书文件有四个，分别说明如下：<br>
	 * 
	 * 
	 * apiclient_cert.p12
	 * 包含了私钥信息的证书文件，为p12(pfx)格式，由微信支付签发给您用来标识和界定您的身份，请妥善保管不要泄漏和被他人复制
	 * 部分安全性要求较高的API需要使用该证书来确认您的调用身份
	 * windows上可以直接双击导入系统，导入过程中会提示输入证书密码，证书密码默认为您的商户ID（如：10010000）
	 * 
	 * @throws Exception
	 */
	public static String pcks12(SSLContext sslcontext, String u, String msg) throws Exception {
		log("POST-pcks12: " + u);
		log("POST-pcks12: " + msg);
		// KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// FileInputStream instream = new FileInputStream(
		// new File(
		// "/Users/admin/java/workspace/pf/reques/ccacamps/apiclient_cert.p12"));
		// try {
		// keyStore.load(instream, "1226722702".toCharArray());
		// } finally {
		// instream.close();
		// }

		// Trust own CA and all self-signed certs
		// SSLContext sslcontext = SSLContexts.custom()
		// .loadKeyMaterial(keyStore, "1226722702".toCharArray()).build();

		// Allow TLSv1 protocol only
		
		// 过时的用法
		// SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
		// new String[] { "TLSv1" }, null,
		// SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());

		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		String code = "UTF-8";
		StringEntity postEntity = new StringEntity(msg, code);
		HttpPost httpost = new HttpPost(u);
		httpost.addHeader("Content-Type", "text/xml");
		httpost.setEntity(postEntity);
		try {

			// EntityBuilder ent = org.apache.http.client.entity.EntityBuilder
			// .create();
			// ent.setContentEncoding(code);
			// ent.setBinary(msg.getBytes());
			// httpost.setEntity(ent.build());
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();
				// System.out.println(response.getStatusLine());
				if (entity != null) {
					// String rst = EntityUtils.toString(entity, code);
					// EntityUtils.consume(entity);
					// log(rst);
					StringBuilder rst = new StringBuilder();
					;
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent(), code));
					String text;
					while ((text = bufferedReader.readLine()) != null) {
						if (rst.length() > 0) {
							rst.append("\n");
						}
						rst.append(text);
					}

					log(rst.toString());
					return rst.toString();
				} else {
					return null;
				}

			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	public static String get(String u) {
		UNet net = new UNet();
		String cnt = net.doGet(u);
		log(cnt);
		return cnt;
	}

	/**
	 * 提交二进制到服务器
	 * 
	 * @param u
	 * @param postName
	 * @param buf
	 * @return
	 */
	public static String postBuff(String u, String postName, byte[] buf) {
		log("POST-BUFF: " + u);

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpost = new HttpPost(u);
		MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
		mEntityBuilder.addBinaryBody(postName, buf);
		httpost.setEntity(mEntityBuilder.build());
		String code = "UTF-8";
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpost);
			// get http status code
			int resStatu = response.getStatusLine().getStatusCode();

			if (resStatu == org.apache.http.HttpStatus.SC_OK) {
				// get result data
				HttpEntity entity = response.getEntity();
				String reStr = EntityUtils.toString(entity, code);
				EntityUtils.consume(entity);

				return reStr;
			} else {

				log(u + ": resStatu is " + resStatu);
				return null;
			}
		} catch (ClientProtocolException e) {
			log(e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			log(e.getLocalizedMessage());
			return null;
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				log(e.getLocalizedMessage());
			}
		}

	}

	/**
	 * 上传文件
	 * 
	 * @param u
	 * @param name
	 * @param filePath
	 * @param params   附加参数
	 * @return
	 */
	public static String uploadFile(String u, String name, String filePath, HashMap<String, String> params) {
		UNet net = new UNet();
		String result = net.doUpload(u, name, filePath, params);
		return result;

	}

	/**
	 * 上传文件
	 * 
	 * @param u
	 * @param name
	 * @param filePath
	 * @return
	 */
	public static String uploadFile(String u, String name, String filePath) {

		return uploadFile(u, name, filePath, null);

	}

	/**
	 * 下载媒体文件
	 * 
	 * @param u          地址
	 * @param jsonString
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static byte[] download(String u, String jsonString) throws IOException {
		log("DW: " + u);
		log("DW: " + jsonString);

		UNet net = new UNet();
		return net.postMsgAndDownload(u, jsonString);

	}

	/**
	 * 下载媒体文件
	 * 
	 * @param u          地址
	 * @param jsonString
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static byte[] download(String u) {
		UNet net = new UNet();
		return net.downloadData(u);
	}

	public static String postMsg(String u, String msg) throws IOException {
		UNet net = new UNet();
		return net.postMsg(u, msg);

	}

	public static String post(String u, HashMap<String, String> params) throws IOException {
		UNet net = new UNet();
		return net.doPost(u, params);
	}

	public static void log(String msg) {
		Date date = new Date();
		String s0 = "";
		try {
			s0 = UFormat.formatDate("TIME", date, "zhcn");
		} catch (Exception e) {

		}
		System.out.println(s0 + ":" + msg);
	}
}
