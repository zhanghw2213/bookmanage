var app = new Vue({
    el: "#index",
    data: {
        username: "",
        permission: 1,
        loginTime: "",
        passwordVerify: "",
        passwordConfirm: "",
        emailErrorTip: "",
        emailVerifyStatus: ""
    }
});

var search = "";

Vue.component('paging-more', {
    template: '<button class="btn btn-link btn-block btn-lg" onclick="offset += 1;getPage();"><b>获取更多</b></button><br/><br/>'
});

var userInfo = {};

function getPage() {
    if (currentTab === "#downloaded-content") {
        getUserDownloaded();
    } else if (currentTab === "#uploaded-content") {
        getUserUploaded();
    } else {
        getResource(getOrderBy());
    }
}

/**
 * 保存用户信息
 */
function saveInfo() {
    var email = $("#email").val();
    if (isEmail(email)) {
        var code = $("#email-verify-code").val();
        if (!userConfig.emailVerify || code.length === 6 || email === userInfo.email) {
            layer.load(1);
            $.ajax({
                url: '/user/info',
                type: 'PUT',
                dataType: "",
                data: {
                    avatar: $("#avatar").attr("src"),
                    realName: $("#real-name").val(),
                    email: email,
                    code: code
                },
                success: function (data) {
                    layer.closeAll();
                    var json = JSON.parse(data);
                    userInfo.email = json.email;
                    alerts(json.message);
                }
            });
        } else {
            alerts("验证码格式不正确");
        }
    } else {
        alerts("邮箱格式不正确");
    }
}
/*注销登录*/
// function logout() {
//     console.log(2344)
//     //alert(333)
//     //window.location.href = "/login";
//     /*$.ajax({
//         url: "/user/logout",
//         type: "PUT",
//         success: function (data) {
//             window.location.href = "/index";
//         }
//     });*/
// }

function logout() {
    var oldPassword = $("#old-password").val();
    var newPassword = $("#new-password").val();
    var confirmNewPassword = $("#confirm-new-password").val();
    if (oldPassword && checkPassword(newPassword, confirmNewPassword)) {
        layer.load(1);
        $.ajax({
            url: "/user/password",
            type: "PUT",
            data: {oldPassword: oldPassword, newPassword: newPassword},
            success: function (data) {
                layer.closeAll();
                var json = JSON.parse(data);
                if (json.status === "success") {
                    alerts("密码修改成功");
                    location.href = "/signin.html#login";
                } else {
                    alerts(json.message);
                }
            }
        });
    } else {
        alerts("格式不合法，无法提交");
    }
}

/**
 * 更新密码
 */
function updatePassword() {
    var oldPassword = $("#old-password").val();
    var newPassword = $("#new-password").val();
    var confirmNewPassword = $("#confirm-new-password").val();
    if (oldPassword && checkPassword(newPassword, confirmNewPassword)) {
        layer.load(1);
        $.ajax({
            url: "/user/password",
            type: "PUT",
            data: {oldPassword: oldPassword, newPassword: newPassword},
            success: function (data) {
                layer.closeAll();
                var json = JSON.parse(data);
                if (json.status === "success") {
                    alerts("密码修改成功");
                    location.href = "/signin.html#login";
                } else {
                    alerts(json.message);
                }
            }
        });
    } else {
        alerts("格式不合法，无法提交");
    }
}

function getUserInfo() {
    layer.load(1);
    $.get("/user/info", function (data) {
        layer.closeAll();
        try {
            var json = JSON.parse(data);
            userInfo = json;
            app.permission = json.permission;
            /** @namespace app.lastLoginTime */
            app.loginTime = new Date(json.lastLoginTime).format("yyyy-MM-dd hh:mm:ss");
            /** @namespace json.avator */
            if (!isEmpty(json.avatar)) {
                $("#avatar").attr("src", json.avatar);
            }
            app.username = json.username;
            $("#email").val(json.email);
            $("#real-name").val(json.realName);
            checkEmailChange(json.email);
        } catch (e) {
            window.location.href = "/signin";
        }
    });
}

