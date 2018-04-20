package com.calabar.commons.utils.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExcuteThread extends Thread {
	private String command;

	public ExcuteThread(String command) {
		this.command = command;
	}
	@Override
	public void run() {
		try {
			Process p = Runtime.getRuntime().exec(command);
			InputStream fis = p.getInputStream();
			final BufferedReader brError = new BufferedReader(
					new InputStreamReader(p.getErrorStream(), "utf-8"));
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			final BufferedReader br = new BufferedReader(isr);
			Thread t1 = new Thread() {
				public void run() {
					String line = null;
					try {
						while ((line = brError.readLine()) != null) {
							// System.out.println(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (brError != null)
								brError.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			Thread t2 = new Thread() {
				public void run() {
					String line = null;
					try {
						while ((line = br.readLine()) != null) {
							// System.out.println(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (br != null)
								br.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			t1.start();
			t2.start();

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
		}

	}

}

