
//tab切换
$('#tab>li').on('click',function(){
    tabUI('on','#msg_send_content_wrap',this);
    if (! $(this).index() == 0) {
        $('.js_del_reply').hide()
    }else{
        $('.js_del_reply').show()
    }
})

//选择关键字回复类型
$('#tab > li').on('click', function(){
    var idx = $(this).index();
    if(idx==0){
        $("#messageType").val("text");
    } else if(idx==1){
        $("#messageType").val("img");
    } else{
        $("#messageType").val("news");
    }
})
