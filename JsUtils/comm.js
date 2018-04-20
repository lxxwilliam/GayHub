function getProp(object, nameObj, nullReplaceStr){
	//
	var nameStr = nameObj.key || nameObj;
	var value="";
	var names = nameStr.split(";");
	for(var j=0; j<names.length; j++){
		var name = names[j];
		var vv = getProp2(object, name);
		if(isEmpty(vv)){
			continue;
		}
		value += getProp2(object, name)+"-";
	}
	if(value.length > 0){
		value = value.substring(0, value.length-1);
	}
	if(isNotEmpty(nullReplaceStr) &&  (value.length == 0)){
		value = nullReplaceStr;
	}
	return value;
}
//最多两级,也可以是0级
function getProp2(object, name){
	if(isEmpty(name)){
		return object;
	}
	var nameArr = name.split(".");
	
	if(nameArr.length == 1){
		var v = object[nameArr[0]+""];
		if(v == undefined || v == null){
			return "";
		}else{
			//
			var v2 = object[nameArr[0]+""];
			return object[nameArr[0]+""];
		}
	}else if(nameArr.length == 2){
		if(isEmpty(object[nameArr[0]+""]) || isEmpty(object[nameArr[0]+""][nameArr[1]+""] )){
			return "";
		}else{
			//
			var v2 =  object[nameArr[0]+""][nameArr[1]+""];
			return object[nameArr[0]+""][nameArr[1]+""];
		}
		
	}
	
}


/**
* 公用判断文本内容为空
 * @param obj
 * @returns {Boolean}
 */
function isEmpty(obj){
	return obj == null || obj == undefined  || trim(obj) == "" || !obj;
}

/**
 * 公用判断文本内容不为空
 * @param obj
 * @returns {Boolean}
 */
function isNotEmpty(obj){
	return !isEmpty(obj);
}

/**
 * 公用去除文本内容前后空格
 * 去除其他的非法输入：|(&(quot|#34);)|(&(iexcl|#161);)|(&(amp|#38);)|(<!--.*-->)|(&(cent|#162);)|(&(pound|#163);)|(&(copy|#169);)|(xp_cmdshell)
 * 须依赖jQuery组件
 * @param obj
 * @returns {Boolean}
 */
//(<(.[^>]*)>)  表示<***> 的格式
//console.log("**"+trim3("   公用去除<script >script</script>  <dffdsfds> <script >script*********</script>文本“ ”'内容''前&quot;后\'空格    \" \"     "));
//console.log("**"+trim3('[{"profession":1,"team":1},{"profession":3,"team":1},{"profession":4,"team":1},{"profession":2,"team":1}]'));
//console.log("**"+trim3('{"recordName":"","recordDetail":[{"colSort":0,"colName":"序\' \"号"}'));
function trim(obj){
	return  $.trim(obj);
}

/**
 * 公用去除文本内容前后空格
 * 须依赖jQuery组件
 * @param obj
 * @returns {Boolean}
 */
function trimSpace(obj){
	return  $.trim(obj);
}

/**
 * 1.公用去除html文本内容前后空格包括&nbsp;类型的空格
 * 2.去除所有的非<i 开头的标签
 * 3.却掉非法字符
 * @param html
 * @returns {string}
 */
//console.log(trimHtml2('  &nbsp;<br/>         &nbsp;  <br/>  &nbsp;<img src="data" alt=""><span style="asfsfsd" >img <p style="" class="" >phtml phtml</p>img img《some》<some></span><img src="data" alt="" > >  &nbsp;<br/>         &nbsp;  <br/>  &nbsp;'));
function trimHtml2(obj){
	//
	var text = /((<([^i]).*?>)|(<\/.*?>))+/g;
	var text1 = trimHtml($.trim(obj).replace(text, "" ));
	return trim(text1);
}

/**
 * 公用去除html文本内容前后空格包括&nbsp;类型的空格
 * @param html
 * @returns {string}
 */
/*
 * 正则表达式的一些小常识：
 * ^ 表示字符串的开头位置，在一个正则表达式中只能使用一次，只修饰与之右边相邻的第一个表达式，^(&nbsp;)+ ^修饰了(&nbsp;)+
 * $ 表示字符串的结尾位置，在一个正则表达式中只能使用一次，只修饰与之左边相邻的第一个表达式，(&nbsp;)+$ $修饰了(&nbsp;)+
 * + 修饰匹配次数为至少 1 次，可以修饰 （）、[] 等表达式，只修饰与之左边相邻的表达式，do[es]+, 可以匹配doe, dos, does, dose
 * | 左右两边表达式之间 "或" 关系，可用在两个表达式之间
 */
