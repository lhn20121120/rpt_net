package com.cbrc.smis.yjzb.proc.impl;

public class GetResult {
	/**
	 * ����쳣��׼�ǰ����������ʽ���ϵľ͵�����������ֱ�Ƚϼ���
	 * @param value      ���ֵ
	 * @param colFormu   �쳣��׼
	 * @return flag      ״̬
	 * @throws Exception
	 */
	public static int getCompare(double value, String colFormu)
			throws Exception {
	     //	boolean result = true;
	    	colFormu=colFormu.trim();
		if (colFormu==null)
			return -1;
		String[] arr = null; //��һ�ζԹ�ʽ����������
		String title = null; //��һ�ν������֮��õ��Ľ��ֵ
		String operator1 = null; //%���ŵı�����ʽ����%����100/û��%����1/
		String arr1[] = null; //�ڶ��ζԹ�ʽ����������
		String operator = null; //�������
		int flag = 0; //״̬   //�ж��ǲ��ǳ�����׼
		String value1 = null; //���ֵ����%ʱ
		double values = 0; //���ձȽϵĽ��ֵ
		
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
