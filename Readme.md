
# 开眼视频

一款模仿 Eyepetizer | 开眼视频的 开源app


> 此项目仅用于学习，相关接口和数据均来自于网络


# 项目架构

项目采用 Jetpack + 协程实现的 MVVM 架构,开发语言为 Kotlin 


![项目架构](https://github.com/smashinggit/eyepetizer/blob/master/pic/architecture.png)


# 项目截图
![screen1](https://github.com/smashinggit/eyepetizer/blob/master/pic/screen1.jpg)
![screen2](https://github.com/smashinggit/eyepetizer/blob/master/pic/screen2.jpg)
![screen3](https://github.com/smashinggit/eyepetizer/blob/master/pic/screen3.jpg)
![screen4](https://github.com/smashinggit/eyepetizer/blob/master/pic/screen4.jpg)
![screen5](https://github.com/smashinggit/eyepetizer/blob/master/pic/screen5.jpg)
![screen6](https://github.com/smashinggit/eyepetizer/blob/master/pic/screen6.jpg)


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

- [Android智能下拉刷新框架-SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)

- [图片加载框架Glide](https://github.com/gyf-dev/ImmersionBar)

- [基于IJKPlayer的多功能视频播放器](https://github.com/CarGuo/GSYVideoPlayer)

- [广告轮播图Banner](https://github.com/youth5201314/banner)

- [事件总线](https://github.com/greenrobot/EventBus)


- [自定义 TabLayout](https://github.com/LillteZheng/FlowHelper)

- [PermissionX 权限请求库](https://github.com/guolindev/PermissionX)

- [TabLayout+ RecyclerView 锚点定位效果](https://github.com/KailuZhang/TabLayoutMediator2)

- [提升H5加载速度框架-VasSonic](https://github.com/Tencent/VasSonic)

- [沉浸式状态栏和沉浸式导航栏管理](https://github.com/gyf-dev/ImmersionBar)


# 监控及统计-友盟
使用友盟SDK进行数据采集与管理、业务监测、用户行为分析、App稳定性监控
