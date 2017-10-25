//<!--
//author:saron
//-->
var regYyyy_mm_dd_A = /^(\d{4})-(\d{1,2})-(\d{1,2})$/;
var regDateTime = /^(\d{4})-(\d{1,2})-(\d{1,2})\s(\d{1,2}):(\d{1,2}):(\d{1,2})$/;
var regChineseY_m_d = /^(\d{4})年(\d{1,2})月(\d{1,2})$/;
var regSlashY_m_d = /^(\d{4})\/(\d{1,2})\/(\d{1,2})$/;
var regSlashYmd = /^(\d{4})(\d{1,2})(\d{1,2})$/;
var sDateFormatA = "yyyy-mm-dd";
//默认的日期格式
var sDateFormatB = "yyyy年mm月dd日";
//中文格式
var sDateFormatC = "yyyy/mm/dd";
var MONTH_LENGTH = [31,28,31,30,31,30,31,31,30,31,30,31];
var LEAP_MONTH_LENGTH = [31,29,31,30,31,30,31,31,30,31,30,31];
var showMsg = "";

function checkNull(item) {
    if (isNull(item.value)) {
        $.messager.alert("错误",item.getAttribute("showName") + "不能为空!\n");
        return false;
    }
    return true;
}

//判断字符串的长度
function strlength($str) {
    var l = $str.length;
    var n = l
    for (var i = 0; i < l; i++)
    {
        if ($str.charCodeAt(i) < 0 || $str.charCodeAt(i) > 255) n++
    }
    return n
}


function getStrByLen($str, $len) {
    //var l = $str.length;
    var s = "";
    var n = 0;
    for (var i = 0; n < $len; i++)
    {
        n++;
        if ($str.charCodeAt(i) < 0 || $str.charCodeAt(i) > 255)n++;
        if (n <= $len)
            s = s + $str.charAt(i);
    }
    return s;
}

function checkLength($ele, $type) {
    var leng = strlength($ele.value);
    var mustLeng = $ele.getAttribute("minLength");
    if ($type == 0 && leng > mustLeng) {
        $.messager.alert("错误",$ele.getAttribute("showName") + "的长度最长不能超过" + mustLeng + "位!\n");
        return false;
    }
    if ($type == 1 && leng < mustLeng) {
        $.messager.alert("错误",$ele.getAttribute("showName") + "的长度最长不能少于" + mustLeng + "位!\n");
        return false;
    }
    return true;
}

//function checkInputSize($Obj, length) {
//    // onkeyup ="checkInputSize(this,80)" onbeforepaste="doPasteCheck(this,80)"
//    if (!event.ctrlKey && event.keyCode != 8) {
//        //  $Obj.value=getStrByLen($Obj.value,length);
//        var offset = 0;
//        if (($Obj.getAttribute("isNumber") == "true") && $Obj.value.indexOf(".") > -1)offset++;
//        $.messager.alert("错误",offset);
//        var l = strlength($Obj.value)
//        if (l > (length + offset)) {
//            $.messager.alert("错误","本字段最多只能输入" + length + "位的英文字母或其一半长度的汉字！");
//            $Obj.value = getStrByLen($Obj.value, length);
//            $Obj.focus();
//            return;
//        }
//    }
//}

var eventObjMap = new HashMap("eventObjMap");
function checkInputSize() {
    // onkeyup ="checkInputSize(this,80)" onbeforepaste="doPasteCheck(this,80)"
    var currInputObj = event.srcElement;
    var currInputLength = eventObjMap.get(currInputObj.name).getAttribute("charLimt");
    var temp = currInputLength;
    if (!event.ctrlKey && event.keyCode != 8) {
        if ((currInputObj.getAttribute("allowPoint") == "true") && currInputObj.value.indexOf(".") > -1)currInputLength++;
        var l = strlength(currInputObj.value);
        if (l > (currInputLength )) {
            $.messager.alert("错误","本字段最多只能输入" + temp + "位的英文字母或其一半长度的汉字！");
            currInputObj.value = getStrByLen(currInputObj.value, currInputLength);
            currInputObj.focus();
            return;
        }
    }
}

