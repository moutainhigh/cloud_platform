package cn.sensordb2.stcloud.util;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ShortMessageTool {
	private static Logger logger = Logger.getLogger(ShortMessageTool.class);
	private static String sign = IniUtil.getInstance().getSmsSign();
	private static String USER_NAME= IniUtil.getInstance().getSmsUserName();
	private static String PWD = IniUtil.getInstance().getSmsPwd();
	private static String MSGContentPrefix = IniUtil.getInstance().getSmsMSGContentPrefix();
	private static String SMSHOST = IniUtil.getInstance().getSmsHost();
	private static String URL = IniUtil.getInstance().getSmsURL();
	private static String HAS_NEW_LOCK_KEY_TEMPLATE = "【虹云网络】用户“{uid}”分享给您一把门锁钥匙，请登录“虹云智慧生活”APP领取钥匙。";
	
	public static boolean sendVerificationCode(String verificationCode, String phoneNumber) {
		StringBuffer sb = new StringBuffer("HTTP://"+SMSHOST+URL);

		// ��StringBuffer׷���û���
		sb.append("name=");
		sb.append(USER_NAME);

		sb.append("&pwd=");
		sb.append(PWD);

		sb.append("&mobile=" + phoneNumber);

		try {
			// ��StringBuffer׷����Ϣ����תURL��׼��
			sb.append("&content=" + URLEncoder.encode(MSGContentPrefix+verificationCode, "UTF-8"));

			//׷�ӷ���ʱ�䣬��Ϊ�գ�Ϊ��Ϊ��ʱ����
			sb.append("&stime=");

			//��ǩ��
			sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
			
			//typeΪ�̶�ֵpt  extnoΪ��չ�룬����Ϊ���� ��Ϊ��
			sb.append("&type=pt&extno=");

			URL url = new URL(sb.toString());

			// ��url����
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// ����url����ʽ ��get�� ���� ��post��
			connection.setRequestMethod("POST");

			// ����
			InputStream is = url.openStream();


			//ת������ֵ
			String returnStr = convertStreamToString(is);
			
			// ���ؽ��Ϊ��0��20140009090990,1���ύ�ɹ��� ���ͳɹ�   �����˵���ĵ�
			System.out.println(returnStr);
			logger.debug("send:"+sb.toString()+" receive:"+returnStr);
			if(returnStr.startsWith("0")) return true;
			else return false;
		} catch (IOException e) {
			logger.error(Tools.getTrace(e));
			return false;
		}
	}
	
	public static String generateGetVerificationCodeUrl(String verificationCode, String phoneNumber) {
		// ����StringBuffer�������������ַ���
		StringBuffer sb = new StringBuffer(URL);

		// ��StringBuffer׷���û���
		sb.append("name=");
		sb.append(USER_NAME);

		sb.append("&pwd=");
		sb.append(PWD);

		sb.append("&mobile=" + phoneNumber);

		try {
			// ��StringBuffer׷����Ϣ����תURL��׼��
			sb.append("&content=" + URLEncoder.encode(MSGContentPrefix+verificationCode, "UTF-8"));

			//׷�ӷ���ʱ�䣬��Ϊ�գ�Ϊ��Ϊ��ʱ����
			sb.append("&stime=");

			//��ǩ��
			sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(Tools.getTrace(e));
			return null;
		}
		
		//typeΪ�̶�ֵpt  extnoΪ��չ�룬����Ϊ���� ��Ϊ��
		sb.append("&type=pt&extno=");
		
		return sb.toString();
	}


	public static String generateNewKeyMsgUrl(String distKeyUserName, String nickName, String phoneNumber) {
		// ����StringBuffer�������������ַ���
		StringBuffer sb = new StringBuffer(URL);

		// ��StringBuffer׷���û���
		sb.append("name=");
		sb.append(USER_NAME);

		sb.append("&pwd=");
		sb.append(PWD);

		sb.append("&mobile=" + phoneNumber);

		String uid;
		if (nickName != null) {
			uid = String.format("%s(%s)", distKeyUserName, nickName);
		}
		else {
			uid = distKeyUserName;
		}
		String msgContent = HAS_NEW_LOCK_KEY_TEMPLATE.replace("{uid}", uid);

		try {
			// ��StringBuffer׷����Ϣ����תURL��׼��

			sb.append("&content=" + URLEncoder.encode(msgContent, "UTF-8"));

			//׷�ӷ���ʱ�䣬��Ϊ�գ�Ϊ��Ϊ��ʱ����
			sb.append("&stime=");

			//��ǩ��
			//sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(Tools.getTrace(e));
			return null;
		}

		//typeΪ�̶�ֵpt  extnoΪ��չ�룬����Ϊ���� ��Ϊ��
		sb.append("&type=pt&extno=");

		return sb.toString();
	}


	public static void getVerificationCode(Vertx vertx, String verificationCode, String phoneNumber,
			Handler<Boolean> handler) {
		String url = ShortMessageTool.generateGetVerificationCodeUrl(verificationCode, phoneNumber);		
		HttpClient client = vertx.createHttpClient();

		// Specify both port and host name
		client.getNow(80, SMSHOST, url, response -> {
			  response.bodyHandler(totalBuffer -> {
					try {
						if(totalBuffer.toString().startsWith("0")) 
							handler.handle(new Boolean(true));
						else handler.handle(new Boolean(false));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(Tools.getTrace(e));
						handler.handle(new Boolean(false));
					}
			  });
		});
	}


	public static void sendNewKeyMsg(Vertx vertx, String distKeyUserName, String nickName, String phoneNumber,
										   Handler<Boolean> handler) {
		String url = ShortMessageTool.generateNewKeyMsgUrl(distKeyUserName, nickName, phoneNumber);
		HttpClient client = vertx.createHttpClient();

		// Specify both port and host name
		client.getNow(80, SMSHOST, url, response -> {
			response.bodyHandler(totalBuffer -> {
				try {
					if(totalBuffer.toString().startsWith("0"))
						handler.handle(new Boolean(true));
					else handler.handle(new Boolean(false));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(Tools.getTrace(e));
					handler.handle(new Boolean(false));
				}
			});
		});
	}


	/**
	 * ת������ֵ����ΪUTF-8��ʽ.
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		StringBuilder sb1 = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;

		try {
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, "UTF-8");
				sb1.append(str);
			}
		} catch (IOException e) {
			logger.error(Tools.getTrace(e));
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.error(Tools.getTrace(e));
			}
		}
		return sb1.toString();
	}
	
	public static void main(String[] args) {
		try {
			//System.out.println(ShortMessageTool.sendVerificationCode("40000", "18020136525"));
//			testSendVerificationCode();
			testSendNewKeyMsg();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testSendVerificationCode() {
		try {
			Vertx vertx = Vertx.vertx();
			ShortMessageTool.getVerificationCode(vertx, "60000", "15088649651", result -> {
				System.out.println(result);
				vertx.close();
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testSendNewKeyMsg() {
		try {
			Vertx vertx = Vertx.vertx();
			ShortMessageTool.sendNewKeyMsg(vertx, "user1", "user1_nickName", "13645160637", result -> {
				System.out.println(result);
				vertx.close();
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