//console.log("xx"+trimHtml("  &nbsp;<br/>         &nbsp;  <br/>  &nbsp; <br/>  <span class='sds' style='sdfsd'>bs检查内容&nbsp;&nbsp;<br/><br/>      bs</span><img src=''>  <br/>    &nbsp;    &nbsp;<br/>         &nbsp;  <br/>  &nbsp; <br/>  ")+"xx");
function trimHtml(html){
	if(isNotEmpty(html)){
		var pattern = /^((&nbsp;)|(<br\/>)|[\s\uFEFF\xA0])+|(&nbsp;|<br\/>|[\s\uFEFF\xA0])+$/g;
		html =  html.replace(pattern, "");
	}else{
		html = "";
	}
	return html;
}


/**
 * 判断是否输入正整数
 * inputId 对应文本框的ID
 * true : 是正整数
 */
function isPositiveInt(inputId){
	var reg = /^\+?[1-9][0-9]*$/;
	var val = $("#"+inputId).val();
	if (!reg.test(val) || val == "" || val == null) {
	    $("#"+inputId).val("").focus();
	    return false;
	}else{
		return true;
	}
}


/**
 * 判断是否输入正整数
 * val 待判断的值
 * true : 是正整数
 */
function isPositiveIntVal(val){
	var reg = /^\+?[1-9][0-9]*$/;
	if (isEmpty(val) || !reg.test(val) ) {
	    return false;
	}else{
		return true;
	}
}

/**
 * 判断是否输入正整数
 * @param event
 */
function keyUp(event){
	if(event.value.length==1){event.value=event.value.replace(/[^1-9]/g,'')}
	
	else{event.value=event.value.replace(/\D/g,'')}
}

/**
 * 不是正整数，就取消输入的内容
 */
function afterPaste(event){
	if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}
	
	else{this.value=this.value.replace(/\D/g,'')}
}
/**
 * 成功提示
 */
function showSuccess(text){
	showHints('success', text);
}

/**
 * 警告提示
 */
function showWarn(text){
	showHints('warning', text);
}

/**
 * 失败提示
 */
function showFailure(text){
	showHints('failure', text);
}

/**
 * 提示样式选择 
 */
function showHints(className, text) {
    $("#hintInfo").html("").text(text);
    $(".hints").addClass(className);
    $(".hints").animate({"right": "0px"}, 1000);
    var timer = setTimeout("hideHints('" + className + "')",3000);
}

/**
 * 提示栏隐藏 
 */
function hideHints(className) {
    $(".hints").animate({"right": "-151px"}, 1000);
    $(".hints").removeClass(className);
}

/**
 * 判断开始日期和结束日期大小(仅判断年-月-日这种格式)
 * @param startDate 开始日期
 * @param endDate 结束日期
 */
function determineStartAndStopDate(startDate,endDate,splitStr){
	 if(splitStr == undefined){
		 splitStr = "-";
	 }
	var starStr = startDate.split(splitStr);
	var endStr = endDate.split(splitStr);
	var DateStart = new Date(starStr[0], parseInt(starStr[1]), parseInt(starStr[2]));
	var DateEnd = new Date(endStr[0], parseInt(endStr[1]), parseInt(endStr[2])); 
	if(DateStart > DateEnd){
		alertDialog("开始日期不能大于结束日期!");
		return false;
	}
	return true;
}


/**
 * 判断输入正整数，不是正整数时弹出提示信息.
 * @param val 	待验证的值
 * @param str	提示信息
 * @returns {Boolean}
 */
function validateNum(val,str){
	var reg = /^\+?[1-9][0-9]*$/;
	if (isEmpty(trim(val)) || !reg.test(trim(val))) {
		alertDialog(str);
	    return false;
	}else{
		return true;
	}
}


/**
 * 关闭当前windows的界面
 */

function closeForm(){ 
	window.close(); 
}

function validateValue(textbox, IllegalString){
	var textboxvalue = textbox.value;
	var trimstr = /[:;：；]/g;
	textbox.value = textboxvalue.replace(trimstr ,'');
} 

/**
 * 对双引号进行转义
 * @param str 要转义的文本
 * @returns 转义后的文本
 */
function zhuanYiStr(str){
	var strNew = str.replace(/\"/g,"&quot;");

	return strNew;
}

function validateStarEndDate(formid, startDateId, endDateId, splitStr){
	var result = {};
	var startDate = $("#"+formid+" #"+startDateId).val();
	var endDate = $("#"+formid+" #"+endDateId).val();
	result.flag = true;
	
	if(splitStr == undefined){
		 splitStr = "-";
	 }
	
	if(isNotEmpty(startDate) && isNotEmpty(endDate)){
		var starStr = startDate.split(splitStr);
		var endStr = endDate.split(splitStr);
		var DateStart = new Date(starStr[0], parseInt(starStr[1]), parseInt(starStr[2]));
		var DateEnd = new Date(endStr[0], parseInt(endStr[1]), parseInt(endStr[2])); 
		if(DateStart > DateEnd){
			result.flag = false;
			result.msg = "开始日期不能大于结束日期!";
		}
	}
	return result;
}



