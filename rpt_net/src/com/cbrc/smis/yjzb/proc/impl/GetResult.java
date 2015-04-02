package com.cbrc.smis.yjzb.proc.impl;

public class GetResult {
	/**
	 * 如果异常标准是包含两个表达式以上的就调用这个方法分别比较计算
	 * @param value      结果值
	 * @param colFormu   异常表准
	 * @return flag      状态
	 * @throws Exception
	 */
	public static int getCompare(double value, String colFormu)
			throws Exception {
	     //	boolean result = true;
	    	colFormu=colFormu.trim();
		if (colFormu==null)
			return -1;
		String[] arr = null; //第一次对公式解析的数组
		String title = null; //第一次解析完成之后得到的结果值
		String operator1 = null; //%符号的表现形式如有%就是100/没有%就是1/
		String arr1[] = null; //第二次对公式解析的数组
		String operator = null; //运算符号
		int flag = 0; //状态   //判断是不是超过标准
		String value1 = null; //如果值不带%时
		double values = 0; //最终比较的结果值
		
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split("<>");
			operator = "!=";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split(">=");
			operator = ">=";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split("<=");
			operator = "<=";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split(">");
			operator = ">";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split("<");
			operator = "<";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split("=");
			operator = "=";
		}

		if (operator.equals(">=")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}
			if (operator.equals(">=")) {
				if (operator1.equals("/100")) {
					if (value >= values / 100) {
						flag = 1;

					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value >= values / 1) {
						flag = 1;

					} else {
						flag = 0;
					}
				}
			}
		} else if (operator.equals("<=")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}
			if (operator.equals("<=")) {
				if (operator1.equals("/100")) {
					if (value <= values / 100) {
						flag = 1;
					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value <= values / 1) {
						flag = 1;
					} else {
						flag = 0;
					}
				}
			}
		} else if (operator.equals("!=")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}

			if (operator.equals("!=")) {
				if (operator1.equals("/100")) {
					if (value != values / 100) {
						flag = 1;
					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value != values / 1) {
						flag = 1;
					} else {
						flag = 0;
					}
				}

			}
		} else if (operator.equals("=")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}

			if (operator.equals("=")) {
				if (operator1.equals("/100")) {
					if (value == values / 100) {
						flag = 1;

					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value == values / 1) {
						flag = 1;

					} else {
						flag = 0;
					}
				}

			}

		} else if (operator.equals(">")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}
			if (operator.equals(">")) {
				if (operator1.equals("/100")) {
					if (value > values / 100) {
						flag = 1;
					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value > values / 1) {
						flag = 1;
					} else {
						flag = 0;
					}
				}

			}
		} else if (operator.equals("<")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}

			if (operator.equals("<")) {
				if (operator1.equals("/100")) {
					if (value < values / 100) {
						flag = 1;

					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value < values / 1) {
						flag = 1;

					} else {
						flag = 0;
					}
				}

			}
		}

		return flag;
	}	
}
