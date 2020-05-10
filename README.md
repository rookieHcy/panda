

## 简介

通过公众号接收关键词（论文url, doi） ，去sci.hub下载后，当接收请求关键词'ok'时，发送邮件


## 注册公众号

公众号平台申请公众号，申请测试账号（测试账号权限多）
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1gelia4zq5gj31k50u043t.jpg)

## 服务器接入

在公测试账号公众号平台填写接入的url和token
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1geliis0hoqj31d90u012o.jpg)

使用ngrok进行内网穿透（将本地服务暴露在公网）    
编写接入接口（get方法），按照[接入指南](https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html)正确接入

## 封装请求、响应消息对象 创建相应工具类
