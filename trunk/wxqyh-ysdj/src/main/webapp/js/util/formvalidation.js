var formvalidation={
	test : function(reg,str){
		return reg.test(str);
	},
	/**
     * <p>由数字、26个英文字母或者下划线组成的字符串</p>
     * @param str 传入的字符串
     * @return 如果是返回true，否则返回false
     */
	isDigitalEnglishUnderline : function(str){
		var reg = /^\w+$/;
		return this.test(reg,str);
	},
	/**
     * 
     * <p>由26个小写英文字母组成的字符串</p>
     * @param str 传入的字符串
     * @return 如果是小写英文字母组成的字符串返回true，否则返回false
     */
    isLowercaseEnglish : function(str){
        var reg = /^[a-z]+$/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>由26个大写英文字母组成的字符串</p>
     * @param str 传入的字符串
     * @return 如果是大写英文字母组成的字符串返回true，否则返回false
     */
    isUppercaseEnglish : function(str){
        var reg = /^[A-Z]+$/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>由26个英文字母组成的字符串</p>
     * @param str 传入的字符串
     * @return 如果是英文字母组成的字符串返回true，否则返回false
     */
    isPureEnglish : function(str){
        var reg = /^[A-Za-z]+$/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>由数字和26个英文字母组成的字符串</p>
     * @param str 传入的字符串
     * @return 如果是英文和(或)者数字返回true，否则返回false
     */
    isEnglishOrDigital : function(str){
        var reg = /^[A-Za-z0-9]+$/;
		return this.test(reg,str);
    },
     /**
     * 
     * <p>是否是数字</p>
     * @param str 传入的字符串
     * @return 如果是数字返回true，否则返回false
     */
    isDigital : function(str){
        var reg = /^[0-9]*$/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>是否是邮政编码（中国）</p>
     * @param str 传入的字符串
     * @return 如果是邮政编码返回true，否则返回false
     */
     isZipCode : function(str){
        var reg = /[1-9]\d{5}/;
		return this.test(reg,str);
    },
     /**
     * 
     * <p>是否是金额</p>
     * @param str 传入的字符串
     * @return 如果是金额返回true，否则返回false
     */
    isMoney : function(str){
        var reg = /^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$/;
		return this.test(reg,str);
    },
     /**
     * 
     * <p>是否是电话号码</p>
     * @param str 传入的字符串
     * @return 如果是电话号码返回true，否则返回false
     */
    isThePhone : function(str){
        var reg = /^(\d{3,4}-)?\d{7,8}$/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>是否是http url 链接</p>
     * @param str 传入的字符串
     * @return 如果是http url 链接返回true，否则返回false
     */
    isHttpUrl : function(str){
        var reg = /^http(s)?:\/\/([\w-]+\.)+[\w-]+(:\d{1,6})?(\/[\w-.\/?%&=]*)?$/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>是否是域名</p>
     * @param str 传入的字符串
     * @return 如果是域名返回true，否则返回false
     */
    isDomain : function(str){
        var reg = /^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>是否是中文字符</p>
     * @param str 传入的字符串
     * @return 如果是中文字符，返回true，否则返回false
     */
    isChinese : function(str){
        var reg = /[\u4e00-\u9fa5]+/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>是否是ip地址（IP4）</p>
     * @param ip ip地址
     * @return 如果是ip地址，返回true，否则返回false
     */
    isIp : function(str){
        var reg = /\b((?!\d\d\d)\d+|1\d\d|2[0-4]\d|25[0-5])\.((?!\d\d\d)\d+|1\d\d|2[0-4]\d|25[0-5])\.((?!\d\d\d)\d+|1\d\d|2[0-4]\d|25[0-5])\.((?!\d\d\d)\d+|1\d\d|2[0-4]\d|25[0-5])\b/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>是否是身份证号</br>
     * 正则只能验证身份证的格式是否正确，是验证不了真伪的，只有公安系统里才能验证身份证号真假
     * </p>
     * @param idNumber 身份证号码
     * @return 如果是身份证号码，返回true，否则返回false
     */
    isIdCard : function(str){
        var reg = /^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>是否是邮箱</p>
     * @param email 邮箱字符串
     * @return 如果是邮箱，返回true，否则返回false
     */
    isEmail : function(str){
        var reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
		return this.test(reg,str);
    },
    /**
     * 
     * <p>是否是手机号码</p>
     * @param mobile 手机号码
     * @return 如果是手机号码，返回true，否则返回false
     */
    isMobile : function(str){
        var reg = /^1\d{10}$/;
		return this.test(reg,str);
    }
}

//alert(formvalidation.isIp("127.0.0.1"));