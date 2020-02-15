var signinItem = new Vue({
    el: "#signin-div",
    data: {
        emailVerify: false,
        description: "",
        emailErrorTip: "",
        emailVerifyStatus: "",
        passwordVerify: "",
        passwordConfirm: ""
    }
});

if (!window.location.href.replace(/https?:\/\/[a-zA-Z0-9.]*(:\d+)?/g, "").startsWith("/signin")) {
    window.location.href = "/signin#login";
}

var cookieStart = document.cookie.indexOf("token");
var cookieValue = null;
if (cookieStart > -1) {
    var cookieEnd = document.cookie.indexOf(';', cookieStart);
    if (cookieEnd == -1) {
        cookieEnd = document.cookie.length;
    }
    cookieValue = document.cookie.substring(cookieStart, cookieEnd);
}
// token="zhangsan@@123"
let name = cookieValue.substring(cookieValue.indexOf("\"")+1,cookieValue.indexOf("@@"));
let password = cookieValue.substring(cookieValue.indexOf("@@")+2,cookieValue.length-1);
$("#username").val(name);
$("#email").val(password);

//重置密码
function reset() {
    var name = $("#resetusername").val();
    var oldPassword = $("#old-password").val();
    var newPassword = $("#res-password").val();
    var passwordConfirm = $("#res-confirm-password").val();
    var isValid = checkPassword(newPassword, passwordConfirm);
    if (isValid) {
        $.ajax({
            url: "/user/reset",
            type: "POST",
            data: {name: name, oldPassword: oldPassword, newPassword: newPassword},
            success: function (data) {
                if (data.result == "success") {
                    alerts("密码重置成功");
                    switchToLogin();
                } else {
                    alerts(data.message);
                }
            }
        });
    } else {
        alerts("新密码和确认密码不一致");
    }
}

function register() {
    /** @namespace globalConfig.allowRegister */
    var username = $("#reusername").val();
    var password = $("#rereg-password").val();
    var passwordConfirm = $("#reconfirm-password").val();
    var canRegister = checkPassword(password, passwordConfirm);
    console.log(canRegister)
    if (canRegister) {
        $.post("/user/create", {
            name: username,
            password: password,
            isManager:0
        }, function (data) {
            layer.closeAll();
            if (data.flag == true) {
                alerts("注册成功");
                switchToLogin();
            } else {
                alerts(json.message);
            }
        });
    } else {
        alerts("有非法内容，无法提交");
    }

}

function login() {
    var name = $("#loginName").val();
    var password = $("#password").val();
    var remember = document.getElementById("remember").checked;
    if (name && password) {
        $.ajax({
            url: "/user/login", type: "PUT", data: {
                name: name,
                password: password,
                remember:remember
            }, success: function (data) {
                console.log(data)
                if (data.flag==true){
                    window.location.href = "/index";
                } else {
                    alerts("用户名或密码不正确");
                }
            }
        });
    } else {
        alerts("用户名或密码不能为空");
    }

}



function switchToRegister() {
    switchTo("none", "none", "block", "register", signinItem.emailVerify ? 30 : 26);
}

function switchToLogin() {
    switchTo("block", "none", "none", "login", 24);
}

function switchToReset() {
    switchTo("none", "block", "none", "reset", 26);
}

function switchTo(login, reset, register, hash, height) {
    $("#login-div").css("display", login);
    $("#reset-div").css("display", reset);
    $("#register-div").css("display", register);
    window.location.hash = "#" + hash;
    $(".center-vertical").css("height", height + "rem");
    signinItem.description = "";
    signinItem.emailErrorTip = "";
    signinItem.emailVerifyStatus = "";
    signinItem.passwordVerify = "";
    signinItem.passwordConfirm = "";
}

function submit() {
    return isEmpty(signinItem.description) && isEmpty(signinItem.emailErrorTip) && isEmpty(signinItem.emailVerifyStatus) && isEmpty(signinItem.passwordVerify) && isEmpty(signinItem.passwordConfirm);
}

$(document).ready(
    function () {
        $("#username").keyup(function () {
                var username = event.srcElement.value;
                if (username.match(userConfig.usernameMatch.pattern)) {
                    $.get("/user/username/exists", {username: username}, function (data) {
                        var json = JSON.parse(data);
                        /** @namespace json.exists */
                        signinItem.description = json.exists ? "用户名已经存在" : "";
                    });
                } else {
                    signinItem.description = userConfig.usernameMatch.description;
                }
            }
        );
        $(".email").keyup(function () {
            var email = event.srcElement.value;
            if (isEmail(email)) {
                if (location.hash === "#register") {
                    $.get("/user/email/exists", {email: email}, function (data) {
                        var json = JSON.parse(data);
                        signinItem.emailErrorTip = json.exists ? "该邮箱已经被注册啦" : "";
                    });
                }
                signinItem.emailErrorTip = "";
            } else {
                signinItem.emailErrorTip = "邮箱格式不正确";
            }
        });
        $(".password").keyup(function () {
            var len = event.srcElement.value.length;
            if (len >= userConfig.password.minLength && len <= userConfig.password.maxLength) {
                signinItem.passwordVerify = "";
            } else {
                signinItem.passwordVerify = "密码长度限定为" + userConfig.password.minLength + "至" + userConfig.password.maxLength + "位";
            }
        });
        $(".confirm-password").keyup(function () {
            var ele = event.srcElement;
            signinItem.passwordConfirm = (ele.value === $(ele).siblings(".password").val()) ? "" : "两次输入的密码不一样";
        });
        $(".sendVerifyCode").click(function () {
            var eventSrc = event.srcElement;
            console.info("test");
            sendVerifyCode($(eventSrc).parents(location.hash + "-div").find(".email").val(), eventSrc);
        });
        $(".email-verify-code").keyup(function () {
            var code = event.srcElement.value;
            if (code.length === 6) {
                $.ajax({
                    url: "/common/" + code + "/verification", type: "PUT", success: function (data) {
                        var json = JSON.parse(data);
                        signinItem.emailVerifyStatus = json.status === "success" ? "" : "验证码错误";
                    }
                });
            } else {
                signinItem.emailVerifyStatus = "";
            }
        });
    }
);

switch (window.location.hash) {
    case "#register":
        switchToRegister();
        break;
    case "#reset":
        switchToReset();
        break;
    default:
        switchToLogin();
        break;
}

layer.load(1);
$.get("/config/user", function (data) {
    layer.closeAll();
    userConfig = JSON.parse(data);
    signinItem.emailVerify = userConfig.emailVerify;
    /** @namespace userConfig.usernameMatch */
    if (window.location.hash === "#register") {
        switchToRegister();
    }
});
