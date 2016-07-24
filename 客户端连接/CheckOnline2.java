package test.liuxin.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author liuxin
 *
 */
public class CheckOnline2 {
	static Future future = null;
	static ExecutorService es = null;
	static Callable<String> callable = null;

	public static void main(String[] args) {
		es = Executors.newFixedThreadPool(1);
		callable = new Callable<String>() {			// ʹ��Callable�ӿ���Ϊ�������
			@Override
			public String call() throws Exception {// ����������������ִ�У�����ķ���ֵ����ΪString������Ϊ��������
				URL url = new URL("http://baidu.com");
				InputStream in = url.openStream();
				in.close();
				return "0";
			}
		};
		netListen();
			
	}
	public static void reConnect(){
		Thread thread = new Thread();
		Process p = null;
		System.out.println("���ڿ�ʼ����...");
		
		while(!checkOnline())
		{
			try {
				Runtime.getRuntime().exec("taskkill /F /IM srun3000.exe");
				thread.sleep(500);
				Runtime rn = Runtime.getRuntime();
				try {
					System.out.println("�������������ͻ���");
					p = rn.exec("\"srun3000.exe\"");
				} catch (Exception e) {
					System.out.println("Error exec!");
				}
				thread.sleep(5000);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
		netListen();
	}
	public static void netListen()
	{
		Thread thread = new Thread();
		if(checkOnline())
			System.out.println("��������������������...");
		try {
			thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(checkOnline())
		{
			try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("����Ͽ�");
		reConnect();
	}
	public static boolean checkOnline() {
		try {
			future = es.submit(callable);
			String value = null;
			try {
				future.get(1000, TimeUnit.MILLISECONDS).toString();// ȡ�ý����ͬʱ���ó�ʱִ��ʱ��Ϊ1�롣
				return true;
			} catch (ExecutionException | TimeoutException e) {
				return false;
			}
		} catch (InterruptedException e) {
			return false;
		}
	}
}