function doPasteCheck() {
    //      if(!event.ctrlKey)
    //      currInputObj.value=getStrByLen(currInputObj.value,currInputLength);
    //      var valuelen = 0;
    //      if(currInputObj.value!=null&&currInputObj.value!="")
    //      valuelen=strlength(currInputObj.value);
    //      if (clipboardData.getData("Text") != null)
    //        if ((valuelen+strlength(clipboardData.getData("Text"))) > currInputLength) {
    //          clipboardData.clearData();
    //          $.messager.alert("错误","本字段最多只能输入"+length+"位的英文字母或其一半长度的汉字！");
    //          event.cancelBubble = true;
    //          event.returnvalue = false;
    //          return;
    //        }
}

function checkDate(sValue, showName, sFormat) {
    if (sValue != "") {
        var regUseFormat = null;
        /**
         * 默认值为YYYY-mm-dd
         */
        if (sFormat == null) {
            sFormat = sDateFormatA;
            regUseFormat = regYyyy_mm_dd_A;
        }

        if (sFormat == "yyyy-mm-dd") {
            regUseFormat = regYyyy_mm_dd_A;
            //yyyy-mm-dd
        } else if (sFormat == "yyyy年mm月dd日") {
            regUseFormat = regChineseY_m_d;
            //yyyy年mm月dd日
        } else if (sFormat == "yyyy/mm/dd") {
            regUseFormat = regSlashY_m_d;
            //yyyy/mm/dd
        } else if (sFormat == "yyyymmdd") {
            regUseFormat = regSlashYmd;
            //yyyy/mm/dd
        } else if (sFormat == "yyyy-MM-dd HH:mm:ss") {
            regUseFormat = regDateTime;
            //yyyy/mm/dd
        } else {
            $.messager.alert("错误",showName + "正确的格式应为:" + sFormat + "!\n");
            return false;
        }

        if (!regUseFormat.test(sValue)) {
            $.messager.alert("错误",showName + "应为日期类型!\n");
            return false;
        }

        /**
         * 检查日期的年月日是否正确
         */
        var aryYmd = sValue.match(regUseFormat);
        var iYear = aryYmd[1];
        var iMonth = aryYmd[2];
        var iDay = aryYmd[3];

        if (iYear < 1 || iYear > 9999 || iMonth < 1 || iMonth > 12 || iDay < 1 || iDay > getMonthDay(iMonth - 1, iYear)) {
            $.messager.alert("错误",showName + "中的日期有误!\n");
            return false;
        }
        return true;
    }
    else return true;
}

function checkTime(begintime, endtime) {
    var bt = document.getElementsByName(begintime)[0].value;
    var et = document.getElementsByName(endtime)[0].value;
    bt = bt.replace("-", "/");
    et = et.replace("-", "/");
    var dbt = new Date(bt);
    var det = new Date(et);
    var today = new Date();
    today = today.getFullYear() + "/" + (today.getMonth() + 1) + "/" + today.getDate();
    today = new Date(today);
    if (dbt.getTime() < today.getTime()) {
        $.messager.alert("错误","开始时间不能早于当天");
        document.getElementsByName(begintime)[0].value = "";
        return false;
    }
    if (det.getTime() < today.getTime()) {
        $.messager.alert("错误","结束时间不能早于当天");
        document.getElementsByName(endtime)[0].value = "";
        return false;
    }
    if (dbt.getTime() > det.getTime()) {
        $.messager.alert("错误","开始时间不能早于结束时间");
        document.getElementsByName(begintime)[0].value = "";
        document.getElementsByName(endtime)[0].value = "";
        return false;
    }
    return true;
}

function checkObjDate(obj, sFormat) {
    var sValue = obj.value;
    if (sValue != "") {
        return checkDate(sValue, obj.getAttribute("showName"), sFormat);
    }
    else return true;
}

