package com.fitech.framework.core.util;

import java.io.IOException;

public class BacketChecker {

	public static boolean check(String str) {
		StackX sx = new StackX();
		boolean flag = true;
		int k = str.length();
		for (int i = 0; i < k && flag; i++) {
			char c = str.charAt(i);
			if (c == '(' || c == '[' || c == '{')
				sx.push(c);
			else if (c == ')' || c == ']' || c == '}') {
				char x = sx.pop();
				switch (c) {
				case ')':
					if (x != '(')
						flag = false;
					break;
				case ']':
					if (x != '[')
						flag = false;
					break;
				case '}':
					if (x != '{')
						flag = false;
					break;
				default:
					break;
				}
			} else {
				;
			}
		}
		if (!sx.isEmpty())
			flag = false;
		return flag;
	}

	public static void main(String[] args) throws IOException {
		String str = "\\(asf{sasd}f\\)";
		System.out.println(BacketChecker.check(str));
	}

}
