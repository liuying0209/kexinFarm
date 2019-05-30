# kx-fram-core


## 钉钉接口文档

#### 钉钉用户相关接口
* [普通登入接口](#普通登入接口)
* [钉钉登入接口](#钉钉登入接口)

首页农场相关接口
* [创建农场接口](#创建农场接口)
* [删除农场](#删除农场)
* [更新农场接口](#更新农场接口)
* [获取三级联动菜单接口](#获取三级联动菜单接口)
* [更新地块名称接口](#更新地块名称接口)
* [地块添加农作物接口](#地块添加农作物接口)
* [开始种植接口](#开始种植接口)

农场记录相关接口
* [查询追加农事集合](#查询追加农事集合)
* [查询所有父类农事环节接口](#查询所有父类农事环节接口)
* [保存农事环节记录](#保存农事环节记录)
* [获取农事环节页面参数](#获取农事环节页面参数)

参数例子
   * [土壤消毒](#土壤消毒)
   * [底肥](#底肥)
   * [育苗基质](#育苗基质)
   * [播种](#播种)
   * [出苗](#出苗)
   * [定植](#定植)
   * [开花](#开花)
   * [授粉](#授粉)
   * [采收](#采收)
   * [农药投入](#农药投入)
   * [追肥投入](#追肥投入)

上传下载
* [上传单个文件接口](#上传单个文件接口)



用户信息
* [获取用户信息](#获取用户信息)
* [更新用户默认农场id](#更新用户默认农场id)
* [完善信息](#完善信息)
* [获取当前用户农场集合](#获取当前用户农场集合)
* [获取单个农场详情](#获取单个农场详情)
* [获取地块信息集合](#获取地块信息集合)
* [获取当前农场下所有品种作物信息](#获取当前农场下所有品种作物信息)
* [获取农场品种作物详情](#获取农场品种作物详情)
* [农场评估列表](#农场评估列表)


#### 统一返回格式-普通
~~~
{
    "status": 200,
    "message": "ok",
    "data": {}
}
~~~

#### 统一返回格式-分页
~~~
{
    "status": 200,
    "message": "ok",
    "data": {
        "count": 1,
        "list": []
    }
}
~~~

登入校验 请求头中需要携带token



#### 普通登入接口

请求路径 api/user/login

请求方式 POST

**请求参数**

 参数  | 类型  | 是否必填 |示例 | 描述
 |---- | ----- | ------  | ------| ------
 username  | String | 是 | 17611013091| 登入账号
 password  | String | 是 | 123456 | 登入密码
 

**响应参数**

 参数  | 类型   | 描述
 |---- | ----- | ------
 |token| String | 请求返回token
 
#### 钉钉登入接口
 
请求路径 api/dd/login

请求方式 POST

**请求参数**

 参数  | 类型  | 是否必填 |示例 | 描述
 |---- | ----- | ------  | ------| ------
 corpId  | String | 是 | “2132dsada”| 企业corpId
 requestAuthCode  | String | 是 | "dasdad" | 授权码


**响应参数**

 参数  | 类型   | 描述
 |---- | ----- | ------
 |token| String | 请求返回token

 
#### 创建农场接口

请求路径 api/farm/add

请求方式 POST

**请求参数**

 参数  | 类型  | 是否必填 |示例 | 描述
 |---- | ----- | ------  | ------| ------
 farmName  | String | 是 | "一号农场"| 农场名称
 area  | Integer | 是 | 100 | 面积
  brooderCount  | Integer | 是 | 10 | 暖棚数量
  coolCount  | Integer | 是 | 10 | 冷棚数量
  answerScore  | Integer | 否 | 10 | 答题分数

**响应参数**

 参数  | 类型   | 描述
 |---- | ----- | ------
 |farmId| Long | 农场id
 
 
#### 删除农场

请求路径 api/farm/delete/{farmId}

请求方式 POST

**请求参数**

参数  | 类型  | 是否必填 |示例 | 描述
|---- | ----- | ------  | ------| ------

**响应参数**

参数  | 类型   | 描述
|---- | ----- | ------
   
 
 #### 更新农场接口
 
 请求路径 api/farm/update
 
 请求方式 POST
 
 **请求参数**
 
  参数  | 类型  | 是否必填 |示例 | 描述
  |---- | ----- | ------  | ------| ------
  farmName  | String | 否 | "一号农场"| 农场名称
  area  | Integer | 否 | 100 | 面积
   farmId  | Long | 是 | 10 | 农场id

 
 **响应参数**
 
  参数  | 类型   | 描述
  |---- | ----- | ------
 
 
#### 获取三级联动菜单接口

请求路径 api/farm/menu

请求方式 GET

**请求参数**

 参数  | 类型  | 是否必填 |示例 | 描述
 |---- | ----- | ------  | ------| ------
 farmId  | Long | 是 | "12"| 农场id
 plotType  | Integer | 是 | 0 | 地块类型 0-暖棚 1-冷棚


**响应参数**

 参数  | 类型   | 描述
 |---- | ----- | ------
|id| Long | 对应联级id
|name| String | 名称
|list| Array | 下级对象数组

~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "name": "1号棚",
            "id": 1,
            "list": [
                {
                    "name": "大番茄",
                    "id": 1,
                    "list": [
                        {
                            "name": "422",
                            "id": 1
                        },
                        {
                            "name": "423",
                            "id": 2
                        }
                    ]
                }
            ]
        },
        {
            "name": "2号棚",
            "id": 1,
            "list": [
                {
                    "name": "大番茄",
                    "id": 2,
                    "list": [
                        {
                            "name": "423",
                            "id": 3
                        }
                    ]
                }
            ]
        }
    ]
}
~~~



#### 更新地块名称接口

请求路径 api/farm/updatePlot

请求方式 POST

**请求参数**

 参数  | 类型  | 是否必填 |示例 | 描述
 |---- | ----- | ------  | ------| ------
 plotId  | String | 是 | "12"| 地块编号
 name  | String | 是 | "xx暖棚" | 地块名称


**响应参数**

 参数  | 类型   | 描述
 |---- | ----- | ------
无


#### 地块添加农作物接口

请求路径 api/farm/addCropVariety

请求方式 POST

**请求参数**

 参数  | 类型  | 是否必填 |示例 | 描述
 |---- | ----- | ------  | ------| ------
 cropVariety   | String | 是 | "品种222"| 品种名称
 batchTime  | String | 是 | "2018-01-01" | 批次时间
 area   | Integer | 是 | 100 | 面积
 plotId  | Long | 是 | 11 | 地块id
 cropId  | Long | 是 | 11 | 农作物id

**响应参数**

 参数  | 类型   | 描述
 |---- | ----- | ------
| plotCropId | Long | 品种作物编号 |


#### 开始种植接口

请求路径 api/record/start/{plotCropId}

请求方式 POST

**请求参数**

 参数  | 类型  | 是否必填 |示例 | 描述
 |---- | ----- | ------  | ------| ------
|plotCropId | Long |是 |1234 | 品种作物id|


**响应参数**

 参数  | 类型   | 描述
 |---- | ----- | ------
 
 
 #### 查询追加农事集合
 
 请求路径 api/record/addFarmWork
 
 请求方式 GET
 
 **请求参数**
 
  参数  | 类型  | 是否必填 |示例 | 描述
  |---- | ----- | ------  | ------| ------
 
 
 **响应参数**
 
  参数  | 类型   | 描述
  |---- | ----- | ------
| id | Long | 农事环节作物编号 |
| name | String | 基础农事环节名称 |
| category | Integer | 归类 0-直接拍摄  1-前置任务  2-细分子任务 |


#### 追加农事环节接口
 
 请求路径 api/record/appended
 
 请求方式 POST
 
 **请求参数**
 
  参数  | 类型  | 是否必填 |示例 | 描述
  |---- | ----- | ------  | ------| ------
 |recordId | Long |是 |1234 | 当前农事环节记录id 如 播种 |
 |farmingId  | Long |是 |1234 | 追加的农事环节id 如 化肥 这个农事环节id  |
 
 **响应参数**
 
  参数  | 类型   | 描述
  |---- | ----- | ------
| recordId | Long | 记录编号 |


#### 查询所有父类农事环节接口
 
 请求路径 api/record/listRecordMenu/{plotCropId}
 
 请求方式 GET
 
 **请求参数**
 
  参数  | 类型  | 是否必填 |示例 | 描述
  |---- | ----- | ------  | ------| ------
 |plotCropId | Long |是 |1234 | 品种作物id|
 
 **响应参数**
 
  参数  | 类型   | 描述
  |---- | ----- | ------
| recordId | Long | 记录编号 |
| farmingId | Long | 农事环节id |
| farmingName | String | 名称|
| addFlag | Integer | 是否有追加 0-否 1-是|
| category | Integer | 归类 0-直接拍摄  1-前置任务  2-细分子任务|
| list | Array| 如果有追加数据 会有数据|
| status | Integer| 完成状态 0-未开始 1-未完成，2-已完成|

没有追加
~~~

{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 14,
            "farmingId": 1,
            "farmingName": "土壤消毒",
            "addFlag": 0,
            "category": 1,
            "status": 0,
            "list": null
        },
        {
            "recordId": 22,
            "farmingId": 2,
            "farmingName": "底肥",
            "addFlag": 0,
            "category": 1,
            "status": 0,
            "list": null
        },
        {
            "recordId": 23,
            "farmingId": 3,
            "farmingName": "育苗基质",
            "addFlag": 0,
            "category": 1,
            "status": 0,
            "list": null
        },
        {
            "recordId": 24,
            "farmingId": 4,
            "farmingName": "播种",
            "addFlag": 0,
            "category": 0,
            "status": 0,
            "list": null
        },
        {
            "recordId": 25,
            "farmingId": 5,
            "farmingName": "出苗",
            "addFlag": 0,
            "category": 2,
            "status": 0,
            "list": null
        },
        {
            "recordId": 26,
            "farmingId": 6,
            "farmingName": "定植",
            "addFlag": 0,
            "category": 2,
            "status": 0,
            "list": null
        },
        {
            "recordId": 27,
            "farmingId": 7,
            "farmingName": "开花",
            "addFlag": 0,
            "category": 2,
            "status": 0,
            "list": null
        },
        {
            "recordId": 28,
            "farmingId": 8,
            "farmingName": "授粉",
            "addFlag": 0,
            "category": 2,
            "status": 0,
            "list": null
        },
        {
            "recordId": 29,
            "farmingId": 9,
            "farmingName": "采收",
            "addFlag": 0,
            "category": 0,
            "status": 0,
            "list": null
        }
    ]
}
~~~

有追加
~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 14,
            "farmingId": 1,
            "farmingName": "土壤消毒",
            "addFlag": 0,
            "category": 1,
            "status": 0,
            "list": null
        },
        {
            "recordId": 22,
            "farmingId": 2,
            "farmingName": "底肥",
            "addFlag": 0,
            "category": 1,
            "status": 0,
            "list": null
        },
        {
            "recordId": 23,
            "farmingId": 3,
            "farmingName": "育苗基质",
            "addFlag": 0,
            "category": 1,
            "status": 0,
            "list": null
        },
        {
            "recordId": 24,
            "farmingId": 4,
            "farmingName": "播种",
            "addFlag": 1,
            "category": 0,
            "status": 0,
            "list": [
                {
                    "recordId": 46,
                    "appendedType": 0,
                    "farmingName": "农药投入",
                    "status": 0,
                    "category": 0
                },
                {
                    "recordId": 47,
                    "appendedType": 1,
                    "farmingName": "追肥投入",
                    "status": 0,
                    "category": 1
                },
                {
                    "recordId": 50,
                    "appendedType": 1,
                    "farmingName": "追肥投入",
                    "status": 0,
                    "category": 1
                }
            ]
        },
        {
            "recordId": 25,
            "farmingId": 5,
            "farmingName": "出苗",
            "addFlag": 0,
            "category": 2,
            "status": 0,
            "list": null
        },
        {
            "recordId": 26,
            "farmingId": 6,
            "farmingName": "定植",
            "addFlag": 0,
            "category": 2,
            "status": 0,
            "list": null
        },
        {
            "recordId": 27,
            "farmingId": 7,
            "farmingName": "开花",
            "addFlag": 0,
            "category": 2,
            "status": 0,
            "list": null
        },
        {
            "recordId": 28,
            "farmingId": 8,
            "farmingName": "授粉",
            "addFlag": 0,
            "category": 2,
            "status": 0,
            "list": null
        },
        {
            "recordId": 29,
            "farmingId": 9,
            "farmingName": "采收",
            "addFlag": 0,
            "category": 0,
            "status": 0,
            "list": null
        },
        {
            "recordId": 46,
            "farmingId": 10,
            "farmingName": "农药投入",
            "addFlag": 0,
            "category": 0,
            "status": 0,
            "list": null
        },
        {
            "recordId": 47,
            "farmingId": 11,
            "farmingName": "追肥投入",
            "addFlag": 0,
            "category": 1,
            "status": 0,
            "list": null
        },
        {
            "recordId": 50,
            "farmingId": 11,
            "farmingName": "追肥投入",
            "addFlag": 0,
            "category": 1,
            "status": 0,
            "list": null
        }
    ]
}
~~~


#### 获取农事环节页面参数
 
 请求路径 api/record/getParams
 
 请求方式 GET
 
 **请求参数**
 
  参数  | 类型  | 是否必填 |示例 | 描述
  |---- | ----- | ------  | ------| ------
 |recordId | Long |是 |1234 | 记录id|
 |category | Integer |是 |0 | 0-直接拍摄  1-前置任务  2-细分子任务 |
 
 **响应参数**
 
  参数  | 类型   | 描述
  |---- | ----- | ------
| recordId | Long | 记录编号 |
| name | String | 名称 |
| status | Integer | 完成状态 0-未开始，1-未完成  2-已完成|
| content | String | 具体参数|
| time | String | 农事完成时间|



content 中参数说明

  参数  | 类型   | 描述
|---- | ----- | ------
| unit | String | 单位 |
| flag | String | 是否完成标识 0-未完成 1-已完成 |
| createTime | String | 完成时间 “2018-02-02” |
| type | String | 参数类型 0-图片 1-文字 2-有子结构|
| name | String | 文案 |
| value | String | 值 |


### 土壤消毒
~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 15,
            "name": "日光高温闷棚",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "土壤消毒闭棚前地表整体",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "消毒完成开棚时地表整体",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 16,
            "name": "臭氧消毒",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "消毒操作现场",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 17,
            "name": "火焰消毒",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "消毒操作现场",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 18,
            "name": "蒸汽消毒",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "消毒操作现场",
                    "type": "0",
                    "value": ""
                }
            ],
           "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 19,
            "name": "热水消毒",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "消毒操作现场",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 20,
            "name": "药剂消毒",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "农药外包装正面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "农药外包装反面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "配药现场",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "农药名称",
                    "type": "1",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "稀释倍数",
                    "type": "1",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "用水桶数",
                    "type": "1",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "喷雾器容量",
                    "type": "1",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 21,
            "name": "不消毒",
            "content": [],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}

~~~

### 底肥

~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 30,
            "name": "自制有机肥",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料制作现场",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "掺入其它肥料",
                    "type": "2",
                    "value": [
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装正面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装反面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "配药现场",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药名称",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "稀释倍数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "用水桶数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "喷雾器容量",
                            "type": "1",
                            "value": ""
                        }
                    ]
                },
                {
                    "score": 0,
                    "unit": "kg",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料施用现场",
                    "type": "1",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "kg",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料用量",
                    "type": "1",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 31,
            "name": "商品有机肥",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料外包装正面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料外包装反面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "掺入其它肥料",
                    "type": "2",
                    "value": [
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装正面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装反面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "配药现场",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药名称",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "稀释倍数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "用水桶数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "喷雾器容量",
                            "type": "1",
                            "value": ""
                        }
                    ]
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料施用现场",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "kg",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料用量",
                    "type": "1",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~

### 育苗基质
~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 32,
            "name": "商品基质",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "外包装正面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "外包装反面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "消毒投入品",
                    "type": "2",
                    "value": [
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装正面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装反面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "配药现场",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药名称",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "稀释倍数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "用水桶数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "喷雾器容量",
                            "type": "1",
                            "value": ""
                        }
                    ]
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料投入品",
                    "type": "2",
                    "value": [
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装正面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "createTime": "",
                            "name": "农药外包装反面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "配药现场",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药名称",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "稀释倍数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "用水桶数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "喷雾器容量",
                            "type": "1",
                            "value": ""
                        }
                    ]
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 33,
            "name": "自制基质",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "基质制作现场",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "外包装反面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "消毒投入品",
                    "type": "2",
                    "value": [
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装正面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装反面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "配药现场",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药名称",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "稀释倍数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "用水桶数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "喷雾器容量",
                            "type": "1",
                            "value": ""
                        }
                    ]
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料投入品",
                    "type": "2",
                    "value": [
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装正面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "createTime": "",
                            "name": "农药外包装反面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "配药现场",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药名称",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "稀释倍数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "用水桶数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "喷雾器容量",
                            "type": "1",
                            "value": ""
                        }
                    ]
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~

### 播种（催芽）

~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 24,
            "name": "播种",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "穴盘",
                    "type": "1",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "种子外包装",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "育苗总数",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~


### 出苗

~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 34,
            "name": "出苗",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "幼苗出土整体",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 35,
            "name": "出苗15-17天",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "幼苗出土整体",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "全株近景",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 36,
            "name": "出苗25-27天",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "幼苗出土整体",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "全株近景",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~

### 定植

~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 37,
            "name": "定植",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "蘸根投入品",
                    "type": "2",
                    "value": [
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装正面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装反面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "配药现场",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药名称",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "稀释倍数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "用水桶数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "喷雾器容量",
                            "type": "1",
                            "value": ""
                        }
                    ]
                },
                {
                    "score": 0,
                    "unit": "kg",
                    "flag": "0",
                    "createTime": "",
                    "name": "定植株数",
                    "type": "1",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 38,
            "name": "定植15-17天",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "单垄幼苗整体",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "全株近景",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 39,
            "name": "定植25-27天",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "单垄幼苗整体",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "全株近景",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~

### 开花

~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 40,
            "name": "开花",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "花穗近景",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "全株近景",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 41,
            "name": "开花7-9天",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "花穗近景",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "全株近景",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 42,
            "name": "开花14-16天",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "花穗近景",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "全株近景",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "第1穗果正常果",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "第1穗果畸形果",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 43,
            "name": "开花28-30天",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "花穗近景",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "全株近景",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "第1穗果正常果",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "第1穗果畸形果",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~

### 授粉

~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 44,
            "name": "第一次授粉",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "授粉现场",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "开放的花朵近景",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "蜂箱",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 45,
            "name": "第二次授粉",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "授粉现场",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "开放的花朵近景",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "蜂箱",
                    "type": "0",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~

### 采收
~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 29,
            "name": "采收",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "可采收果实近景",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "kg",
                    "flag": "0",
                    "createTime": "",
                    "name": "采收量",
                    "type": "1",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~

### 农药投入
~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 91,
            "name": "农药投入",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "农药外包装正面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "农药外包装反面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "配药现场",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "农药名称",
                    "type": "1",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "稀释倍数",
                    "type": "1",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "用水桶数",
                    "type": "1",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "喷雾器容量",
                    "type": "1",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~


### 追肥投入
~~~
{
    "status": 200,
    "message": "ok",
    "data": [
        {
            "recordId": 86,
            "name": "自制有机肥",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料制作现场",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "掺入其它肥料",
                    "type": "2",
                    "value": [
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装正面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装反面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "配药现场",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药名称",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "稀释倍数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "用水桶数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "喷雾器容量",
                            "type": "1",
                            "value": ""
                        }
                    ]
                },
                {
                    "score": 0,
                    "unit": "kg",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料施用现场",
                    "type": "1",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "kg",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料用量",
                    "type": "1",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        },
        {
            "recordId": 87,
            "name": "商品有机肥",
            "content": [
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料外包装正面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料外包装反面",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "掺入其它肥料",
                    "type": "2",
                    "value": [
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装正面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药外包装反面",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "配药现场",
                            "type": "0",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "农药名称",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "稀释倍数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "用水桶数",
                            "type": "1",
                            "value": ""
                        },
                        {
                            "score": 0,
                            "unit": "",
                            "flag": "0",
                            "createTime": "",
                            "name": "喷雾器容量",
                            "type": "1",
                            "value": ""
                        }
                    ]
                },
                {
                    "score": 0,
                    "unit": "",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料施用现场",
                    "type": "0",
                    "value": ""
                },
                {
                    "score": 0,
                    "unit": "kg",
                    "flag": "0",
                    "createTime": "",
                    "name": "肥料用量",
                    "type": "1",
                    "value": ""
                }
            ],
            "time":"2018-03-01",
            "status": 0
        }
    ]
}
~~~


#### 上传单个文件接口

请求路径 /api/upload/single

请求方式 POST

**请求参数**

 参数  | 类型  | 是否必填 |示例 | 描述
 |---- | ----- | ------  | ------| ------
 file  | MultipartFile | 是 | | 文件对象

 
**响应参数**

 参数  | 类型   | 描述
 |---- | ----- | ------
 | url | String | 请求路径
 
  
  
#### 获取用户信息

请求路径 /api/user/info

请求方式 GET

**请求参数**

参数  | 类型  | 是否必填 |示例 | 描述
|---- | ----- | ------  | ------| ------
无


**响应参数**

参数  | 类型   | 描述
|---- | ----- | ------
| id | String | 用户名
| mobileNumber | String | 手机号
| realName | String | 真实姓名
| sex | String | 性别
| email | String | 邮箱
| sex | String | 性别
| idCardImage  | String | 身份证照片
| farmImage | String | 农场照片
| businessLicense | String | 营业执照
| qualification | String | 资质认证
| answerScore | Integer | 答题分数
| farm | object | 农场信息
| dataIntegrity | String | 完善度

farm

参数  | 类型   | 描述
|---- | ----- | ------
| id | Long | 农场id
| name | String | 农场名称
| area | Integer | 面积
| score | Integer | 分数



#### 更新用户默认农场id

请求路径 /api/user/defaultFarm

请求方式 POST

**请求参数**

参数  | 类型  | 是否必填 |示例 | 描述
|---- | ----- | ------  | ------| ------
farmId  | String | 是 | 1 | 农场id


**响应参数**

参数  | 类型   | 描述
|---- | ----- | ------
无

#### 完善信息

请求路径 /api/user/update

请求方式 POST

**请求参数**

参数  | 类型  | 是否必填 |示例 | 描述
|---- | ----- | ------  | ------| ------
| id | String | 是 | "h1761103091"|用户名
| idCardImage  | String | 否 ||身份证照片
| farmImage | String | 否 ||农场照片
| businessLicense | String | 否 ||营业执照
| qualification | String | 否 ||资质认证
| photo | String | 否 ||头像


**响应参数**

参数  | 类型   | 描述
|---- | ----- | ------

  
#### 获取当前用户农场集合

请求路径 /api/user/farm

请求方式 GET

**请求参数**

参数  | 类型  | 是否必填 |示例 | 描述
|---- | ----- | ------  | ------| ------
无


**响应参数**

参数  | 类型   | 描述
|---- | ----- | ------
| id | Long | 农场id
| name | String | 农场名称
| area | Integer | 面积
| score | Integer | 分数


#### 获取当前用户农场集合

请求路径 /api/user/farm/{farmId}

请求方式 GET

**请求参数**

参数  | 类型  | 是否必填 |示例 | 描述
|---- | ----- | ------  | ------| ------
无


**响应参数**

参数  | 类型   | 描述
|---- | ----- | ------
| id | Long | 农场id
| name | String | 农场名称
| area | Integer | 面积
| score | Integer | 分数
| updateTime | String | 截止时间    
| coverArea | String | 覆盖面积    
| assessCount | Integer | 健康评估报告   

  
#### 获取当前农场下所有品种作物信息

请求路径 /api/user/cropVariety

请求方式 GET

**请求参数**

参数  | 类型  | 是否必填 |示例 | 描述
|---- | ----- | ------  | ------| ------
farmId  | String | 是 | 1 | 农场id
cropId  | String | 是 | 1 | 作物id
status  | String | 是 | 1 | 状态 
plotId  | String | 是 | 1 | 地块Id


**响应参数**

参数  | 类型   | 描述
|---- | ----- | ------
| id | Long | 作物品种id
| plotId | Long | 地块id
| cropId | Long | 作物id
| cropVariety  | String | 品种名称
| plotName  | String | 地块名称
| batchTime  | String | 批次时间
| score  | Integer | 作物品种得分
| image  | String | 图片

 
 #### 农场评估列表
 
 请求路径 /api/user/farmAssess
 
 请求方式 GET
 
 **请求参数**
 
 参数  | 类型  | 是否必填 |示例 | 描述
 |---- | ----- | ------  | ------| ------
 farmId  | Long | 是 | 1 | 农场id
 
 
 **响应参数**
 
 参数  | 类型   | 描述
 |---- | ----- | ------
 | farmId | Long | 农场id
 | farmName | String | 农场名称
 | score | Integer | 当前得分
 | remark  | String | 备注信息
 | createTime  | String | 创建时间
 | updateTime  | String | 更新时间
 
 
 #### 获取地块信息集合
  
  请求路径 /api/user/plot
  
  请求方式 GET
  
  **请求参数**
  
  参数  | 类型  | 是否必填 |示例 | 描述
  |---- | ----- | ------  | ------| ------
  farmId  | Long | 是 | 1 | 农场id
  plotType  | Integer | 是 | 1 | 0-暖棚 1-冷棚
  
  
  **响应参数**
  
  参数  | 类型   | 描述
  |---- | ----- | ------
  | id | Long | 地块id
  | name | String | 地块名称
 
 
 #### 保存农事环节记录
  
  请求路径 api/record/add
  
  请求方式 POST
  
  **请求参数**
  
  参数  | 类型  | 是否必填 |示例 | 描述
  |---- | ----- | ------  | ------| ------
  recordId  | Long | 是 | 1 | 记录
  content  | String | 是 | "" | 内容
  date  | String | 是 | "2018-03-01" | 时间
  
  例子
  
  ~~~
  
  [{
  	"createTime": "2019-05-01",
  	"flag": “0”,
  	"key": "保存图片",
  	"score": 3,
  	"type": “0”,
  	"unit": "",
  	"value": "a.jpg"
  }, {
  	"createTime": "2019-05-01",
  	"flag": “1",
  	"key": "保存文字",
  	"score": 3,
  	"type": “1”,
  	"unit": "米",
  	"value": "30"
  }, {
  	"createTime": "2019-05-01",
  	"flag": “1”,
  	"key": "参入其他肥料",
  	"score": 3,
  	"type": “2”,
  	"unit": "米",
  	"value": [{
  		"createTime": "2019-05-01",
  		"flag": “0”,
  		"key": "保存文字",
  		"score": 3,
  		"type": “1”,
  		"unit": "kg",
  		"value": "50"
  	}, {
  		"createTime": "2019-05-01",
  		"flag": “1”,
  		"key": "保存文字",
  		"score": 3,
  		"type": “1”,
  		"unit": "kg",
  		"value": "50"
  	}]
  }]
  ~~~
  
  **响应参数**
  
  参数  | 类型   | 描述
  |---- | ----- | ------
  | farmId | Long | 农场id
  | farmName | String | 农场名称
  | score | Integer | 当前得分
  | remark  | String | 备注信息
  | createTime  | String | 创建时间
  | updateTime  | String | 更新时间
  
#### 获取农场品种作物详情  
  请求路径 api/user/varietyDetail
  
  请求方式 GET
  
  **请求参数**
  
  参数  | 类型  | 是否必填 |示例 | 描述
  |---- | ----- | ------  | ------| ------
  plotCropId  | Long | 是 | 1 | 品种作物id

  **响应参数**
   
   参数  | 类型   | 描述
   |---- | ----- | ------
   | cropInfo | Object | 品种作物详情
   | timerShaft | Array | 时间轴
   
   
 cropInfo 具体参数
 
    参数  | 类型   | 描述
    |---- | ----- | ------
    | cropVariety | String | 品种作物
    | cropName | String | 作物名称
    | plotName | String | 地块名称
    | score | Integer | 分数

 timerShaft 参数
 
   参数  | 类型   | 描述
     |---- | ----- | ------
     | farmWorkName | String | 农事环节名称
     | createTime | String | 时间
     | plotName | String | 地块名称
     | cropVariety | String | 品种作物名称
     | content | String | 参数内容
     
 content  参数  时间轴 类型只有 0-图片 1-文字
 
  参数  | 类型   | 描述
|---- | ----- | ------
| unit | String | 单位 |
| flag | String | 是否完成标识 0-未完成 1-已完成 |
| createTime | String | 完成时间 “2018-02-02” |
| type | String | 参数类型 0-图片 1-文字 2-有子结构|
| name | String | 文案 |
| value | String | 值 |    
 
            # kexinFarm
# kexinFarm
