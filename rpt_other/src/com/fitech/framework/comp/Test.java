package com.fitech.framework.comp;




public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = System.getProperty("file.separator");
		if(s.equals("\\"))
			System.out.println("win");
		else
			System.out.println("u");
	}

}
