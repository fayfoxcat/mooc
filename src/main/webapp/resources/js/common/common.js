/*登录验证码*/
function changeVerifyCode(img){
	img.src = "/mooc/Kaptcha?"+Math.floor(Math.random()*100);
}

/*邮箱发送验证码*/
$(function () {
	//获取邮箱验证码
	var validCode = true;
	$(document).on('click','.btn-code',function () {
		var formData = new FormData();
		var email = $('.email').val();
		if(!util.emailValid(email)){
			$(".email").css({ "border":"solid red 1px" });
			toast("id","表单提示","邮箱格式不正确！")
			return;
		}
		formData.append("email",email);
		var time = 60;
		var code = $(this);
		if (validCode) {
			//通知后台发送邮件
			$.ajax({
				url:("/mooc/useroperator/getcode"),
				type:'POST',
				data:formData,
				contentType:false,
				processData:false,
				cache:false,
				success:function(data){
					if(data.success){
						toast("id","系统通知",data.message);
						validCode = false;
						code.addClass("msgs1");
						var t = setInterval(function () {
							time--;
							code.html(time + "秒");
							if (time === 0) {
								clearInterval(t);
								code.html("重新获取");
								validCode = true;
								code.removeClass("msgs1");
							}
						}, 1000)
					}
					else{
						$(".email").css({ "border":"solid red 1px" });
						toast("id","系统通知",data.message);
					}
				}
			});
		}
	})
});

