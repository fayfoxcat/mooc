/*获取地址栏的参数*/
function getQueryString(name){
	var reg = new RegExp("(^|&)" + name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null){
		return decodeURIComponent(r[2]);
	}
	return '';
}

/*格式化时间函数*/
Date.prototype.Format = function(fmt){
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
		// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]): (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};

/*获取当前时间及前n天*/
function nearbyDate(day){
	var array = [];
	var currentDate = new Date();
	var d = 24*60*60*1000;
	for(var i = 0 ;i<=day;i++){
		array.push(new Date(currentDate.getTime()-d*i).Format("MM-dd"));
	}
	return array.reverse();
}

/*toast通知*/
var confirm = true;
function content() {
	var content = '<div aria-live="polite" id = "toast-content" aria-atomic="true"'
		+'style="position: absolute; z-index: 1051; top: 100px; right: 20px;">'
		+'</div>';
	$('body').prepend(content);
	confirm = false;
}
function toast(id,head,message) {
	var img = '/mooc/resources/images/tip.png';
	if (confirm){
		content();
	}
	var html ='<div id="'
		+id
		+'"role="alert"'
		+'class="toast" data-autohide="true" data-delay="10000"'
		+'style="position: relative;width: 250px;">'
		+'<div class="toast-header"><img src="'
		+img
		+'" width="20" height="20" class="rounded mr-2">'
		+'<strong class="mr-auto">'
		+head
		+'</strong><button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">'
		+'<span aria-hidden="true">&times;</span>'
		+'</button></div><div class="toast-body">'
		+message
		+'</div></div>';
	$('#toast-content').prepend(html);
	$("#"+id).toast('show');
}

/**
 * 通用类
 */
UTIL = function() {};
UTIL.prototype = {
	//判断是否为空,如果为空返回true，否则返回false
	isEmpty : function(text){
		if(text === undefined || text == null || text === '' || text === 'null' || text === 'undefined'){
			return true;
		}else{
			text = text.replace(/(\s*$)/g, '');
			if(text === ''){
				return true;
			}
		}
		return false;
	},
	//数字正则表达式，验证通过返回 true；
	numValid : function(text){
		var patten = new RegExp(/^[0-9]+$/);
		return patten.test(text);
	},
	//英文、数字正则表达式
	enNumValid : function(text){
		var patten = new RegExp(/^[a-zA-Z0-9]+$/);
		return patten.test(text);
	},
	//英文、数字、-、_验证
	cValid : function(text){
		var patten = new RegExp(/^[a-zA-Z][\w-_]{5,19}$/);
		return patten.test(text);
	},
	//中文、英文、数字、-、_验证
	zcValid : function(text){
		var patten = RegExp(/^[\u4E00-\u9FA5A-Za-z0-9_.-]{2,16}$/);
		return patten.test(text);
	},

	//中文、数字、-验证
	numZnValid : function(text){
		var patten = RegExp(/^[\u4E00-\u9FA50-9-]{2,16}$/);
		return patten.test(text);
	},
	//以字母开头正则表达式，英文、数字、-、_验证
	enStartValid : function(text){
		var patten = new RegExp(/^[a-zA-Z][a-zA-Z0-9_]*$/);
		return patten.test(text);
	},
	//中文_验证
	cnValid : function(text){
		var patten = RegExp(/^[\u4E00-\u9FA5]{2,5}$/);
		return patten.test(text);
	},
	//时间格式
	timeValid : function(text){
		var patten = RegExp(/^([0-5][0-9]):([0-5][0-9])$/);
		return patten.test(text);
	},

	//mobile
	mobileValid : function(text){
		var patten = RegExp(/^1\d{10}$/);
		return patten.test(text);
	},
	//url验证
	urlValid : function(text){
		var patten = RegExp( /((http|https)\:\/\/)?[a-zA-Z0-9\.\/\?\:@\-_=#]+\.([a-zA-Z0-9\&\.\/\?\:@\-_=#])*/);
		return patten.test(text);
	},
	//email
	emailValid : function(text){
		var patten = RegExp(/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/);
		return patten.test(text);
	},
	//字母&符号&数字至少2种;8-16位数
	pwdValid : function(text){
		var patten = RegExp(/^((?=.*\d)(?=.*\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{6,16}$/);
		return patten.test(text);
	},
	//图片验证
	photoValid : function(text){
		var photos = ['.jpg','.png','.jpeg'];
		photoExt=text.substring(text.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		var flag = false;
		for(var i = 0; i < photos.length; i++){
			if(photos[i] === photoExt){
				flag = true;
				break;
			}
		}
		return flag;
	},
	//文档excel验证
	excelValid : function(text){
		var excels = ['.xlsx'];
		excelExt=text.substr(text.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		var flag = false;
		for(var i = 0; i < excels.length; i++){
			if(excels[i] == excelExt){
				flag = true;
				break;
			}
		}
		return flag;
	},
	
	//json数据的日期格式化
	jsonDateFormat : function(value,format){
		if (value == null || value === '') {
			return '';
		} else {
			return this.dateFormat(new Date(value.time),format);
		}
	},
	
	/**
	 * 客户端js实现图片预览
	 * fileElId 选择文件的input type=file的id
	 * imgElId 预览的image 的id
	 */
	previewUploadImg : function(fileElId,imgElId){
		var file = document.getElementById(fileElId);
		var pic = document.getElementById(imgElId);
	    var isIE = navigator.userAgent.match(/MSIE/)!= null;
	    var isIE6 = navigator.userAgent.match(/MSIE 6.0/)!= null;
	    if(isIE) {
	    	file.select();
	        var reallocalpath = document.selection.createRange().text;
	        // IE6浏览器设置img的src为本地路径可以直接显示图片
	        if (isIE6) {
	            pic.src = reallocalpath;
	        }else {
	            // 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
	            pic.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src=\"" + reallocalpath + "\")";
	            // 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
	            pic.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
	        }
	     }else{
	    	var reader = new FileReader();
		    reader.readAsDataURL(file.files[0]);
		    reader.onload = function(e){
		    	pic.src = this.result;
		    };
	    }
	}
};
util = new UTIL();

//分页按钮插件
// 初始化分页
function createPage(el) {
	var element = $(el)
	// 创建回到顶部
	element.append('<a class="prev_top_btn page_common_btn_style"></a>')
	// 添加图标
	$(el + ' .prev_top_btn').append('<span class="iconfont icon-most-left"></span>')
	// 创建上一步按钮
	element.append('<a class="prev_btn page_common_btn_style"></a>')
	// 添加图标
	$(el + ' .prev_btn').append('<span class="iconfont icon-zuo"></span>')
	// 创建页码按钮框
	element.append('<div class="page_btn_box"></div>')
	// 创建下一步按钮
	element.append('<a class="next_btn page_common_btn_style"></a>')
	// 添加图标
	$(el + ' .next_btn').append('<span class="iconfont icon-you"></span>')
	// 创建跳到最后一页按钮
	element.append('<a class="next_bottom_btn page_common_btn_style"></a>')
	// 添加图标
	$(el + ' .next_bottom_btn').append('<span class="iconfont icon-most-right"></span>')
	// 返回 el
	return {
		el: el, //选择器
		html: $(el).html(), //htnl 内容
	}
}

// 设置分页
function Page(_ref) {
	var pageSize = _ref.pageSize,
		pageTotal = _ref.pageTotal,
		curPage = _ref.curPage,
		id = _ref.id,
		getPage = _ref.getPage,
		showPageTotalFlag = _ref.showPageTotalFlag,
		showSkipInputFlag = _ref.showSkipInputFlag,
		pageAmount = _ref.pageAmount,
		dataTotal = _ref.dataTotal;
	if(!pageSize){
		pageSize = 0
	};
	if(!pageSize){
		pageSize = 0
	};
	if(!pageTotal){
		pageTotal = 0
	};
	if(!pageAmount){
		pageAmount = 0
	};
	if(!dataTotal){
		dataTotal = 0
	};
	this.pageSize = pageSize || 5; //分页个数
	this.pageTotal = pageTotal; //总共多少页
	this.pageAmount = pageAmount; //每页多少条
	this.dataTotal = dataTotal; //总共多少数据
	this.curPage = curPage || 1; //初始页码
	this.ul = document.createElement('ul');
	this.id = id;
	this.getPage = getPage;
	this.showPageTotalFlag = showPageTotalFlag || false; //是否显示数据统计
	this.showSkipInputFlag = showSkipInputFlag || false; //是否支持跳转
	if(dataTotal >0 &&pageTotal>0){
		this.init();
	}else{
		console.log("总页数或者总数据参数不对")
	}
};
// 给实例对象添加公共属性和方法
Page.prototype = {
	init: function init() {
		var pagination = document.getElementById(this.id);
		pagination.innerHTML = '';
		this.ul.innerHTML = '';
		pagination.appendChild(this.ul);
		var that = this;
		//首页
		that.firstPage();
		//上一页
		that.lastPage();
		//分页
		that.getPages().forEach(function (item) {
			var li = document.createElement('li');
			if (item == that.curPage) {
				li.className = 'active';
			} else {
				li.onclick = function () {
					that.curPage = parseInt(this.innerHTML);
					that.init();
					that.getPage(that.curPage);
				};
			}
			li.innerHTML = item;
			that.ul.appendChild(li);
		});
		//下一页
		that.nextPage();
		//尾页
		that.finalPage();

		//是否支持跳转
		if (that.showSkipInputFlag) {
			that.showSkipInput();
		}
		//是否显示总页数,每页个数,数据
		if (that.showPageTotalFlag) {
			that.showPageTotal();
		}
	},
	//首页
	firstPage: function firstPage() {
		var that = this;
		var li = document.createElement('li');
		li.innerHTML = '首页';
		this.ul.appendChild(li);
		li.onclick = function () {
			var val = parseInt(1);
			that.curPage = val;
			that.getPage(that.curPage);
			that.init();
		};
	},
	//上一页
	lastPage: function lastPage() {
		var that = this;
		var li = document.createElement('li');
		li.innerHTML = '<';
		if (parseInt(that.curPage) > 1) {
			li.onclick = function () {
				that.curPage = parseInt(that.curPage) - 1;
				that.init();
				that.getPage(that.curPage);
			};
		} else {
			li.className = 'disabled';
		}
		this.ul.appendChild(li);
	},
	//分页
	getPages: function getPages() {
		var pag = [];
		if (this.curPage <= this.pageTotal) {
			if (this.curPage < this.pageSize) {
				//当前页数小于显示条数
				var i = Math.min(this.pageSize, this.pageTotal);
				while (i) {
					pag.unshift(i--);
				}
			} else {
				//当前页数大于显示条数
				var middle = this.curPage - Math.floor(this.pageSize / 2),
					//从哪里开始
					i = this.pageSize;
				if (middle > this.pageTotal - this.pageSize) {
					middle = this.pageTotal - this.pageSize + 1;
				}
				while (i--) {
					pag.push(middle++);
				}
			}
		} else {
			console.error('当前页数不能大于总页数');
		}
		if (!this.pageSize) {
			console.error('显示页数不能为空或者0');
		}
		return pag;
	},
	//下一页
	nextPage: function nextPage() {
		var that = this;
		var li = document.createElement('li');
		li.innerHTML = '>';
		if (parseInt(that.curPage) < parseInt(that.pageTotal)) {
			li.onclick = function () {
				that.curPage = parseInt(that.curPage) + 1;
				that.init();
				that.getPage(that.curPage);
			};
		} else {
			li.className = 'disabled';
		}
		this.ul.appendChild(li);
	},
	//尾页
	finalPage: function finalPage() {
		var that = this;
		var li = document.createElement('li');
		li.innerHTML = '尾页';
		this.ul.appendChild(li);
		li.onclick = function () {
			var yyfinalPage = that.pageTotal;
			var val = parseInt(yyfinalPage);
			that.curPage = val;
			that.getPage(that.curPage);
			that.init();
		};
	},
	//是否支持跳转
	showSkipInput: function showSkipInput() {
		var that = this;
		var li = document.createElement('li');
		li.className = 'totalPage';
		var span1 = document.createElement('span');
		span1.innerHTML = '跳转到';
		li.appendChild(span1);
		var input = document.createElement('input');
		input.setAttribute("type","number");
		input.onkeydown = function (e) {
			var oEvent = e || event;
			if (oEvent.keyCode == '13') {
				var val = parseInt(oEvent.target.value);
				if (typeof val === 'number' && val <= that.pageTotal && val>0) {
					that.curPage = val;
					that.getPage(that.curPage);
				}else{
					alert("请输入正确的页数 !")
				}
				that.init();
			}
		};
		li.appendChild(input);
		var span2 = document.createElement('span');
		span2.innerHTML = '页';
		li.appendChild(span2);
		this.ul.appendChild(li);
	},
	//是否显示总页数,每页个数,数据
	showPageTotal: function showPageTotal() {
		var that = this;
		var li = document.createElement('li');
		li.innerHTML = '共&nbsp' + that.pageTotal + '&nbsp页';
		li.className = 'totalPage';
		this.ul.appendChild(li);
		var li2 = document.createElement('li');
		li2.innerHTML = '每页&nbsp' + that.pageAmount + '&nbsp条';
		li2.className = 'totalPage';
		this.ul.appendChild(li2);
		var li3 = document.createElement('li');
		li3.innerHTML = '合计&nbsp' + that.dataTotal + '&nbsp条数据';
		li3.className = 'totalPage';
		this.ul.appendChild(li3);
	}
};

