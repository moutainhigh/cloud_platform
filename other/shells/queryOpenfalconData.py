#-*- coding:utf8 -*-

import requests
import time
import json

end = int(time.time()) # 起始时间戳
start = end - 3600  # 截至时间戳 （例子中为查询过去一个小时的数据）

d = {
        "start": start,
        "end": end,
        "cf": "AVERAGE",
        "endpoint_counters": [
            {
#                "endpoint": "product",
                "endpoint": "openfalconAgent",
                "counter": "GUARD/dev=guard_000000105",
            },
            {
                "endpoint": "openfalconAgent",
                "counter": "load.1min",
            },
        ],
}

query_api = "http://product.cloud.rbcloudtech.com:9966/graph/history"
r = requests.post(query_api, data=json.dumps(d))
print r.text

