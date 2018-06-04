package mycatDemo;

import java.io.UnsupportedEncodingException;

public class Demo {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		byte[] bytes=new byte[] {28, 0, 0, 2, 3, 100, 101, 102, 0, 0, 0, 6, 117, 115, 101, 114, 40, 41, 0, 12, 5, 0, 93, 0, 0, 0, -3, 0, 0, 31, 0, 0};
		System.out.println(new String(bytes,5,bytes.length-5,"utf-8"));
	}
}
