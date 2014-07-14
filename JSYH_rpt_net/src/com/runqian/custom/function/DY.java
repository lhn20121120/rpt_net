package com.runqian.custom.function;
import com.runqian.base4.resources.EngineMessage;
import com.runqian.base4.resources.MessageManager;
import com.runqian.base4.util.ReportError;
import com.runqian.report4.model.expression.Expression;
import com.runqian.report4.model.expression.Function;
import com.runqian.report4.model.expression.Variant2;
import com.runqian.report4.usermodel.Context;
public class DY extends Function
{
	public Object calculate(Context ctx, boolean isInput) //标准接口，ctx为运算环境，isInput为是否填报
	{
	//得到获得的参数数据
		//判断自定义函数接收到得参数个数，如果个数为0，则抛出异常信息
		if ( this.paramList.size() < 1 ) {
		MessageManager mm = EngineMessage.get();
		throw new ReportError("paramater:" +mm.getMessage("function.missingParam"));
		}
		/*
		取得第一个参数，默认为表达式，需要把该表达式算出来，结果才是函数的参数值
		*/
		//取得第一个参数的表达式param1
		Expression param1=(Expression)this.paramList.get(0);
		//取得第二个参数的表达式param2
		if( this.paramList.size() == 2){
			Object result1 = Variant2.getValue(param1.calculate(ctx, isInput), false, isInput);
			//将计算出来的object类型的参数值转成String类型
			double res1=new Double(result1.toString());
			//取得第二个参数的表达式param1
			Expression param2=(Expression)this.paramList.get(1);
			//如果不为空，算出第二个参数的具体值，默认状态下返回类型为object
			Object result2 = Variant2.getValue(param2.calculate(ctx, isInput), false, isInput);
		    double res2=new Double(result2.toString());
		    Double number = res1-res2;
			if (number < 0){
				double number1 = Math.round(-1*number*100)/100.0;  
				return number1;
			}	
			else{
				return 0;
				}
		}else{
			Object result1 = Variant2.getValue(param1.calculate(ctx, isInput), false, isInput);
			//将计算出来的object类型的参数值转成String类型
			double res1=new Double(result1.toString());
			double res2 = 0;
			Double number = res1-res2;
			if (number < 0){
				double number1 = Math.round(-1*number*100)/100.0;  
				return number1;
			}	
			else{
				return 0;
				}
			
		}


	}
}