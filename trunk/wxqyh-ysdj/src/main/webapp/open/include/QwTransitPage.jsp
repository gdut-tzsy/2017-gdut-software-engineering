<%--
  Created by IntelliJ IDEA.
  User: ken
  Date: 2017/5/15
  Time: 17:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <script>
        var _hmt = _hmt || [];
        (function() {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?6abcc5eeee320072f7a9ed10e79be5c1";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
        var _vds = _vds || [];
        window._vds = _vds;
        (function(){
            _vds.push(['setAccountId', '93fa6a13b11eb6a5']);
            (function() {
                var vds = document.createElement('script');
                vds.type='text/javascript';
                vds.async = true;
                vds.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'dn-growing.qbox.me/vds.js';
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(vds, s);
            })();
        })();
        window.onload = function(){
            var url="${param.url}";
            if(url){
                if(url.indexOf("http://")>-1){
                    window.location.href=url;
                }else{
                    window.location.href="http://"+url;
                }
            }
        };
    </script>
</head>
<body>
</body>
</html>
