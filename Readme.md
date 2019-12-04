# Cas SSO Service

以Cas单点登录为核心的一个模板,方便大家二次开发




## Authentication Server

1. 用于CAS用户认证服务的获取

2. CAS用户服务相关信息的获取  菜单 角色 其他




## CAS Overlay Template

 CAS For github https://github.com/apereo/cas


CAS 5.3.X Document  https://apereo.github.io/cas/5.3.x/index.html 本项目采用的是 5.3.14

REST 验证方式 https://apereo.github.io/cas/5.3.x/installation/Rest-Authentication.html

1.增加maven依赖

2. 配置Rest uri

可以自主修改CAS版本

CAS SSO美化


Cas 5.3 overlay-template

s


## 问题解决


1. Caused by: java.io.FileNotFoundException: \etc\cas\thekeystore (系统找不到指定的文件。)

keytool -genkey -alias cas -keyalg RSA -keysize 2048 -keypass changeit -storepass changeit -keystore casexample.keystore -dname "CN=http://cas.example.org/,OU=casexample.com,O=casexample,L=casexample,ST=casexample,C=CN" -deststoretype pkcs12