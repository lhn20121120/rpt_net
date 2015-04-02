package com.runqian.custom.function;

import com.runqian.report4.model.expression.Function;
import com.runqian.report4.usermodel.Context;

public class EXCEL_IF extends Function{
	public Object calculate(Context ctx, boolean isInput) //标准接口，ctx为运算环境，isInput为是否填报
	{
		return "0";
	}
}
