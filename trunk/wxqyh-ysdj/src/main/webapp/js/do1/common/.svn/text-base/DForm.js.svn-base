/**
 * @fileoverview DForm.js : 表单自动通用验证功能
 * @version 0.1
 * @author 曹春城 <br />
 * CopyRight 广州市道一信息技术有限公司 2008
 */


/**
 * 表单自动通用验证处理工具类。
 * @class JavaScript表单自动通用验证实现类。<br />
 * @constructor 构造函数
 * @throws MemoryException 如果没有足够的内存
 * @return 返回 DFormClass 对象
 */
function DFormClass(){


    /**
     * 通用默认验证表单方法，错误信息使用弹出式窗口提示。
     * 此方式使用时可以分两种方式实现表单验证，即：<br/>
     * 一、注入方式，表单属性加入validate属性定义验证规则时，不需要dRules参数。<br/>
     * 二、分离方式，在JavaScript脚本定义表单验证规则，需要dRules参数。<br/>
     *
     * 错误信息提示定义：只提供表单属性中最选检查到错误的信息，<br/>
     * 即只显示一条验证错误信息，不显示所有的错误信息。<br/>
     *
     * @param formName String #+表单ID 如:#userForm 参数必须 是
     * @param dRules Object 验证规则和错误提示信息对象 参数必须 否
     * @return 验证正确返回true，验证非法返回false
     * @type boolean
     */
    this.validate = function(formName,dRules){
        if(dRules == '' || dRules == null || dRules == undefined){
            var mValidator = $(formName).validate( {
                showErrors : function(){
                    for ( var i = 0; this.errorList[i]; i++ ) {
                        var error = this.errorList[i];
                        alert(error.message);
                        error.element.focus();
                        return false;
                    }
                },
                onfocusin : function(){},
                onfocusout : function(){},
                onkeyup : function(){},
                highlight : function(){},
                unhighlight : function(){},
                onsubmit : false
            });
            return mValidator.form();
        }else{
            dRules.showErrors = function(){
                for ( var i = 0; this.errorList[i]; i++ ) {
                    var error = this.errorList[i];
                    alert(error.message);
                    error.element.focus();
                    return false;
                }
            };
            dRules.onfocusin=function(){};
            dRules.onfocusout = function(){};
            dRules.onkeyup = function(){};
            dRules.highlight = function(){};
            dRules.unhighlight = function(){};
            dRules.onsubmit = false;
            var mValidator = $(formName).validate((dRules));
            return mValidator.form();
        }
    }

    /**
     * 通用默认验证表单方法，错误信息显示在对应的字段右边。
     * 此方式使用时可以分两种方式实现表单验证，即：<br/>
     * 一、注入方式，表单属性加入validate属性定义验证规则时，不需要dRules参数。<br/>
     * 二、分离方式，在JavaScript脚本定义表单验证规则，需要dRules参数。<br/>
     *
     * 错误信息提示定义：错误信息显示在对应的字段右边，<br/>
     * 即所有错误信息都会被显示。<br/>
     *
     * @param formName String #+表单ID 如:#userForm 参数必须 是
     * @param dRules Object 验证规则和错误提示信息对象 参数必须 否
     * @return 验证正确返回true，验证非法返回false
     * @type boolean
     */
    this.exValidate = function(formName,dRules){
        if(dRules == '' || dRules == null || dRules == undefined){
            var mValidator = $(formName).validate( {
                highlight : function(){},
                unhighlight : function(){},
                onsubmit : false
            });
            return mValidator.form();
        }else{
            dRules.highlight = function(){};
            dRules.unhighlight = function(){};
            dRules.onsubmit = false;
            var mValidator = $(formName).validate((dRules));
            return mValidator.form();
        }
    }
    /**
     * 添加自定义验证方法
     * @param key 自定义验证关键字
     * @param fun 自定义验证功能函数
     * @param message 错误提示信息
     * @return 没有返回值
     * @type void
     */
    this.addValidateFunction = function(key,fun,message){
        $.validator.addMethod(key,fun,message);
    }
    /**
     * 设置嵌入表单元素，私有方法
     * @private
     * @param key 一般值为 attr
     * @param value 为表单验证规则的属性标识
     * @return 没有返回值
     * @type void
     */
    this.setMetadataType = function(key,value){
        $.metadata.setType(key, value);
    }
}

