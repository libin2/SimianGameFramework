package com.simian.game.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
		//���л�
		baos = new ByteArrayOutputStream();
		oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		byte[] bytes = baos.toByteArray();
		return bytes;
		} catch (Exception e) {
		}
		return null;
		}
		 
		public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
		//�����л�
		bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
		} catch (Exception e) {
		 
		}
		return null;
		}
}