function showAvatarModal() {
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        offset: 'ct',
        shadeClose: true,
        content: "<input id='file-input' class='form-control file' multiple data-max-file-count='100' data-preview-file-type='img' name='file' type='file'/>"
    });
    $("#file-input").fileinput({
        uploadUrl: "/common/avatar",
        uploadAsync: true,
        maxFileCount: 1,
        maxFilePreviewSize: 10485760
    }).on('fileuploaded', function (event, data) {
        var json = data.response;
        if (JSON.stringify(json).indexOf("success") > 0) {
            $("#avatar").attr("src", json.success);
        } else {
            alerts(json.error);
        }
    });
}

function getOrderBy() {
    return $("#order-by").val() + " " + $("#order-way").val();
}

$(document).ready(function () {
    $("#search").keyup(function () {
        /** @namespace window.event.keyCode */
        if (window.event.keyCode === 13) {
            search = $('#search').val();
            offset = 0;
            getPage();
        }
    });
    $(".content-filter").change(function () {
        offset = 0;
        getResource(getOrderBy());
    });
    $(".email-verify-code").keyup(function () {
        var code = event.srcElement.value;
        if (code.length === 6) {
            $.ajax({
                url: "/common/" + code + "/verification", type: "PUT", success: function (data) {
                    var json = JSON.parse(data);
                    app.emailVerifyStatus = json.status === "success" ? "" : "验证码错误";
                }
            });
        } else {
            app.emailVerifyStatus = "";
        }
    });
    $(".email").keyup(function () {
        checkEmailChange(event.srcElement.value);
    });
    $(".password").keyup(function () {
        var len = event.srcElement.value.length;
        if (len >= userConfig.password.minLength && len <= userConfig.password.maxLength) {
            app.passwordVerify = "";
        } else {
            app.passwordVerify = "密码长度限定为" + userConfig.password.minLength + "至" + userConfig.password.maxLength + "位";
        }
    });
    $(".confirm-password").keyup(function () {
        app.passwordConfirm = (event.srcElement.value === $("#new-password").val()) ? "" : "两次输入的密码不一样";
    });
    $(".sendVerifyCode").click(function () {
        sendVerifyCode($("#email").val(), event.srcElement);
    });

    $("a.nav-link[href='" + location.hash + "-tab']").click();
    getTabContent(location.hash);
    $(".nav-link").click(function () {
        getTabContent($(event.srcElement).attr("href"));
    });
});

function getTabContent(href) {
    if (href.startsWith("uploaded", 1)) {
        offset = 0;
        window.location.hash = "uploaded";
        getUserUploaded();
    } else if (href.startsWith("downloaded", 1)) {
        offset = 0;
        window.location.hash = "downloaded";
        getUserDownloaded();
    } else if (href.startsWith("bio", 1)) {
        window.location.hash = "bio";
        getUserInfo();
    } else {
        offset = 0;
        window.location.hash = "resource";
        getResource("");
    }
}

var offset = 0;

function checkEmailChange(email) {
    var isChange = email !== userInfo.email;
    if (isEmail(email)) {
        if (isChange) {
            $.get("/user/email/exists", {email: email}, function (data) {
                var json = JSON.parse(data);
                app.emailErrorTip = json.exists ? "该邮箱已经被注册啦" : "";
            });
        }
        app.emailErrorTip = "";
    } else {
        app.emailErrorTip = "邮箱格式不正确";
    }
    $(".verify-code-div").css("display", isChange && userConfig.emailVerify ? "block" : "none");
}

/**
 * 加载用户配置信息
 */
layer.load(1);
$.get("/config/user", function (data) {
    layer.closeAll();
    userConfig = JSON.parse(data);
});

var currentTab = "#resources-content";