/*登录注册模块*/
$(function () {
	$(document).ready(function () {
		var html ='<div class="modal fade" id="login-register" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">' +
			'    <div class="modal-dialog modal-dialog-centered" role="document">\n' +
			'        <div class="modal-content" style="width: 420px">\n' +
			'            <div class="modal-header">\n' +
			'                <button class="btn btn-login">登录</button>\n' +
			'                <button class="btn btn-register">注册</button>\n' +
			'                <button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
			'                    <span aria-hidden="true">×</span>\n' +
			'                </button>\n' +
			'            </div>\n' +
			'            <!--登录模块-->\n' +
			'            <div class="modal-body login-modal" style="display: block;">\n' +
			'                <div class="list">\n' +
			'                    <div class="input-group mb-3">\n' +
			'                        <div class="input-group-prepend">\n' +
			'                            <span class="input-group-text">邮箱</span>\n' +
			'                        </div>\n' +
			'                        <input type="text" id="email" class="form-control" placeholder="请输入邮箱">\n' +
			'                    </div>\n' +
			'                    <div class="input-group mb-3">\n' +
			'                        <div class="input-group-prepend">\n' +
			'                            <span class="input-group-text">密码</span>\n' +
			'                        </div>\n' +
			'                        <input type="password" id="password" class="form-control" placeholder="请输入密码">\n' +
			'                    </div>\n' +
			'                    <div class="input-group mb-3">\n' +
			'                        <div class="input-group-prepend">\n' +
			'                            <span class="input-group-text">验证码</span>\n' +
			'                        </div>\n' +
			'                        <input type="text" id="code" class="form-control" placeholder="请输入验证码" value="">\n' +
			'                        <img id="cpatcha-img" alt="点击更换" title="点击更换" onclick="changeVerifyCode(this)" src="/mooc/Kaptcha" style="height: 38px">' +
			'                    </div>\n' +
			'                </div>\n' +
			'                <span class="forget-password">忘记密码</span>\n' +
			'                <button class="btn btn-success sub-login">登录</button>\n' +
			'            </div>\n' +
			'            <!--注册模块-->\n' +
			'            <div class="modal-body register-modal" style="display: none;">\n' +
			'                <div class="list">\n' +
			'                    <div class="input-group mb-3">\n' +
			'                        <div class="input-group-prepend">\n' +
			'                            <span class="input-group-text">昵称</span>\n' +
			'                        </div>\n' +
			'                        <input type="text" class="form-control username" placeholder="请输入昵称">\n' +
			'                    </div>\n' +
			'                    <div class="input-group mb-3">\n' +
			'                        <div class="input-group-prepend">\n' +
			'                            <span class="input-group-text">密码</span>\n' +
			'                        </div>\n' +
			'                        <input type="password" class="form-control password" placeholder="请输入密码">\n' +
			'                    </div>\n' +
			'                    <div class="input-group mb-3">\n' +
			'                        <div class="input-group-prepend">\n' +
			'                            <span class="input-group-text">邮箱</span>\n' +
			'                        </div>\n' +
			'                        <input type="text" class="form-control email" placeholder="请输入邮箱">\n' +
			'                    </div>\n' +
			'                    <div class="input-group mb-3">\n' +
			'                        <div class="input-group-prepend">\n' +
			'                            <span class="input-group-text">验证</span>\n' +
			'                        </div>\n' +
			'                        <input type="text" class="form-control code" placeholder="请输入验证码" >\n' +
			'                        <button class="btn-code">获取验证码</button>\n' +
			'                    </div>\n' +
			'                </div>\n' +
			'                <button class="btn btn-success submit-register">注册</button>\n' +
			'            </div>\n' +
			'        </div>\n' +
			'    </div>\n' +
			'</div>';
		$('body').prepend(html);
	});

	/*触发事件*/
	$(document).on('click','.btn-login',login);
	$(document).on('click','.btn-register',register);
	/*登录、注册模态框触发事件*/
	function login() {
		$('#login-register').modal('show');
		$('.login-modal').css({
			"display":"block"
		});
		$('.register-modal').css({
			"display":"none"
		});
	}
	function register() {
		$('#login-register').modal('show');
		$("#cpatcha-img").click();
		$('.register-modal').css({
			"display":"block"
		});
		$('.login-modal').css({
			"display":"none"
		})
	}

	/*邮箱*/
	$(document).on('blur','#email',function() {
		if(util.emailValid($(this).val())){
			$(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
		}else{
			$(this).css({ "border":"solid red 1px"})
		}
	});
	/*密码*/
	$(document).on('blur','#password',function() {
		if(util.pwdValid($(this).val())){
			$(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
		}else{
			$(this).css({ "border":"solid red 1px"})
		}
	});

	/*登录校验*/
	$(document).on('click','.sub-login',function(){
		/*账户（邮箱）、密码*/
		var account = $("#email").val();
		var password= $("#password").val();
		var verifyCodeActual = $("#code").val();
		if(!util.emailValid(account)){
			$("#email").css({ "border":"solid red 1px" });
			toast("id","表单提示","邮箱格式不正确！")
			return;
		}
		if(!util.pwdValid(password)){
			$("#password").css({ "border":"solid red 1px" });
			toast("id","表单提示","密码必须是字母、符号、数字至少2种，6-16位！")
			return;
		}
		if(!verifyCodeActual){
			$("#code").css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
			toast("id","系统通知","验证码不能为空");
			$("#cpatcha-img").click();
			return;
		}
		//生成表单对象，用于接收参数并传递给后台
		var formData = new FormData();
		formData.append("account",account);
		formData.append("password",password);
		formData.append("verifyCodeActual",verifyCodeActual);

		//3.3、使用ajax将数据传递给后台
		$.ajax({
			url:("/mooc/useroperator/login"),
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			success:function(data){
				if(data.success){
					$('#login-register').modal('hide')
					toast("id","系统通知",data.message);
					if (data.count>0){
						toast("id","系统通知","您当前有"+data.count+"消息未读，请到个人中心查看");
					}
					try{
						if (typeof(eval(getuserstate()))==='function'){
							getuserstate();
						}
					}catch (e) {

					}

				}
				else{
					toast("id","系统通知",data.message);
					$("#code").val('');
					$("#cpatcha-img").click();
				}
			}
		});
	});

	/*昵称*/
	$(document).on('blur','.username',function() {
		if(util.zcValid($(this).val())){
			$(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
		}else{
			$(this).css({ "border":"solid red 1px"})
		}
	});

	/*密码*/
	$(document).on('blur','.password',function() {
		if(util.pwdValid($(this).val())){
			$(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
		}else{
			$(this).css({ "border":"solid red 1px"})
		}
	});

	/*邮箱*/
	$(document).on('blur','.email',function() {
		if(util.emailValid($(this).val())){
			$(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
		}else{
			$(this).css({ "border":"solid red 1px"})
		}
	});
	/*注册校验*/
	$(document).on('click','.submit-register',function(){
		/*账户（邮箱）、密码*/
		var username = $(".username").val();
		var account = $(".email").val();
		var password= $(".password").val();
		var verifyCodeActual = $(".code").val();
		if(!util.zcValid(username)){
			$(".username").css({ "border":"solid red 1px" });
			toast("id","表单提示","昵称必须是中文、字母及-、_组合，2-12位！")
			return;
		}
		if(!util.pwdValid(password)){
			$(".password").css({ "border":"solid red 1px" });
			toast("id","表单提示","密码必须是字母、符号、数字至少2种，6-16位！")
			return;
		}
		if(!util.emailValid(account)){
			$(".email").css({ "border":"solid red 1px" });
			toast("id","表单提示","邮箱格式不正确！")
			return;
		}
		if(!verifyCodeActual){
			toast("id","系统通知","验证码不能为空");
			return;
		}
		//生成表单对象，用于接收参数并传递给后台
		var formData = new FormData();
		formData.append("username",username);
		formData.append("account",account);
		formData.append("password",password);
		formData.append("verifyCodeActual",verifyCodeActual);

		//3.3、使用ajax将数据传递给后台
		$.ajax({
			url:("/mooc/useroperator/register"),
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			success:function(data){
				if(data.success){
					login();
					toast("id","系统通知",data.message);
				}
				else{
					toast("id","系统通知",data.message);
				}
			}
		});
	});

});
//安全退出
function exit(){
	$.ajax({
		url:("/mooc/useroperator/exit"),
		type:'POST',
		contentType:false,
		processData:false,
		cache:false,
		success:function(data){
			if(data.success){
				toast("id","系统通知",data.message);
			}
			else{
				toast("id","系统通知",data.message);
			}
		}
	});
	window.location.href = "/mooc/";
}