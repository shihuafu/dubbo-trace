package com.fushihua.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @ClassName: NetWorkUtils
 * @Description: 网络相关工具类
 * @author fushihua
 * @date 2016年11月19日 下午2:07:13
 */
public class NetWorkUtils {
	
	/**
	 * 获取本地IP地址
	 * @throws SocketException
	 */
	public static String getLocalIP() {
		if (isWindowsOS()) {
			return getWindowsIP();
		} else {
			return getLinuxLocalIp();
		}
	}

	/**
	 * 判断操作系统是否是Windows
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	/**
	 * 获取本地Host名称
	 */
	public static String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取WindowsIP
	 */
	public static String getWindowsIP() {
		String serverIP = "";
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		byte[] ipAddr = addr.getAddress();
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				serverIP += ".";
			}
			serverIP += ipAddr[i] & 0xFF;
		}
		return serverIP;
	}

	/**
	 * 获取LinuxIp
	 */
	private static String getLinuxLocalIp() {
		String ip = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				String name = intf.getName();
				if (!name.contains("docker") && !name.contains("lo")) {
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							String ipaddress = inetAddress.getHostAddress().toString();
							if (!ipaddress.contains("::") && !ipaddress.contains("0:0:")
									&& !ipaddress.contains("fe80")) {
								ip = ipaddress;
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			System.out.println("获取ip地址异常");
			ip = "127.0.0.1";
			ex.printStackTrace();
		}

		return ip;
	}

	public static void main(String[] args) {
		System.out.println(NetWorkUtils.getLocalIP());
	}
}

