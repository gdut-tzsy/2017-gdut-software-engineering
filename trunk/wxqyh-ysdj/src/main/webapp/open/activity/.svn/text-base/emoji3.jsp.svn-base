<%@page language="java" contentType="text/html; charset=UTF-8" %>
<div id="open_comment_page" class=" hide">
    <div class="wrap_inner">
        <div class="open_comment_page foot_plus_bar" style="position: absolute;z-index: 101;width: 100%">
            <div class="open_comment_input">
                <textarea id="inputDiv" class="text_input" style="min-height: 127px;" placeholder="我也说一句..."></textarea>
            </div>
            <div class="positionInfo"  style="display:none;margin-left: 0px;">正在获取微信定位信息...</div>
            <div class="open_comment_bottom clearfix">
                <div class="fl">
                    <a id="get_smile" class="open_comment_button open_comment_emoji"></a>
                    <a id="positionId" href="javascript:showPosition()" class="open_comment_button open_comment_position" style="display: none;"></a>
                </div>
                <div class="fr">
                    <a id="sendmsg" onclick="commitComment();" class="btn">发 表</a>
                    <a id="sendmsg_spare" class="btn white_btn comment_btn" style="display:none;">发 表</a>
                </div>
            </div>
            <div id="emoji_list" class="emoji_list hide">
                <div class="emoji_list_inner">
                    <div class="emoji_list_viewport">
                        <div class="flipsnap clearfix">
                            <div class="item">
                                <ul class="clearfix">
                                    <li><a title=":smile:" href="#">:smile:</a></li>
                                    <li><a title=":blush:" href="#">:blush:</a></li>
                                    <li><a title=":heart_eyes:" href="#">:heart_eyes:</a></li>
                                    <li><a title=":grin:" href="#">:grin:</a></li>
                                    <li><a title=":joy:" href="#">:joy:</a></li>
                                    <li><a title=":yum:" href="#">:yum:</a></li>
                                    <li><a title=":sunglasses:" href="#">:sunglasses:</a></li>
                                    <li><a title=":stuck_out_tongue_closed_eyes:" href="#">:stuck_out_tongue_closed_eyes:</a></li>
                                    <li><a title=":sleeping:" href="#">:sleeping:</a></li>
                                    <li><a title=":flushed:" href="#">:flushed:</a></li>
                                    <li><a title=":relieved:" href="#">:relieved:</a></li>
                                    <li><a title=":disappointed:" href="#">:disappointed:</a></li>
                                    <li><a title=":astonished:" href="#">:astonished:</a></li>
                                    <li><a title=":rage:" href="#">:rage:</a></li>
                                    <li><a title=":sob:" href="#">:sob:</a></li>
                                    <li><a title=":neckbeard:" href="#">:neckbeard:</a></li>
                                    <li><a title=":smiling_imp:" href="#">:smiling_imp:</a></li>
                                    <li><a title=":sweat:" href="#">:sweat:</a></li>
                                    <li><a title=":fearful:" href="#">:fearful:</a></li>
                                    <li><a title=":cold_sweat:" href="#">:cold_sweat:</a></li>
                                    <li><a title=":scream:" href="#">:scream:</a></li>
                                </ul>
                            </div>
                            <div class="item">
                                <ul class="clearfix">
                                    <li><a title=":no_mouth:" href="#">:no_mouth:</a></li>
                                    <li><a title=":neutral_face:" href="#">:neutral_face:</a></li>
                                    <li><a title=":open_mouth:" href="#">:open_mouth:</a></li>
                                    <li><a title=":ghost:" href="#">:ghost:</a></li>
                                    <li><a title=":new_moon_with_face:" href="#">:new_moon_with_face:</a></li>
                                    <li><a title=":no_good:" href="#">:no_good:</a></li>
                                    <li><a title=":shit:" href="#">:shit:</a></li>
                                    <li><a title=":cupid:" href="#">:cupid:</a></li>
                                    <li><a title=":heart:" href="#">:heart:</a></li>
                                    <li><a title=":broken_heart:" href="#">:broken_heart:</a></li>
                                    <li><a title=":exclamation:" href="#">:exclamation:</a></li>
                                    <li><a title=":question:" href="#">:question:</a></li>
                                    <li><a title=":+1:" href="#">:+1:</a></li>
                                    <li><a title=":-1:" href="#">:-1:</a></li>
                                    <li><a title=":v:" href="#">:v:</a></li>
                                    <li><a title=":ok_hand:" href="#">:ok_hand:</a></li>
                                    <li><a title=":clap:" href="#">:clap:</a></li>
                                    <li><a title=":pray:" href="#">:pray:</a></li>
                                    <li><a title=":umbrella:" href="#">:umbrella:</a></li>
                                    <li><a title=":sunny:" href="#">:sunny:</a></li>
                                    <li><a title=":musical_note:" href="#">:musical_note:</a></li>
                                </ul>
                            </div>
                            <div class="item">
                                <ul class="clearfix">
                                    <li><a title=":hibiscus:" href="#">:hibiscus:</a></li>
                                    <li><a title=":rose:" href="#">:rose:</a></li>
                                    <li><a title=":four_leaf_clover:" href="#">:four_leaf_clover:</a></li>
                                    <li><a title=":beers:" href="#">:beers:</a></li>
                                    <li><a title=":cocktail:" href="#">:cocktail:</a></li>
                                    <li><a title=":wine_glass:" href="#">:wine_glass:</a></li>
                                    <li><a title=":pizza:" href="#">:pizza:</a></li>
                                    <li><a title=":spaghetti:" href="#">:spaghetti:</a></li>
                                    <li><a title=":rice:" href="#">:rice:</a></li>
                                    <li><a title=":icecream:" href="#">:icecream:</a></li>
                                    <li><a title=":birthday:" href="#">:birthday:</a></li>
                                    <li><a title=":watermelon:" href="#">:watermelon:</a></li>
                                    <li><a title=":walking:" href="#">:walking:</a></li>
                                    <li><a title=":bike:" href="#">:bike:</a></li>
                                    <li><a title=":bus:" href="#">:bus:</a></li>
                                    <li><a title=":train:" href="#">:train:</a></li>
                                    <li><a title=":bullettrain_side:" href="#">:bullettrain_side:</a></li>
                                    <li><a title=":boom:" href="#">:boom:</a></li>
                                    <li><a title=":star:" href="#">:star:</a></li>
                                    <li><a title=":zap:" href="#">:zap:</a></li>
                                    <li><a title=":syringe:" href="#">:syringe:</a></li>
                                </ul>
                            </div>

                            <div class="item">
                                <ul class="clearfix">
                                    <li><a title=":gift:" href="#">:gift:</a></li>
                                    <li><a title=":bouquet:" href="#">:bouquet:</a></li>
                                    <li><a title=":lollipop:" href="#">:lollipop:</a></li>
                                    <li><a title=":dollar:" href="#">:dollar:</a></li>
                                    <li><a title=":moneybag:" href="#">:moneybag:</a></li>
                                    <li><a title=":crown:" href="#">:crown:</a></li>
                                    <li><a title=":crystal_ball:" href="#">:crystal_ball:</a></li>
                                    <li><a title=":tophat:" href="#">:tophat:</a></li>
                                    <li><a title=":congratulations:" href="#">:congratulations:</a></li>
                                    <li><a title=":secret:" href="#">:secret:</a></li>
                                    <li><a title=":guitar:" href="#">:guitar:</a></li>
                                    <li><a title=":running:" href="#">:running:</a></li>
                                    <li><a title=":speech_balloon:" href="#">:speech_balloon:</a></li>
                                    <li><a title=":monkey_face:" href="#">:monkey_face:</a></li>
                                    <li><a title=":speak_no_evil:" href="#">:speak_no_evil:</a></li>
                                    <li><a title=":innocent:" href="#">:innocent:</a></li>

                                </ul>
                            </div>
                        </div>
                        <div class="pointer">
                            <span class="current"></span> <span></span> <span></span> <span></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(function(){
        $("#inputDiv").on('focus',function(){
            $(".commentBtnBoxBg").css("background","none");
        });

        $("#get_smile").click(function(){
            if($("#emoji_list").attr('class').indexOf('hide')<0){
                $("#emoji_list").addClass('hide');
                closeEmoji();
            }else{
                $("#emoji_list").removeClass('hide');
            }
        });

        $('.wrap_inner').click(function(e) {
            if (e.target.className == 'wrap_inner') {
                $("#wrap_main").show();
                $("#open_comment_page").hide();
            }
        });
    });
</script>