function checkObjDateTime($obj, $sFormat) {
    $.messager.alert("错误","判断是日期时间的方法暂未实现");
    return false;
    var sValue = $obj.value;
    if (sValue != "") {
        if (checkObjDate($obj, $sFormat)) {
        }
    } else return true;
}

/**
 * private 检查日期是否润年
 * @param iYear 年份
 * @param boolean
 */
function isLeapYear(iYear) {
    return ((iYear % 4 == 0) && ((iYear % 100 != 0) || (iYear % 400 == 0)));
}

/**
 * private 获得月份的日子数
 * @param iMonth 月
 * @param iYear 年
 * @return int 日数
 */
function getMonthDay(iMonth, iYear) {
    return isLeapYear(iYear) ? LEAP_MONTH_LENGTH[iMonth] : MONTH_LENGTH[iMonth];
}


function checkAllNull(bv, item) {
    var b = true;
    b = checkNull(item);
    if (bv && !b)bv = false;
    return bv;
}

function checkAllDate(bv, item) {
    var b = true;
    b = checkObjDate(item);
    if (bv && !b)bv = false;
    return bv;
}

function checkMinLength(bv, item) {
    var b = true;
    b = checkLength(item, 1);
    if (bv && !b)bv = false;
    return bv;
}


function checkDateBefor(sDate, lDate) {
    if (sDate.getTime() < lDate.getTime())
        return false;
    else return true;
}
//判断上架时间晚于等与当前时间用
function checkDateBefor2(sDate, lDate) {
    if (sDate.getTime() < lDate.getTime())
        return false;
    else return true;
}

function checkDateAfter(sDate, lDate) {
    if (sDate.getTime() > lDate.getTime())
        return false;
    else return true;
}
//折扣活动时间要早于等于下架时间
function checkDateAfter2(sDate, lDate) {
    if (sDate.getTime() < lDate.getTime())
        return false;
    else return true;
}

function checkAllDateBefor(bv, sDate, lDate) {
    var b = true;
    b = checkDateBefor(sDate, lDate);
    if (bv && !b)bv = false;
    return bv;
}


