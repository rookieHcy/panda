

## 简介

通过公众号接收关键词（论文url, doi） ，去sci.hub下载后，当接收请求关键词'ok'时，发送邮件


## 注册公众号

公众号平台申请公众号，申请测试账号（测试账号权限多）
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1gelia4zq5gj31k50u043t.jpg)

## 服务器接入

在公众号平台填写接入的url和token
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1geliis0hoqj31d90u012o.jpg)

[接入指南](https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html)

http://wx.ilovszsn.com/wx/ 使用nginx进行代理，将wx转发到9999端口
提供接口()