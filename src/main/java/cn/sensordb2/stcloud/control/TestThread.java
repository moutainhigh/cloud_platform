package cn.sensordb2.stcloud.control;

import com.alibaba.fastjson.JSONObject;

public class TestThread extends Thread{
    private int num = 0;
    JSONObject jsonObject;
    int n;
    public TestThread(JSONObject jsonObject,int n){
        this.jsonObject=jsonObject;
        this.n=n;
    }
    @Override
    public void run(){
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.putAll(jsonObject);
        jsonObject1.put("1",n);
        System.out.println(n);
        System.out.println(jsonObject1);
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("1",9);
        for(int i=0;i<3;i++){
            TestThread testThread = new TestThread(jsonObject,i);
            testThread.run();
        }
    }
}
