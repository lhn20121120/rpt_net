package com.cbrc.smis.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExecuteThread extends Thread {
	private String cmdString;

	public ExecuteThread(String cmd) {
		this.cmdString = cmd;
	}

	public void run() {
		String[] cmdArray = { "/bin/bash", "-c", cmdString };
		ProcessBuilder builder = new ProcessBuilder(cmdArray);
		try {
			System.out.println("asdfasdfadsfasdfasdfasdfasdf");
			Process process = builder.start(); // ��ȡ�������
			InputStream stderr = process.getErrorStream(); // ʹ��Reader���������ȡ�ʹ�ӡ
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			System.out.println("<ERROR>");
			while ((line = br.readLine()) != null)
				System.out.println(line); // ��ȡִ�з���ֵ
			int exitCode = process.waitFor();
			System.out.println("exitCode============" + exitCode);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
