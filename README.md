## wehax安卓内部库 ##

----

`cn.wehax.common`主要包含以下功能：

* `framework` 提供一套mvp框架的声明和实现.  
* `widget` 提供若干控件。
* `container` 为fragment提供容器。

`cn.wehax.util`包含若干辅助类。

#### framework

`cn.wehax.common.framework`包含了以下功能：

* view里包含若干常用view的声明，
* view.impl里包含activity和fragment的对应实现。
* presenter里包含对应view的若干默认presenter声明。
* presenter.impl里包含presenter的默认实现，以及单个数据返回时，以及列表数据返回时的默认回调。
* adapter包含框架用到的GenericAdapter,列表presenter用到的IRender接口，
* adapter.impl里包含列表逻辑用到的IRender实现。
* model里提供了基础bean以及若干常用bean。同时定义了数据返回的接口。
* data.annotation声明了数据解析用到的若干标注。
* data.helper包含数据解析的辅助方法。
