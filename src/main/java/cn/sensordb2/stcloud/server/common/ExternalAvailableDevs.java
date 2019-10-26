package cn.sensordb2.stcloud.server.common;

import io.vertx.core.json.JsonArray;

import java.util.Comparator;
import java.util.Vector;

/*
{
    "zl_cloud": "1.0",
    "result": {
        "owned_devs": [
            {
                "dev_id": 1#,
                "type": 2#,
                "nick_name": 3#,
                "shared_to": [
                    {
                        "user_id": 4#,
                        "nick_name": 5#
                    }
                ]
            }
        ],
        "external_available_devs": [
            {
                "dev_id": 6#,
                "type": 7#,
                "nick_name": 8#,
                "from": {
                    "user_id": 9#,
                    "nick_name": 10#
                }
            }
        ]
    },
    "id": 11#
}

 */
public class ExternalAvailableDevs {
	private Vector<ExternalAvailableDev> devs = new Vector();
	private int size;

	public ExternalAvailableDevs(int size) {
		super();
		this.size = size;
	}

	public ExternalAvailableDevs() {
		super();
	}

    public Vector<ExternalAvailableDev> getDevs() {
        return devs;
    }

    public void addDev(ExternalAvailableDev externalAvailableDev) {
		this.devs.add(externalAvailableDev);
	}
	
	public boolean isFull() {
		return devs.size()==this.size;
	}
	
	public JsonArray toJsonArray() {
        devs.sort(new DevComparator());
		JsonArray result = new JsonArray();
		
		for(ExternalAvailableDev ead: this.devs) {
			result.add(ead.toJsonObject());
		}
		return result;
	}


    class DevComparator implements Comparator<ExternalAvailableDev> {
        @Override
        public int compare(ExternalAvailableDev o1, ExternalAvailableDev o2) {
            if(o1.isOnline()&&!o2.isOnline()) return -1;
            if(!o1.isOnline()&&o2.isOnline()) return 1;

            if(o1.getType()!=o2.getType()) return o1.getType()-o2.getType();
            return o1.getDevId().compareTo(o2.getDevId());
        }
    }



}
