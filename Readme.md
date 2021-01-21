[toc]

# 开眼视频


# 项目架构

项目使用 Kotlin 语言开发，



# 事件总线



# 监控及统计-友盟

使用友盟SDK进行数据采集与管理、业务监测、用户行为分析、App稳定性监控

# 相关知识点

## Android 10+ 数据存取权限

- Android Q文件存储机制修改成了沙盒模式
- APP只能访问自己目录下的文件和公共媒体文件
- 对于AndroidQ以下，还是使用老的文件存储方式


## 在 Android P 上使用限制了明文流量

由于 Android P 限制了明文流量的网络请求，非加密的流量请求都会被系统禁止掉

如果当前应用的请求是 http 请求，而非 https ,这样就会导系统禁止当前应用进行该请求，
如果 WebView 的 url 用 http 协议，同样会出现加载失败，
https 不受影响

为此，OkHttp3 做了检查，所以如果使用了明文流量，
默认情况下，在 Android P 版本 OkHttp3 就抛出异常:
 CLEARTEXT communication to " + host + " not permitted by network security policy


解决方法：
1. 在res目录下新建xml目录，创建network_security_config.xml文件
2. 编写network_security_config.xml内容：
```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
```
3.在 AndroidManifest.xml 的 application 标签中新加：
```
 android:networkSecurityConfig="@xml/network_security_config"
```



# 第三方依赖库

[Android智能下拉刷新框架-SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
[Android状态栏导航栏库](https://github.com/Zackratos/UltimateBarX)
[自定义 TabLayout](https://github.com/LillteZheng/FlowHelper)
[PermissionX 权限请求库](https://github.com/guolindev/PermissionX)
[TabLayout+ RecyclerView 锚点定位效果](https://github.com/KailuZhang/TabLayoutMediator2)
[提升H5加载速度框架-VasSonic](https://github.com/Tencent/VasSonic)
[沉浸式状态栏和沉浸式导航栏管理](https://github.com/gyf-dev/ImmersionBar)