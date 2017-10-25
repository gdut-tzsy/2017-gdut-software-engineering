(function ($) {
    $.fn.tree = function (options) {
        var defaults = {
            nodes:"",
            showall:true
        };
        var options = $.extend(defaults, options);
        this.each(function () {
            var nodes = options.nodes ? options.nodes : "",
                    tree_node = "",
                    detail = "",
                    tree = "";
            obj = $(this);

            for (var list in nodes) {
                detail = nodeList(nodes[list], list, options.showall);
                tree_node += "<li class='node_" + list + "'>" + detail + "</li>";
            }
            tree = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> <tr> <td valign=\" top \" class=\"left \" id=\"left \"><div class='menu'><ul >" + tree_node + "</ul></div></td></tr></table>";
            $(this).html(tree);
        });

        function nodeList(node, list, showall) {
            var nodes = node,
                    tree_detail = "",
                    tree_common = "",
                    tree_node = "",
                    icon = nodes.icon ? ("<img src=\"" + baseURL + nodes.icon + "\"></img>" ): "",
                    title = nodes.title ? nodes.title : "",
                    titleurl = nodes.titleurl ? baseURL + nodes.titleurl : "#",
                    nodelist = nodes.nodes ? nodes.nodes : "";

            var num = list;
            if(titleurl.indexOf("?")>0)titleurl+="&dqdp_csrf_token="+dqdp_csrf_token;
            else if( titleurl!="#"&&titleurl!="javascript:"&&titleurl!="") titleurl+="?dqdp_csrf_token="+dqdp_csrf_token;
            var classnode = "node_" + num;
            var nodehref = "<a href='" + titleurl +(titleurl=="#"?"'":"' target='mainFrame' ")+">" + title + "</a>";
            if (nodelist) {
                for (var list in nodelist) {
                    var nums = num + "_" + list,
                            nodes = nodelist[list].node ? nodelist[list].node : "";
                    tree_common += nodeList(nodelist[list], nums, showall);
                    classnode = "node_" + nums;
                    tree_common = showall ? "<ul class=" + classnode + "><li>" + tree_common + "</li></ul>" : "<ul class='hidden " + classnode + "'><li>" + tree_common + "</li></ul>";
                    tree_node += tree_common;
                    if (nodes) {
                        return nodeList(nodes, nums, showall);
                    }
                    tree_common = "";
                }
                tree_detail = icon ? icon + nodehref : nodehref;
            } else {
                tree_detail = icon ? icon + nodehref : nodehref;
            }
            tree_detail += tree_node;
            return tree_detail;
        }

        var lastClickedObj = null;
        var isInChild = false;
        $.each($("a", obj), function () {
            var cssObj = $(this).parent();
            cssObj.bind("mouseover", function () {
                cssObj.addClass("on");
            });
            cssObj.bind("mouseout", function () {
                if(!lastClickedObj||cssObj.html()!=lastClickedObj.parent().html())
                cssObj.removeClass("on");
            });
        });
        $("a", obj).click(function () {
            if ($("ul:first", obj).length != 0) {
                $("a", obj).removeClass("fb");
                var that = $(this);
                that.nextAll("ul").slideToggle("fast");
                that.addClass("fb");
                _rollbackFrameHeight();
                if (that.parent().parent().parent().attr("class") == "menu") {
                    if (isInChild || (lastClickedObj != null && lastClickedObj.parent().html() != that.parent().html())) {
                        lastClickedObj.nextAll("ul").slideUp("fast");
                        lastClickedObj.parent().removeClass("on");
                        lastClickedObj=null;
                    }
                    lastClickedObj = that;
                    isInChild = false;
                    $(this).parent().addClass("on");
                } else {
                    isInChild = true;
                }
                return that.attr("href")!="#"&&that.attr("href")!="javascript:"&&that.attr("href")!="";
            }
            else
                return true;
        });
    }
})(jQuery); 