//可以判断的项有
//checknull:判断不能为空
//dateBeforCurr :日期应该早于当前日期
//dateAfterCurr:日期应该晚于当前日期
//dateAfterCurr2:日期应该晚于或等于当前日期
//dateAfter:是否晚于另一字段日期
//dateAfter2:是否早于或者等于另一字段日期
//CardID :检查身份证号
//isNumber:判断是否为数据类型
//isTel：判断是否为电话号码和手机号码，多个用、分开
//isSignlessIntegral:判断是否为正整数
//isDate: 判断是否日期类型
//isDateTime: 判断是否日期类型
//checkCmcc 判断号码是否是中移动的号码
//checkGroupID判断拥有这一属性的元素是否有任一人被选中
//checkGorupType 0代表最少要有一个，1代表有且只能有一个
//minLength 最少多长
//isAmount 金额格式：00000.00
//isMoney  金额格式：最大5位整数，2位小数 如：10000.00（isAmount的特殊情况）
//isMoney4 金额格式：最大4位整数，2位小数 如：1000.00（isAmount的特殊情况）
//isMoney8 金额格式：最大8位整数，2位小数 如：10000000.00（isAmount的特殊情况）
//isPretent 判断百份比100.00，且不超过100.00
//isPretent1 判断百份比0.15，且不超过1.0
//checkPhotoType 判断图片格式
function doCheckForm(checkForm) {
    var els = checkForm.elements;
    for (var i = 0; i < els.length; i++) {
        var e = els[i];
        var dependObj = null;
        var shoudCheck = true;
        if (e.getAttribute("dependObjID") != null) {
            dependObj = document.getElementById(e.getAttribute("dependObjID"));
            if (dependObj.style.display == "none")shoudCheck = false;
            if (dependObj.tagName == "INPUT") {
                if (dependObj.type == "checkbox" || dependObj.type == "radio")
                    if (!dependObj.checked)shoudCheck = false;
            }
        }

        if (e.getAttribute("pattern") != null) {
            if (!checkPattern(e)) {
                foucus(e);
                return false;
            }
        }

        if (shoudCheck)
        {
            //            if (e.getAttribute("checkGroupID") != null && e.getAttribute("checkGroupID")) {
            //                  switch(e.typeName)
            //            }
            if (e.getAttribute("checkNull") == "true") {
                if (!checkNull(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isDateTime") == "true") {
                if (!checkObjDate(e, 'yyyy-MM-dd HH:mm:ss')) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isDate") == "true") {
                if (!checkObjDate(e, "yyyy-mm-dd")) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("minLength") != null && e.getAttribute("minLength") != "") {
                if (!checkLength(e, 1)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("trim") != null && e.getAttribute("trim") != "" && e.getAttribute("trim") == "true") {
                var subString = e.value;
                if (subString != null && subString != "") {
                    subString = Trim(subString);
                    e.value = subString;
                }

            }
            if (e.getAttribute("dateBeforCurr") == "true") {
                var chooseDate = e.value;
                if (chooseDate != "") {
                    var temp = chooseDate.split("-");
                    var tempDate = temp[1] + "/" + temp[2] + "/" + temp[0];
                    var tb = checkDateBefor(new Date(), new Date(tempDate));
                    if (!tb) {
                        $.messager.alert("错误",e.getAttribute("showName") + "应早于当前日期!\n");
                        foucus(e);
                        return false;
                    }
                }
            }
            //判断时间晚于当前时间
            if (e.getAttribute("dateAfterCurr") == "true") {
                var chooseDate = e.value;
                if (chooseDate != "") {
                    var temp = chooseDate.split("-");
                    var tempDate = temp[1] + "/" + temp[2] + "/" + temp[0];
                    var tb = checkDateBefor(new Date(tempDate), new Date());
                    if (!tb) {
                        $.messager.alert("错误",e.getAttribute("showName") + "应晚于当前日期!\n");
                        foucus(e);
                        return false;
                    }
                }
            }
            //判断时间晚于等于当前时间
            if (e.getAttribute("dateAfterCurr2") == "true") {
                var chooseDate = e.value;
                if (chooseDate != "") {
                    var temp = chooseDate.split("-");
                    var tempDate = temp[1] + "/" + temp[2] + "/" + temp[0];
                    var cur = new Date();
                    var curYear = cur.getYear();
                    curMonth = cur.getMonth() + 1;
                    curDay = cur.getDate();
                    var riqi = curMonth + "/" + curDay + "/" + curYear;
                    var tb = checkDateBefor2(new Date(tempDate), new Date(riqi));
                    if (!tb) {
                        $.messager.alert("错误",e.getAttribute("showName") + "应等于或晚于当前日期!\n");
                        foucus(e);
                        return false;
                    }
                }
            }
            if (e.getAttribute("dateAfter") != "" && e.getAttribute("dateAfter") != null) {
                var chooseDate = e.value;
                var newDayElement = document.getElementsByName(e.getAttribute("dateAfter"));
                var newDay = newDayElement[0].value;
                if (chooseDate != "" && newDay != "") {
                    var temp = chooseDate.split("-");
                    var tempDate = temp[1] + "/" + temp[2] + "/" + temp[0];
                    var newTemp = newDay.split("-");
                    var newDate = newTemp[1] + "/" + newTemp[2] + "/" + newTemp[0];
                    var tb = checkDateAfter(new Date(newDate), new Date(tempDate));
                    if (!tb) {
                        $.messager.alert("错误",e.getAttribute("showName") + "应晚于" + newDayElement[0].getAttribute("showName") + "!\n");
                        foucus(e);
                        return false;
                    }
                }
            }
            if (e.getAttribute("dateBefore") != "" && e.getAttribute("dateBefore") != null) {
                var chooseDate = e.value;
                var newDayElement = document.getElementsByName(e.getAttribute("dateBefore"));
                var newDay = newDayElement[0].value;
                if (chooseDate != "" && newDay != "") {
                    var temp = chooseDate.split("-");
                    var tempDate = temp[1] + "/" + temp[2] + "/" + temp[0];
                    var newTemp = newDay.split("-");
                    var newDate = newTemp[1] + "/" + newTemp[2] + "/" + newTemp[0];
                    var tb = checkDateAfter(new Date(newDate), new Date(tempDate));
                    if (tb) {
                        $.messager.alert("错误",e.getAttribute("showName") + "应早于" + newDayElement[0].getAttribute("showName") + "!\n");
                        foucus(e);
                        return false;
                    }
                }
            }
            //折扣活动时间要早于等于下架时间
            //dateAfter2="aaaaa",意思是当前的对象早于等于aaaaa对象
            if (e.getAttribute("dateAfter2") != "" && e.getAttribute("dateAfter2") != null) {
                var chooseDate = e.value;
                var newDayElement = document.getElementsByName(e.getAttribute("dateAfter2"));
                var newDay = newDayElement[0].value;
                if (chooseDate != "" && newDay != "") {
                    var temp = chooseDate.split("-");
                    var tempDate = temp[1] + "/" + temp[2] + "/" + temp[0];
                    var newTemp = newDay.split("-");
                    var newDate = newTemp[1] + "/" + newTemp[2] + "/" + newTemp[0];
                    var tb = checkDateAfter2(new Date(newDate), new Date(tempDate));
                    if (!tb) {
                        $.messager.alert("错误",e.getAttribute("showName") + "早于等于" + newDayElement[0].getAttribute("showName") + "!\n");
                        foucus(e);
                        return false;
                    }
                }
            }
            if (e.getAttribute("CardID") == "true") {
                if (!checkCertID(e)) {
                    $.messager.alert("错误","请在" + e.getAttribute("showName") + "中输入正确的身份证号码")
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isTel") == "true") {
                if (!checktel(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isNumber") == "true") {
                if (!checkNumber(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isSignlessIntegral") == "true") {
                if (!checkSignlessNumber(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isPretent") == "true") {
                if (!checkPretent(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isPretent1") == "true") {
                if (!checkPretent1(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("checkCmcc") == "true") {
                if (!checkCmcc(e)) {
                    foucus(e);
                    return false;
                }
            }

            if (e.getAttribute("isAmount") == "true") {
                if (!checkAmount(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("formatValue") == "true") {
                if (!checkFormatValue(e, e.getAttribute("formatter"))) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isMoney8") == "true") {
                if (!checkMoney8(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isMoney4") == "true") {
                if (!checkMoney4(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("isMoney") == "true") {
                if (!checkMoney(e)) {
                    foucus(e);
                    return false;
                }
            }
            if (e.getAttribute("checkPhotoType") == "true") {
                if (!checkphototype(e)) {
                    foucus(e);
                    return false;
                }
            }
        }
    }
    return true;
}
function checkAmount($ele) {
    var eleValue = $ele.value;
    if (eleValue == "" || eleValue.replace(/^[1-9]+[0-9]*$|^[1-9]+[0-9]*\.\d\d?$|^0\.\d\d?$|^0$/g, "") == "")
        return true;
    $.messager.alert("错误",$ele.getAttribute("showName") + "的格式错误，小数长度最多为2位");
    return false;

}

function checkFormatValue($ele, $formatter) {
    var s = $ele.value;
    var integer = $formatter.split(",")[0];
    var decimal = $formatter.split(",")[1];
    var index = s.indexOf(".");
    if ((index == -1) && s.length > integer) {
        $.messager.alert("错误",$ele.getAttribute("showName") + "整数部分最大为" + integer + "位");
        return false;
    }
    if (index > integer) {
        $.messager.alert("错误",$ele.getAttribute("showName") + "整数部分最大为" + integer + "位");
        return false;
    }
    var regu = /^[1-9]+[0-9]*$|^[1-9]+[0-9]*\.\d\d?$|^0\.\d\d?$|^0$/;
    var re = new RegExp(regu);
    var rs = true;
    if (s != "") {
        rs = re.test(s);
    }
    if (!rs)$.messager.alert("错误",$ele.getAttribute("showName") + "格式错误：整数最多" + integer + "位，小数最多" + decimal + "位!");
    return rs;
}

function checkMoney($ele) {
    var s = $ele.value;
    var index = s.indexOf(".");
    //$.messager.alert("错误",index);
    if ((index == -1) && s.length > 5) {
        $.messager.alert("错误","价格整数部分最大为5位");
        return false;
    }
    if (index > 5) {
        $.messager.alert("错误","价格整数部分最大为5位");
        return false;
    }
    //if(s.charAt(s.length-1)=="."){
    //	$.messager.alert("错误",'最后一位不能为"."，请重新输入');
    //	return false;
    //}
    //var regu =/^\d+\.{0,1}\d{0,2}$/;
    //判断的格式有：最后一位不为".",最多2位小数，整数超过一位后第一位不为0（零）
    var regu = /^[1-9]+[0-9]*$|^[1-9]+[0-9]*\.\d\d?$|^0\.\d\d?$|^0$/;
    var re = new RegExp(regu);
    var rs = true;
    if (s != "") {
        rs = re.test(s);
    }
    if (!rs)$.messager.alert("错误","价格格式错误：整数最多5位，小数最多2位!");
    return rs;
}
function checkMoney4($ele) {
    var s = $ele.value;
    var index = s.indexOf(".");
    if ((index == -1) && s.length > 4) {
        $.messager.alert("错误","费用整数部分最大为4位");
        return false;
    }
    if (index > 4) {
        $.messager.alert("错误","费用整数部分最大为4位");
        return false;
    }
    //if(s.charAt(s.length-1)=="."){
    //	$.messager.alert("错误",'最后一位不能为"."，请重新输入');
    //	return false;
    //}
    //var regu =/^\d+\.{0,1}\d{0,2}$/;
    //判断的格式有：最后一位不为".",最多2位小数，整数超过一位后第一位不为0（零）
    var regu = /^[1-9]+[0-9]*$|^[1-9]+[0-9]*\.\d\d?$|^0\.\d\d?$|^0$/;
    var re = new RegExp(regu);
    var rs = true;
    if (s != "") {
        rs = re.test(s);
    }
    if (!rs)$.messager.alert("错误","费用格式错误：整数最多4位，小数最多2位!");
    return rs;
}
//整数部分10位
function checkMoney8($ele) {
    var s = $ele.value;
    var index = s.indexOf(".");
    //$.messager.alert("错误",index);
    if ((index == -1) && s.length > 8) {
        $.messager.alert("错误","消费数据整数部分最大为8位");
        return false;
    }
    if (index > 8) {
        $.messager.alert("错误","消费数据整数部分最大为8位");
        return false;
    }
    //if(s.charAt(s.length-1)=="."){
    //	$.messager.alert("错误",'最后一位不能为"."，请重新输入');
    //	return false;
    //}
    //var regu =/^\d+\.{0,1}\d{0,2}$/;
    //判断的格式有：最后一位不为".",最多2位小数，整数超过一位后第一位不为0（零）
    var regu = /^[1-9]+[0-9]*$|^[1-9]+[0-9]*\.\d\d?$|^0\.\d\d?$|^0$/;
    var re = new RegExp(regu);
    var rs = true;
    if (s != "") {
        rs = re.test(s);
    }
    if (!rs)$.messager.alert("错误","消费数据格式错误：整数最多8位，小数最多2位!");
    return rs;
}

function foucus($ele) {
    try {
        if ($ele.getAttribute("isHidden") != "true")  $ele.focus();
    } catch(e) {

    }
}

function checkByFormName(formName) {
    var checkForm = document.getElementsByName(formName);
    return doCheckForm(checkForm[0]);
}

function checkByFormID(formID) {
    var checkForm = document.getElementById(formID);
    return doCheckForm(checkForm);
}

function checkNumber(obj_number) {
    var req = null;
    if (obj_number.getAttribute("allowPoint") == "true") {
        req = /^\d+\.\d+$|^\d+$/;
    } else req = /^\d+$/;
    var n_value = obj_number.value;
    var rs = true;
    if (n_value != "") {
        rs = req.test(n_value);
        //        for (i = 0; i < n_value.length; i++) {
        //            if (n_value.charAt(i).search(/\d/) == -1) {
        //                rs = false;
        //            }
        //        }
    }
    if (!rs)showMsg = $.messager.alert("错误",obj_number.getAttribute("showName") + "应为数字类型\n");
    return rs;
}
//此方法不做判断。
function checktel(obj_number) {
    return true;//不做判断
    //var req = null;    
    //req = /^\d+\、\d+$|^\d+$/;    
    //var n_value = obj_number.value;
    //var rs = true;
    //if (n_value != "") {
    //    rs = req.test(n_value);
    //}
    //if (!rs)showMsg = $.messager.alert("错误",obj_number.getAttribute("showName") + "应为电话类型,如:02088888888、13888888888\n");
    //return rs;
}
function checkSignlessNumber(obj_number) {
    var req = null;
    if (obj_number.getAttribute("allowPoint") == "true") {
        req = /^\d+\.\d+$|^\d+$/;
    } else req = /^[1-9]\d*$/;
    var n_value = obj_number.value;
    var rs = true;
    if (n_value != "") {
        rs = req.test(n_value);
    }
    if (!rs)showMsg = $.messager.alert("错误",obj_number.getAttribute("showName") + "应为大于0的正整数\n");
    return rs;
}

function checkPretent(obj_number) {
    var req = /1?\d{1,2}\.\d{2}/;
    //var currencyPayRate = document.getElementsByName("case2")[0].value;
    //$.messager.alert("错误",currencyPayRate);
    var currencyPayRate = obj_number.value;
    var num = parseFloat(currencyPayRate);
    if (num >= 0 && num < 100) {
        if (!req.test(currencyPayRate)) {
            $.messager.alert("错误","折扣率输入不合规范，请采用 87.00 的类似格式输入，并且不要超过100.00!");
            return false;
        }
    }
    else {
        $.messager.alert("错误","折扣率输入不合规范，请采用 87.00 的类似格式输入，并且不要超过100.00!");
        return false
    }
    return true;
}
function checkPretent1(obj_number) {
    var req = /^[0][.]+([0-9]{1,9})?$/;
    //var currencyPayRate = document.getElementsByName("case2")[0].value;
    //$.messager.alert("错误",currencyPayRate);
    var currencyPayRate = obj_number.value;
    var num = parseFloat(currencyPayRate);
    if (num >= 0 && num < 1) {
        if (!req.test(currencyPayRate)) {
            $.messager.alert("错误","请输入小于1的小数!");
            return false;
        }
    }
    else {
        $.messager.alert("错误","请输入小于1的小数!");
        return false
    }
    return true;
}
function checkPattern(obj_pattern) {
    var req = obj_pattern.getAttribute("pattern");
    var temp = new RegExp(req, "g");
    if (temp.test(obj_pattern.value)) {
        return true;
    } else {
        $.messager.alert("错误",obj_pattern.getAttribute("showName") + "不符合规范，请重新输入");
        return false;
    }
}

function checkCertID(obj_certID) {
    var rs = true;
    var certID = obj_certID.value;
    //先去掉身份证前后的空格
    certID = certID.replace(/(^\s+)|(\s+$)/g, "");
    if (certID != "") {
        //    for (i = 0; i < certID.length; i++) {
        //      if (certID.charAt(i).search(/\d/) == -1) {
        //        rs = false;
        //      }
        //    }
        //    if (!rs)$.messager.alert("错误",obj_certID.getAttribute("showName")+"包含非数字字符");
    }
    else return false;
    if (certID.length == 15 || certID.length == 18) {
        if (certID.length == 15) {
            year = "19" + certID.substring(6, 8);
            month = certID.substring(8, 10);
            day = certID.substring(10, 12);
        } else {
            year = certID.substring(6, 10);
            month = certID.substring(10, 12);
            day = certID.substring(12, 14);
        }
        rs = checkDate(year + month + day, obj_certID.getAttribute("showName"), "yyyymmdd")
    } else {
        $.messager.alert("错误",("showName") + "不应为" + certID.length + "位，请纠正\n");
        rs = false;
    }
    return rs;
}

function isNull($value) {
    if ($value == "" || $value.replace(/\s+/g, "") == "")
        return true;
    return false;
}

function checkphototype($file) {
    var str = $file.value
    strs = str.toLowerCase();
    lens = strs.length;
    extname = strs.substring(lens - 4, lens);
    if (extname != ".jpg" && extname != ".gif")
    {
        $.messager.alert("错误","图片格式应为jpg/gif，请重新输入");
        return false;
    }
    return true;
}
function checkCmcc($ele)
{
    var str = $ele.value;
    if (isNull(str))  return true;
    if (/^\d+$/.test(str))
    {
        if (str.length == 11) {
            strs = str.toLowerCase();
            lens = strs.length;
            extname = strs.substring(0, 3);
            if (extname != "150" && extname != "134" && extname != "135" && extname != "136" && extname != "137" && extname != "138" && extname != "139" && extname != "158" && extname != "159")
            {
                $.messager.alert("错误","手机号码只能为移动号码，包括134-139，以及150、158、159的号段\n");
                return false;
            }
            return true;
        } else {
            $.messager.alert("错误","手机号码不正确\n");
            return false;
        }
    }
    else {
        $.messager.alert("错误","手机号码不正确\n");
        return false;
    }
}
function checkAllCmcc($bv, $ele) {
    var b = true;
    b = checkMobile($ele);
    if ($bv && !b)$bv = false;
    return $bv;
}
function LTrim(str)
    /*
     PURPOSE: Remove leading blanks from our string.
     IN: str - the string we want to LTrim
     */
{
    var whitespace = new String(" 　\t\n\r");

    var s = new String(str);

    if (whitespace.indexOf(s.charAt(0)) != -1) {
        // We have a string with leading blank(s)...

        var j = 0, i = s.length;

        // Iterate from the far left of string until we
        // don't have any more whitespace...
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1)
            j++;

        // Get the substring from the first non-whitespace
        // character to the end of the string...
        s = s.substring(j, i);
    }
    return s;
}

/*
 ==================================================================
 RTrim(string) : Returns a copy of a string without trailing spaces.
 ==================================================================
 */
function RTrim(str)
    /*
     PURPOSE: Remove trailing blanks from our string.
     IN: str - the string we want to RTrim

     */
{
    // We don't want to trip JUST spaces, but also tabs,
    // line feeds, etc.  Add anything else you want to
    // "trim" here in Whitespace
    var whitespace = new String(" 　\t\n\r");

    var s = new String(str);

    if (whitespace.indexOf(s.charAt(s.length - 1)) != -1) {
        // We have a string with trailing blank(s)...

        var i = s.length - 1;       // Get length of string

        // Iterate from the far right of string until we
        // don't have any more whitespace...
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1)
            i--;


        // Get the substring from the front of the string to
        // where the last non-whitespace character is...
        s = s.substring(0, i + 1);
    }

    return s;
}

/*
 =============================================================
 Trim(string) : Returns a copy of a string without leading or trailing spaces
 =============================================================
 */
function Trim(str)
    /*
     PURPOSE: Remove trailing and leading blanks from our string.
     IN: str - the string we want to Trim

     RETVAL: A Trimmed string!
     */
{
    return RTrim(LTrim(str));
}

function checkLength2(content,number){
	num=content.value.length;
	var arr=content.value.match(/[^\x00-\x80]/ig)
	if(arr!=null)num+=arr.length;
	if(num>number){
		$.messager.alert("错误","输入内容过长,请控制在"+number+"个字符内");
		content.value=getStrByLen(content.value,number);
		content.focus();
	} 
}