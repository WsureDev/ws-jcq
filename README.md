# ws-jcq(已废弃，JCQ无了)
## 此项目将重已重构，请移步 [【warframe-world-state】](https://github.com/WsureDev/warframe-world-state)
一个jcq轮子，俺自己用
#### 这是什么
这是一个JCQ的轮子，为 [warframe-bot](https://github.com/WsureDev/warframe-bot) 这个项目量身打造

#### 有什么功能？

- 一.静态代理CoolQ API (为什么不动态代理? 老实说我也想，但是这玩意经不起折腾了，如果你熟悉jcq加载java插件的方式，你就会明白我的痛了。
  - 这样做能带来以下好处
    - 1.做AOP，可以用来统计、通知之类的，或者执行其他逻辑
    - 2.拦截，防止api滥用
- 二.一个简易的内置缓存,搭配一个Cache注解(用于注册新的缓存池),搭配一个枚举用来管理缓存池(其实我就是想试试枚举的单例特性)。可以从CacheEnum中取出指定的缓存池进行存取操作
- 三.两个核心注解`@BotEvent`、`@BotEventType`，名字不重要，哈哈。
  - `@BotEvent`是类注解，前者重在控制权限(权限分为主人、开发者、群主、管理、群员5级)。
  - `@BotEventType`是方法注解，主要负责`绑定事件`、`绑定指令别名`，事件就是jcq支持的事件，在此不啰嗦了。别名涉及到本项目配置文件中配置的指令触发词
- 四.一个简单的应答词库配置，适合一些的问答
