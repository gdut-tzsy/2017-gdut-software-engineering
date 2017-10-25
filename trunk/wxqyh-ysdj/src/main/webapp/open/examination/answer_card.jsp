<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/19
  Time: 9:58
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<div class="wrap" id="answerCard_div" style="display: none">
    <div class="return_bar" id="answerCard_title">

    </div>
    <div class="cardBox">
        <div class="cardTit" id="answerCard_type">
        </div>
        <ul class="cardList clearfix" id="answerCard_list">
        </ul>
        <%--<div class="cardList clearfix" id="answerCard_list">
            </div>--%>
    </div>
</div>

<script type="text/javascript">
    var card_title1=''
            +'<a href="javascript:showQuestionDiv();" class="fl c666"><i class="ic_point ic_return"></i>继续答题</a>'
            +'<span class="c333">答题卡</span>'
            +'<a href="javascript:saveQuestion();" class="fr cblue"><i class="fr ic_jiaojuan"></i>交卷</a>';
    var card_type1=''
            +'<i class="ic_yida"></i>'
            +'<span>已答</span>'
            +'<i class="ic_weida"></i>'
            +'<span>未答<b class="cred">@unAnswer</b></span>';

    var card_title2=''
            +'<span>答题卡</span>'
            +'<i class="fr ic_close" onclick="closeSheet()"></i> ';
    var card_type2=''
            +'<i class="ic_weida"></i>'
            +'<span>未答</span>'
            +'<i class="ic_yida"></i>'
            +'<span>答对</span>'
            +'<i class="ic_dacuo"></i>'
            +'<span>答错</span>';

    //每5个一行
   var CARD_LI='<li class="clearfix">@questionList</li>'
    //未答
    var weida_li='<span><a href="javascript:showQuestion(\'@index\');" class="ic_weida">@questionIndex</a></span>';
    //已答、答对
    var yida_li='<span><a href="javascript:showQuestion(\'@index\');" class="ic_yida">@questionIndex</a></span>';
    //答错
    var dacuo_li='<span><a href="javascript:showQuestion(\'@index\');" class="ic_dacuo">@questionIndex</a></span>';
</script>