/**
 * @ignore
 * 基本JavaScript表单验证类实例对象,即引入DForm.js文件，
 * 可以通过DForm.XXX()的方式使用自动通用的表单验证函数。
 */
var DForm = new DFormClass();

/**
 * @ignore
 * 以下是DForm对象实例化和初始化步骤
 */

/**
 * @ignore
 * 设置嵌入表单元素 表单验证属性validate,实现配置验证
 * attr = "validate"
 */
DForm.setMetadataType("attr","validate");

/**
 * @ignore
 * 添加自定义的验证功能.
 * dNumeric : true 只能输入整数 包括正整数、负整数和零
 * dPNumeric : true 只能输入正整数 不包括零
 * dNNumeric : true 只能输入负正数 不包括零
 * dNotPNumeric : true 非正整数 即负整数和零
 * dNotNNumeric : true 非负整数 即正整数和零
 * dDecimal : true 只能输入浮点数 包括正浮点数、负浮点数和零
 * dPDecimal : true 只能输入正浮点数 不包括零
 * dNDecimal : true 只能输入负浮点数  不包括零
 * dNotPDecimal : true 非正浮点数 即负浮点数和零
 * dNotNDecimal : true 非负浮点数 即正浮点数和零
 * dDecimalPlaces : [2] 小数位不能超过2
 * dIp : true IP地址有效性验证
 * dUpperCase : true 只能输入大写字母
 * dLowerCase : true 只能输入小写字母
 * dAlphanumeric : true 只能输入字母（不区分大小写）和数字
 * dChinese : true 只能输入中文
 * dNotChinese : true 不能输入中文
 * dDate : [''] 表示验证日期的有效性，日期的格式为 yyyy-MM-dd dDate : ['dd/MM/yyyy'] 指定日期格式验证日期的有效性
 * dMobile :true 验证手机号码 手机号码必须是11位或是12位（0开头）的数字
 * dUrl : true 验证URL有效性
 * dDependence ：['field','value'] 依赖判断，在dDependence判断为true的情况下，才继续进行相关的验证
 */

