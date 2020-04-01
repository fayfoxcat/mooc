/*登录验证码*/
function changeVerifyCode(img){
    img.src = "/mooc/Kaptcha?"+Math.floor(Math.random()*100);
}
$(function () {
    /*邮箱*/
    $(document).on('blur','#account',function() {
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
    $(document).on('click','.admin-login',function(){
        /*账户（邮箱）、密码*/
        const account = $("#account").val();
        const password = $("#password").val();
        const verifyCodeActual = $("#code").val();
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
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
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
            url:("/mooc/admin/login"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    $('#login-register').modal('hide');
                    toast("id","系统通知",data.message);
                    if (data.count>0){
                        toast("id","系统通知","您当前有"+data.count+"消息未处理，请到个消息处理页查看");
                    }
                    try{
                        if (typeof(eval(getuserstate()))==='function'){
                            getuserstate();
                        }
                    }catch (e) {
                    }
                    return window.location.href='/mooc/admin';
                }
                else{
                    toast("id","系统通知",data.message);
                    $("#cpatcha-img").click();
                }
            }
        });
    });
});