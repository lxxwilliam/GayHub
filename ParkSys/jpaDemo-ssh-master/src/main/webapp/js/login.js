$(function(){
    initLoginPanel();

    $(window).resize(function() {
        initLoginPanel();
    });


    $(".loginBox input").focus(function() {
        $(this).closest("li").css("background-color", "#f0f4f7");
        $(this).closest("li").find(".focusLine").show();
    });

    $(".loginBox input").blur(function() {
        $(this).closest("li").css("background-color", "#fff");
        $(this).closest("li").find(".focusLine").hide();
    });
});
function login() {

}
/**
 * 初始化登录窗口的位置
 */
function initLoginPanel() {
    var windowHeight = $(window).height();
    var windowWidth = $(window).width();

    if (windowHeight < 600) {   // 页面最小高度600px，窗口高度小于600置为600
        windowHeight = 600;
    }
    if (windowWidth < 1200) {
        windowWidth = 1200;
    }
    var top = (windowHeight - 400)/2;
    var left = (windowWidth - 530)/2;
    
    $(".contain").css("height", windowHeight);
    $(".login").css({"top": top, "left": left});
}
//JS光标定位到文本框字符串末尾
function setCaretPosition(tObj, sPos){
	if(tObj.setSelectionRange){
		setTimeout(function(){
			tObj.setSelectionRange(sPos, sPos);
			tObj.focus();
		}, 0);
	}else if(tObj.createTextRange){
		var rng = tObj.createTextRange();
		rng.move('character', sPos);
		rng.select();
	}

} 

$(function(){
	//粒子特效的初始化事件
	$('#particles').particleground({
	    dotColor: '#10a8d9', //点的颜色
	    lineColor: '#10a8d9' //线的颜色
	    /*density:5000, //点的最大数量
	    particleRadius:3, //粒子的半径
	    parallaxMultiplier:10, // 数字越低，视差效果越明显
	    proximity:25 //两个点间多远开始连*/
  });
})
/**
 * 输入框的获得事件
 * @param {Object} obj
 */
function focusIn(obj){
	$(obj).parents(".login-inputDiv").addClass("box-shadow");
}
/**
 * 输入框失去焦点事件
 * @param {Object} obj
 */
function focusOut(obj){
	$(obj).parents(".login-inputDiv").removeClass("box-shadow");
}
/**
 * 密码的可见与不可见的切换事件
 * @param {Object} obj
 */
function showPwd(obj){
	var type = $(obj).siblings("input").attr("type");
	if(type == "password"){
		$(obj).siblings("input").attr("type","text");
		$(obj).addClass("text");
	}
	else if(type == "text"){
		$(obj).siblings("input").attr("type","password");
		$(obj).removeClass("text");
	}
}