/**
 * @ignore 扩展验证功能
 * 验证是否是整数，如果整数是空的，则返回false，默认属性如下：<br/>
 * key = dNumeric <br/>
 * message = 只能是整数！<br/>
 * @param num int 整数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dNumeric",function(num){
    if (num === ""){
        return true;
    }
    var numberReg = /^[+-]?([1-9]\d*|[0])$/;
    if(!numberReg.test(num)){
        return false;
    }
    return true;
},'只能是整数！');

/**
 * @ignore 扩展验证功能
 * 验证是否是正整数，不包括零，如果整数是空的，则返回false，默认属性如下：<br/>
 * key = dPNumeric <br/>
 * message = 只能是正整数！<br/>
 * @param num int 整数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dPNumeric",function(num){
    if (num === ""){
        return true;
    }
    var numberReg = /^[+]?([1-9]\d*)$/;
    if(!numberReg.test(num)){
        return false;
    }
    return true;
},'只能是正整数！');

/**
 * @ignore 扩展验证功能
 * 验证是否是负整数，不包括零，如果整数是空的，则返回false，默认属性如下：<br/>
 * key = dNNumeric <br/>
 * message = 只能是负整数！<br/>
 * @param num int 整数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dNNumeric",function(num){
    if (num === ""){
        return true;
    }
    var numberReg = /^[-]([1-9]\d*)$/;
    if(!numberReg.test(num)){
        return false;
    }
    return true;
},'只能是负整数！');

/**
 * @ignore 扩展验证功能
 * 验证是否是非正整数，即负整数和零，如果整数是空的，则返回false，默认属性如下：<br/>
 * key = dNotPNumeric <br/>
 * message = 只能是非正整数！<br/>
 * @param num int 整数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dNotPNumeric",function(num){
    if (num === ""){
        return true;
    }
    var numberReg = /^([-]([1-9]\d*))|([0])$/;
    if(!numberReg.test(num)){
        return false;
    }
    return true;
},'只能是非正整数！');

/**
 * @ignore 扩展验证功能
 * 验证是否是非负整数，即正整数和零，如果整数是空的，则返回false，默认属性如下：<br/>
 * key = dNotNNumeric <br/>
 * message = 只能是非负整数！<br/>
 * @param num int 整数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dNotNNumeric",function(num){
    if (num === ""){
        return true;
    }
    var numberReg = /^[+]?([1-9]\d*|[0])$/;
    if(!numberReg.test(num)){
        return false;
    }
    return true;
},'只能是非负整数！');

/**
 * @ignore 扩展验证功能
 * 验证是否是浮点数，如果浮点数是空的，则返回false，默认属性如下：<br/>
 * key = dDecimal <br/>
 * message = 只能是浮点数！<br/>
 * @param dec float 浮点数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dDecimal",function(dec){
    if (dec === ""){
        return true;
    }
    var decimalReg = /^[+-]?([1-9]\d*|[1-9]\d*[.]\d*|0[.]\d*[1-9]\d*|0?[.]0+|0)$/;
    if(!decimalReg.test(dec)){
        return false;
    }
    return true;
},'只能是浮点数！');

/**
 * @ignore 扩展验证功能
 * 验证是否是正浮点数，不包括零，如果浮点数是空的，则返回false，默认属性如下：<br/>
 * key = dPDecimal <br/>
 * message = 只能是正浮点数！<br/>
 * @param dec float 浮点数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dPDecimal",function(dec){
    if (dec === ""){
        return true;
    }
    var decimalReg = /^[+]?([1-9]\d*|[1-9]\d*[.]\d*|0[.]\d*[1-9]\d*)$/;
    if(!decimalReg.test(dec)){
        return false;
    }
    return true;
},'只能是正浮点数！');

/**
 * @ignore 扩展验证功能
 * 验证是否是负浮点数，不包括零，如果浮点数是空的，则返回false，默认属性如下：<br/>
 * key = dNDecimal <br/>
 * message = 只能是负浮点数！<br/>
 * @param dec float 浮点数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dNDecimal",function(dec){
    if (dec === ""){
        return true;
    }
    var decimalReg = /^[-]([1-9]\d*|[1-9]\d*[.]\d*|0[.]\d*[1-9]\d*)$/;
    if(!decimalReg.test(dec)){
        return false;
    }
    return true;
},'只能是负浮点数！');

/**
 * @ignore 扩展验证功能
 * 验证是否是非正浮点数，即负浮点数和零，如果浮点数是空的，则返回false，默认属性如下：<br/>
 * key = dNotPDecimal <br/>
 * message = 只能是非正浮点数！<br/>
 * @param dec float 浮点数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dNotPDecimal",function(dec){
    if (dec === ""){
        return true;
    }
    var decimalReg = /^([-]([1-9]\d*|[1-9]\d*[.]\d*|0[.]\d*[1-9]\d*))|([0])$/;
    if(!decimalReg.test(dec)){
        return false;
    }
    return true;
},'只能是非正浮点数！');

/**
 * @ignore 扩展验证功能
 * 验证是否是非负浮点数，即正浮点数和零，如果浮点数是空的，则返回false，默认属性如下：<br/>
 * key = dNotNDecimal <br/>
 * message = 只能是非负浮点数！<br/>
 * @param dec float 浮点数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dNotNDecimal",function(dec){
    if (dec === ""){
        return true;
    }
    var decimalReg = /^[+]?([1-9]\d*|[1-9]\d*[.]\d*|0[.]\d*[1-9]\d*|[0])$/;
    if(!decimalReg.test(dec)){
        return false;
    }
    return true;
},'只能是非负浮点数！');

/**
 * @ignore 扩展验证功能
 * 验证是小数几位数，如果浮点数是空的，则返回false，默认属性如下：<br/>
 * key = dDecimalPlaces <br/>
 * message = 小数位数不对！<br/>
 * @param number float 浮点数 参数必须 是
 * @param params 参数集 params[0] int 小数位数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dDecimalPlaces",function(number, element, params){
    if (number ==="") return this.optional(element) || true;

    if(number.indexOf("-") != -1 || number.indexOf("+") != -1 ){
        number = number.substring(1,number.length);
    }

    var i;
    var numOfPeriod=0;
    var numOfDecimalPlaces=0;
    for(i = 0; i < number.length; i++){
        var ch = number.charAt(i);
        if (ch=='.') numOfPeriod++;
        if (numOfPeriod>1) return this.optional(element) || false;
        if ((ch < '0' || ch > '9') && (ch !='.')) return this.optional(element) || false;
        if (numOfPeriod==1 && (ch >= '0' && ch <= '9')) numOfDecimalPlaces++;
        if (numOfDecimalPlaces > params[0]) return this.optional(element) || false;
    }
    return this.optional(element) || true;
},jQuery.format("小数位数不对，位数必须是: {0} ！"));


/**
 * @ignore 扩展验证功能
 * 验证IP地址的合法性，如果IP地址是空的，则返回false，默认属性如下：<br/>
 * key = dIp <br/>
 * message = IP地址不合法！<br/>
 * @param ip String IP地址 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dIp",function(ip){
    if(ip == ''){
        return true;
    }
    var ipReg =/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
    if(ipReg.test(ip)){
        return true;
    }
    return false;
},'IP地址不合法！');


/**
 * @ignore 扩展验证功能
 * 验证是否全是大写字母，如果字符串是空的，则返回false，默认属性如下：<br/>
 * key = dUpperCase <br/>
 * message = 必须是大写字母！<br/>
 * @param str String 字符串 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dUpperCase",function(str){
    if(str == '' || str == null || str == undefined){
        return true;
    }
    var i;
    for (i = 0; i < str.length; i++){
        var ch = str.charAt(i);
        if (ch < 'A' || ch > 'Z')return false;
    }
    return true;
},'必须是大写字母！');

/**
 * @ignore 扩展验证功能
 * 验证是否全是小写字母，如果字符串是空的，则返回false，默认属性如下：<br/>
 * key = dLowerCase <br/>
 * message = 必须是小写字母！<br/>
 * @param str String 字符串 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dLowerCase",function(str){
    if(str == '' || str == null || str == undefined){
        return true;
    }
    var i;
    for (i = 0; i < str.length; i++){
        var ch = str.charAt(i);
        if (ch < 'a' || ch > 'z')return false;
    }
    return true;
},'必须是小写字母！');

/**
 * @ignore 扩展验证功能
 * 判断字符串是否是只包含大小写字母和数字，如果字符串是空的，则返回false，默认属性如下：<br/>
 * key = dAlphanumeric <br/>
 * message = 只能是大小写字母和数字！<br/>
 * @param str String 字符串 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dAlphanumeric",function(str){
    if(str == '' || str == null || str == undefined){
        return true;
    }
    var i;
    for (i = 0; i < str.length; i++){
        var ch = str.charAt(i);
        if ( ((ch < 'A' || ch > 'Z') && (ch < 'a' || ch > 'z')) && (ch < '0' || ch > '9') )	return false;
    }
    return true;
},'只能是大小写字母和数字！');

/**
 * @ignore 扩展验证功能
 * 验证只能输入中文字符，默认属性如下：<br/>
 * key = dChinese <br/>
 * message = 只能输入中文字符！<br/>
 * @param str String 字符串 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dChinese",function(str){
    if(str == '' || str == null || str == undefined){
        return true;
    }
    var i;
    var chinese_reg = /[\u4e00-\u9fa5]/;
    for (i = 0; i < str.length; i++){
        var ch = str.charAt(i);
        if(!chinese_reg.test(ch)){
            return false;
        }
    }
    return true;
},"只能输入中文字符！");

/**
 * @ignore 扩展验证功能
 * 验证字符串不能包含中文字符，默认信息如下：<br/>
 * key = dNotChinese <br/>
 * message = 不能输入中文字符！<br/>
 * @param str String 字符串 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dNotChinese",function(str){
    if(str == '' || str == null || str == undefined){
        return true;
    }
    var i;
    var chinese_reg = /[\u4e00-\u9fa5]/;
    for (i = 0; i < str.length; i++){
        var ch = str.charAt(i);
        if(chinese_reg.test(ch)){
            return false;
        }
    }
    return true;
},"不能输入中文字符！");


/**
 * @ignore 扩展验证功能
 * 验证日期是否合法，如果日期是空的，则返回false，默认信息如下：<br/>
 * key = dDate <br/>
 * message = 请输入正确的日期！<br/>
 * @param date Date 日期 参数必须 是
 * @param params 参数集，即params[0]表示日期格式 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dDate",function(date, element, params){
    var dformat = '';
    if(date == ''){
        return this.optional(element) || true;
    }
    if( ((params[0] == null)) || (params[0] == '')|| (params[0] == "undefined")){
        dformat = "yyyy-MM-dd";
    }else{
        dformat = params[0];
    }
    if(date.length != dformat.length){
        return this.optional(element) || false;
    }
    var year,month,day,yi,Mi,di;
    yi = dformat.indexOf("yyyy");
    Mi = dformat.indexOf("MM");
    di = dformat.indexOf("dd");
    if(yi == -1 || Mi == -1 || di == -1){
        return this.optional(element) || false;
    }else{
        year = date.substring(yi, yi+4);
        month = date.substring(Mi, Mi+2);
        day = date.substring(di, di+2);
    }

    if(isNaN(year) || isNaN(month) || isNaN(day)){
        return this.optional(element) || false;
    }
    if(year.length != 4 || month.length != 2 || day.length != 2){
        return this.optional(element) || false;
    }

    if (month < 1 || month > 12) { // check month range
        return this.optional(element) || false;
    }
    if (day < 1 || day > 31) {
        return this.optional(element) || false;
    }
    if ((month==4 || month==6 || month==9 || month==11) && day==31) {
        return this.optional(element) || false;
    }
    if (month == 2) { // check for february 29th
        var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
        if (day>29 || (day==29 && !isleap)) {
            return this.optional(element) || false;
        }
    }
    if (year>9998 || year<1950)
        return this.optional(element) || false;
    return this.optional(element) || true;
},jQuery.format("请输入正确的日期，日期格式为: {0} ！"));



/**
 * @ignore 扩展验证功能
 * 验证手机号码是否合法，如果手机号码是空的，则返回false，默认信息如下：<br/>
 * key = dMobile <br/>
 * message = 手机号码不合法！<br/>
 * @param mobile String 手机号码 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dMobile",function(mobile){
    if(mobile == '' || mobile == null || mobile == undefined){
        return true;
    }
    var mReg =/(^[0-9]{11}$)|(^0[0-9]{11}$)/;
    if (mReg.test( mobile)) {
        return true;
    }
    return false;
},"手机号码不合法！");

/**
 * @ignore 扩展验证功能
 * 验证URL是否合法，如果URL是空的，则返回false，默认信息如下：<br/>
 * key = dMobile <br/>
 * message = URL不合法！<br/>
 * @param mobile String 手机号码 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dUrl",function(url){
    if(url == ''){
        return true;
    }
    var httpReg = /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/i;
    var ftpReg = /^ftp:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/i;
    var httpsReg = /^https:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/i;
    var urlReg = /^[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/i;
    var flag = false;
    flag = httpReg.test(url);
    if(flag){
        return flag;
    }
    flag = ftpReg.test(url);
    if(flag){
        return flag;
    }
    flag = httpsReg.test(url);
    if(flag){
        return flag;
    }
    flag = urlReg.test(url);
    return flag;
},"URL不合法！");

/**
 * @ignore 扩展验证功能
 * 依赖判断，在dDependence判断为true的情况下，才继续进行相关的验证：<br/>
 * key = dDependence <br/>
 * message 不起提示作用
 * @param params ['field','value'] 依赖判断参数 参数必须 是
 * @return 合法返回true，非法返回false
 * @type boolean
 */
DForm.addValidateFunction("dDependence",function(fieldName, element, params){
    if(params.length == 2){
        var v ='';
        try{
            v = document.getElementById(params[0]).value;
        }catch(e){
            v = null;
        }
        try{
            if(v == '' || v == null || v == undefined){
                v = document.getElementsByName(params[0])[0].value;
            }
        }catch(e){
            v = null;
        }
        return v == params[1];
    }else{
        return false;
    }
},"依赖条件不通过不需要再验证!");





