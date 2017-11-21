package cn.com.do1.component.filehandle.filehandle;


/**
 * Description :
 * Created by lwj on 2017-8-17.
 */
public class Test {
	public static String subStr(String str, String str1){
		if (str.contains(str1 + "、")) {
			str = str.replace(str1 + "、", "");
		} else if (str.contains("、" + str1)) {
			str = str.replace("、" + str1, "");
		}
		return str;
	}

	public static void main(String[] args) {
		/*String str = "23456|";
		System.out.println(str.endsWith("|"));
		System.out.println(str.substring(0, str.length() - 1));
		*/
/*
		String str1 = "测试测试张三、李四、王五、赵六测试测试";
		String str2 = "张三";
		String str3 = "赵六";
		String str4 = "王五";
		String str5 = "李四";
		String temp = subStr(str1, str4);
		temp = subStr(temp, str3);
		temp = subStr(temp, str2);
		temp = subStr(temp, str5);
		System.out.println(temp);*/
		/*if (str1.contains(str2 + "、")) {
			str1 = str1.replace(str2 + "、", "");
		} else if (str1.contains("、" + str2)) {
			str1 = str1.replace("、" + str2, "");
		}
		System.out.println(str1);
		if (str1.contains(str3 + "、")) {
			str1 = str1.replace(str3 + "、", "");
		} else if (str1.contains("、" + str3)) {
			str1 = str1.replace("、" + str3, "");
		}
		System.out.println(str1);
		if (str1.contains(str4 + "、")) {
			str1 = str1.replace(str4 + "、", "");
		} else if (str1.contains("、" + str4)) {
			str1 = str1.replace("、" + str4, "");
		}
		System.out.println(str1);*/

		StringBuilder sb = new StringBuilder(" or 1234");
		sb.delete(0, 4);
		sb.insert(0, " and (");
		sb.append(")");
		System.out.println(sb);
	}
}