function getUserDownloaded() {
    currentTab = "#downloaded-content";
    layer.load(1);
    $.get("/file/user/downloaded", {offset: offset, search: search}, function (data) {
        layer.closeAll();
        try {
            setResources(JSON.parse(data), currentTab);
        } catch (e) {
            window.location.href = "/signin";
        }
    });
}

function getUserUploaded() {
    currentTab = "#uploaded-content";
    layer.load(1);
    $.get("/book/user/uploaded", {offset: offset, search: search}, function (data) {
        layer.closeAll();
        setResources(JSON.parse(data), currentTab);
        /*try {

        } catch (e) {
            window.location.href = "/signin";
        }*/
    });
}

function getResource(orderBy) {
    currentTab = "#resources-content";
    layer.load(1);
    $.get("/book/all", {
        offset: offset,
        search: search
    }, function (data) {
        console.log(data)
        layer.closeAll();
        setResources(JSON.parse(data), currentTab);
    });
}

function setResources(resources, tabId) {
    var isManager = localStorage.getItem("isManager");
    var ifAdmin = isManager=='true'? 'block':'none';
    var contentHtml = "";
    search = "";
    if (resources.books.length < 1) {
        offset -= 1;
        alerts("糟糕，没有数据了");
    } else {
        $.each(resources.books, function (i, resource) {
            console.log(resource)
            var param = "title="+resource.title+"&authorName="+resource.authorName+"&pushDate="+resource.pushDate+"&path="+resource.path+"&source="+resource.source;
            param= param.replace(/\\/g,"@").replace(/\//g,"@");
            param = encodeURI(encodeURI(param));
            var isDownloaded = "#downloaded-content" === tabId;
            var date = isDownloaded ? resource.downloadTime : resource.createTime;
            /*contentHtml += "<div style='margin-bottom: 7px;' class='row content-box rounded' data-id='" + resource.id + "'>"+
                "<div class='col-sm-11 col-12'>" +
                "<a href='down.html?"+param+"' target='_blank' style='cursor: pointer'>"+
                "<p>" +
                "论文题目：<b>" + resource.title + "</b>&emsp;" +
                "论文作者：<b class='file-category'>" + resource.authorName + "</b>&emsp;" +
                "发表时间：<b class='file-category'>" + resource.pushDate + "</b>&emsp;" +
                "上传者：<b class='file-category'>" + resource.username + "</b>&emsp;" +
                "论文出处：<b class='file-category'>" + resource.source + "</b>&emsp;" +
                "是否审核通过：<b class='file-category'>" + (resource.verifyed==null?'未审核':(resource.verifyed==1?'通过':'不通过')) + "</b>" +
                "</p></a></div>" +
                "<button data-toggle='modal' data-target='#myModal' att1='"+resource.bookId+"' style='cursor: pointer; border-radius: 6px; background-color: #4CAF50;padding: 3px 21px;margin: 4px; display: "+ifAdmin+";'>审核</button>" +
                "</div></div></div>";*/
                contentHtml +="<tr>"+
                    "<td><a href='down.html?"+param+"' target='_blank' style='cursor: pointer'>"+resource.title+"</a></td>"+
                    "<td>"+resource.authorName+"</td>"+
                    "<td>"+resource.pushDate+"</td>"+
                    "<td>"+resource.source+"</td>"+
                    "<td>"+resource.userName+"</td>"+
                    "<td id='"+"+resource.bookId+"+"'>"+(resource.verifyed==null?'未审核':(resource.verifyed==1?'通过':'不通过'))+"</td>"+
                    "<td><a onclick='showModel("+resource.bookId+")' style='cursor: pointer; text-align: center; margin: 4px; display: "+ifAdmin+";' att1='"+resource.bookId+"' href='#' >审核</a></td>"+
                "</tr>";
        });
        if (offset > 0) {
            $(tabId).append(contentHtml);
        } else {
            $(tabId).html(contentHtml);
        }
        // $(tabId).html(contentHtml);
        $('[data-toggle="tooltip"]').tooltip();
        setCSS();
    }
}

//展示审核模态框
function showModel(bookId) {
    $("#myModal").modal("show");
    $("#bookIdTemp").val(bookId);
}

//审核
function audit(auditCode) {
    var bookId = $("#bookIdTemp").val();
    $.ajax({
        url: "/book/audit", type: "PUT", data: {
            bookId: bookId,
            auditCode: auditCode
        }, success: function (data) {
            if (data.result == "success"){
                if (auditCode == 1)
                    $("#"+bookId).html("通过");
                 else
                    $("#"+bookId).html("不通过");
                alerts("审核完成");
            } else
                alerts("审核失败");
        }
    });
}

var srcContentBox;

function editFile() {
    var contentBox = $(event.srcElement).parents(".content-box");
    srcContentBox = contentBox;
    $("#edit-file-id").val($(contentBox).attr("data-id"));
    $("#edit-file-name").val($(contentBox).find("a.visit-url").text());
    $("#edit-file-category").val($(contentBox).find("b.file-category").text());
    $("#edit-file-tag").val($(contentBox).find("b.file-tag").text());
    $("#edit-file-description").val($(contentBox).find("a.visit-url").attr("data-description"));
    $("#edit-file-modal").modal("show");
}

function saveFileInfo() {
    var name = $("#edit-file-name").val();
    var category = $("#edit-file-category").val();
    var tag = $("#edit-file-tag").val();
    var description = $("#edit-file-description").val();
    if (isEmpty(name)) {
        alerts("文件名不能为空");
    } else {
        layer.load(1);
        $.ajax({
            url: "/file/" + $("#edit-file-id").val(),
            type: "PUT",
            data: {
                name: name,
                category: category,
                tag: tag,
                description: description
            },
            success: function (data) {
                layer.closeAll();
                var json = JSON.parse(data);
                if (json.status === "success") {
                    $(srcContentBox).find("a.visit-url").text(name);
                    $(srcContentBox).find("b.file-category").text(category);
                    $(srcContentBox).find("b.file-tag").text(tag);
                    $(srcContentBox).find("a.visit-url").attr("data-description", description);
                    var href = $(srcContentBox).find("a.visit-url").attr("href");
                    $(srcContentBox).find("a.visit-url").attr("href", href.substr(0, href.lastIndexOf("/") + 1) + name);
                    $("#edit-file-modal").modal("hide");
                    alerts("保存成功");
                } else {
                    alerts(json.message);
                }
            }
        })
        ;
    }
}

function removeFile() {
    var contentBox = $(event.srcElement).parents(".content-box");
    layer.confirm('是否确定删除文件【' + $(contentBox).find("a.visit-url").text() + '】', {
        btn: ['确定', '删除'],
        skin: 'layui-layer-molv'
    }, function () {
        var id = $(contentBox).attr("data-id");
        layer.load(1);
        $.ajax({
            url: "/file/" + id, type: "DELETE", success: function (data) {
                layer.closeAll();
                var json = JSON.parse(data);
                if (json.status === "success") {
                    $(contentBox).remove();
                    layer.msg("删除成功");
                } else {
                    alerts(json.message);
                }
            }, error: function () {
                layer.closeAll();
                alerts("服务器异常，请联系管理员");
            }
        });
    });
}

$.get("/category/all", function (data) {
    var json = JSON.parse(data);
    var option = "";
    $.each(json, function (i, category) {
        option += "<option value='" + category.name + "'>" + category.name + "</option>";
    });
    if (!isEmpty(option)) {
        $("#edit-file-category").html(option);
    }
    option = "";
    $.each(json, function (i, category) {
        option += "<option value='" + category.id + "'>" + category.name + "</option>";
    });
    if (!isEmpty(option)) {
        $("#category").append(option);
    }
});
