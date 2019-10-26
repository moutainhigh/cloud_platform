package cn.sensordb2.stcloud.server.common;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by lms on 12/24/16.
 */
public class OpenFalconClient extends AbstractVerticle {
    public static OpenFalconClient instance;
    private JsonObject totalSktCntInfoMap = new JsonObject();
//    private Buffer stringBuffer = Buffer.buffer();

    public static OpenFalconClient getInstance() {
        if (instance == null)
            instance = new OpenFalconClient();
        return instance;
    }


    public void run(Vertx vertx) {
        HttpClientOptions httpApiServerOption = new HttpClientOptions();

        HttpClient httpApiClient = vertx.createHttpClient(httpApiServerOption);

        HttpClientRequest httpApiServerRequest = httpApiClient.post(49000, "127.0.0.1",
                "/httpapiserver/api/v1", response -> {
                    //ReactiveReadStream<Buffer> rrs = ReactiveReadStream.readStream()
                    Buffer stringBuffer = Buffer.buffer();

//                    String he = response;
//                    System.out.println(response.toString());
                    response.handler(responseBuffer -> {
                        if (!responseBuffer.toString().contains("\r\n")) {
                            stringBuffer.appendBuffer(responseBuffer);

//                        System.out.println("GET_CURRENT_DEV_LIST SUCCESS!");
                            //System.out.println("hehe" + responseBuffer.toString());
                            //System.out.println("hahahahaha");
                        }


                        else {

                            stringBuffer.appendBuffer(responseBuffer);
                            System.out.println("** Total Info **" + stringBuffer.toString());
                            JsonObject currentSktCntInfoMap = stringBuffer.toJsonObject().getJsonObject("result");
                            System.out.println("** Result Info ** : " + currentSktCntInfoMap);
                            System.out.println("** Map Size** : " + currentSktCntInfoMap.size());

//                        System.out.println("** ValueSize : " + currentSktCntInfoMap.size());


                            System.out.println("\n-> START NEXT PHASE : POST -> agent\n");

                            HttpClientOptions openfalconCliOption = new HttpClientOptions();
                            HttpClient openfalconClient = vertx.createHttpClient(openfalconCliOption);


//                            HttpClientRequest openfalconCliRequest = openfalconClient.post(4001, "127.0.0.1", "/v1/push", aresponse -> {
                            HttpClientRequest openfalconCliRequest = openfalconClient.post(1988, "192.168.5.239", "/v1/push", aresponse -> {
                                response.handler(aresponseBuffer -> {
                                });
                            });

                        /*
                        traverse currentSktCntInfo:
                        1. new in totalMap - add and set alive
                        2. old in totalMap - only set the alive(the info in current) connection true
                         */

//                      System.out.println("** value : " + currentSktCntInfoMap.size());
                            Iterator currentMapIter = currentSktCntInfoMap.getMap().keySet().iterator();
                            //        Iterator currentMapIter = currentSktCntInfoMap.keySet().iterator();

                            int j = 0;
                            while (currentMapIter.hasNext()) {

//                                System.out.println(j++);
                                String key = (String) currentMapIter.next();
//                            System.out.println("\n");
                                //System.out.println(currentSktCntInfoMap.getString(key));
                                if (!totalSktCntInfoMap.containsKey(key)) {
                                    currentSktCntInfoMap.getJsonObject(key).put("isAlive", true);
                                    totalSktCntInfoMap.put(key, currentSktCntInfoMap.getJsonObject(key));
                                } else {
                                    totalSktCntInfoMap.getJsonObject(key).put("isAlive", true);
                                    //totalSktCntInfoMap.get(key).setIsAlive(true);
                                }
                            }


                           /*
                            endpoint string
                            metric string
                            timestamp int // Unix timestamp
                            step int
                            value float64
                            counterType string
                            tags string
                        */
                            Vector<JsonObject> jsonData = new Vector<>();
                            Iterator totalMapIter = totalSktCntInfoMap.getMap().keySet().iterator();
                            while (totalMapIter.hasNext()) {
                                String totalInfoMapKey = (String) totalMapIter.next();
                                JsonObject connectionInfoObject = totalSktCntInfoMap.getJsonObject(totalInfoMapKey);
                                JsonObject data = new JsonObject();
                                data.put("endpoint", "ubuntu");
                                data.put("metric", totalSktCntInfoMap.getJsonObject(totalInfoMapKey).getString("accountType"));
                                data.put("timestamp", System.currentTimeMillis() / 1000);
                                data.put("step", 60);


                                if (connectionInfoObject.getBoolean("isAlive")) {
                                    data.put("value", 1);
                                    // set all connection closed
                                    totalSktCntInfoMap.getJsonObject(totalInfoMapKey).put("isAlive", false);
                                } else {
                                    data.put("value", 0);
                                }
                                data.put("counterType", "GAUGE");
                                data.put("tags", "dev=" + totalInfoMapKey);
                                jsonData.add(data);

                            }

                            //print jsonData
                            System.out.println("**********************************************************************");
                            System.out.println();
                            System.out.println(String.format("TIME: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                            for (int i = 0; i < jsonData.size(); i++) {
                                System.out.println("KEY: " + jsonData.get(i).getString("tags") + "  VALUE : " + jsonData.get(i).getFloat("value") + " --> " + jsonData.get(i).toString());
                            }
                            System.out.println();
                            System.out.println("**********************************************************************\n\n\n");
                            openfalconCliRequest.end(jsonData.toString());
                        }
                    });

                });



        httpApiServerRequest.putHeader("userName", "userID_1a");
        httpApiServerRequest.putHeader("hashedPassword", "1");
        httpApiServerRequest.putHeader("Content-Type", "application/json;charset=utf-8");

        // only for the lock
        httpApiServerRequest.putHeader("Content-length", String.valueOf(71));

        JsonObject requestBody = new JsonObject();
        requestBody.put("zl_cloud", "1.0");
        requestBody.put("id", 1);
        requestBody.put("method", "GET_CURRENT_DEV_LIST");
        requestBody.put("params", (JsonObject) null);



//      System.out.println(requestBody.toString());
        httpApiServerRequest.write(requestBody.toString());
        //{"zl_cloud":"1.0","id":1,"method":"GET_CURRENT_DEV_LIST","params":null}
        httpApiServerRequest.end();

    }

    public static void main(String[] args) throws InterruptedException {
        OpenFalconClient op = new OpenFalconClient();
        Vertx vertx = Vertx.vertx();

        while (true) {
            op.run(vertx);
            //heartbeat time: 60s
            try {
                Thread.sleep(1000 * 20);
            } catch (InterruptedException e) {
                e.getStackTrace();
            }

        }
    }
}
