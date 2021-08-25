package Notorious.utils;

import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.lang3.time.DurationFormatUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class Wrapper {

	public static void sendPacket(Packet packet) {
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
	}

	public static String getHWID() {
		try {
			String toEncrypt = System.getenv("COMPUTERNAME") + System.getProperty("user.name")
					+ System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(toEncrypt.getBytes());
			StringBuffer hexString = new StringBuffer();

			byte byteData[] = md.digest();

			for (byte aByteData : byteData) {
				String hex = Integer.toHexString(0xff & aByteData);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}

	public static String generateRandomStringWithPrefix() {
		String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder stringBuilder = new StringBuilder();
		Random random = new Random();
		while (true) {
			if (stringBuilder.length() >= 5) {
				String string2 = "Notorious_" + String.valueOf(stringBuilder);
				return string2;
			}
			int n = (int) (random.nextFloat()
					* (float) "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".length());
			stringBuilder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".charAt(n));
		}
	}

	public static String formatMillis(int millis) {
		String string;
		string = DurationFormatUtils.formatDuration(millis, "H:mm:ss", true);
		String[] parts = string.split(":");
		if (!parts[0].equals("0") && !parts[0].equals("00")) {
			return "Hours: " + parts[0] + " Minutes: " + parts[1] + " Seconds: " + parts[2];
		} else if (!parts[1].equals("0") && !parts[1].equals("00")) {
			return "Minutes: " + parts[1] + " Seconds: " + parts[2];
		} else {
			return "Seconds: " + parts[2];
		}
	}

}
