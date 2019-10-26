var db = connect('hycloudRing');
db.dropDatabase();

db.accounts.insert({"userName":"userID_1","hashedPassword":"1","type":1,"nickName":"userID_1"});
db.accounts.insert({"userName":"userID_1a","hashedPassword":"1","type":1,"nickName":"userID_1a"});
db.accounts.insert({"userName":"userID_1b","hashedPassword":"1","type":1,"nickName":"userID_1b"});
db.accounts.insert({"userName":"userID_1c","hashedPassword":"1","type":1,"nickName":"userID_1c"});
db.accounts.insert({"userName":"userID_2","hashedPassword":"1","type":1,"nickName":"userID_2"});
db.accounts.insert({"userName":"userID_2a","hashedPassword":"1","type":1,"nickName":"userID_2a"});
db.accounts.insert({"userName":"userID_2b","hashedPassword":"1","type":1,"nickName":"userID_2b"});
db.accounts.insert({"userName":"userID_2c","hashedPassword":"1","type":1,"nickName":"userID_2c"});


db.accounts.insert({"userName":"router_1","hashedPassword":"1","type":2,"nickName":"router_1"});
db.accounts.insert({"userName":"router_2","hashedPassword":"1","type":2,"nickName":"router_2"});
db.accounts.insert({"userName":"router_3","hashedPassword":"1","type":2,"nickName":"router_3"});
db.accounts.insert({"userName":"router_4","hashedPassword":"1","type":2,"nickName":"router_4"});


db.accounts.insert({"userName":"ring_1","hashedPassword":"1","type":3,"nickName":"ring_1"});
db.accounts.insert({"userName":"ring_2","hashedPassword":"1","type":3,"nickName":"ring_2"});
db.accounts.insert({"userName":"ring_3","hashedPassword":"1","type":3,"nickName":"ring_3"});
db.accounts.insert({"userName":"ring_4","hashedPassword":"1","type":3,"nickName":"ring_4"});

db.accounts.insert({"userName":"lock_1","hashedPassword":"1","type":4,"nickName":"lock_1"});
db.accounts.insert({"userName":"lock_2","hashedPassword":"1","type":4,"nickName":"lock_2"});
db.accounts.insert({"userName":"lock_3","hashedPassword":"1","type":4,"nickName":"lock_3"});
db.accounts.insert({"userName":"lock_4","hashedPassword":"1","type":4,"nickName":"lock_4"});

db.accounts.insert({"userName":"gate_1","hashedPassword":"1","type":5,"nickName":"gate_1"});
db.accounts.insert({"userName":"gate_2","hashedPassword":"1","type":5,"nickName":"gate_2"});
db.accounts.insert({"userName":"gate_3","hashedPassword":"1","type":5,"nickName":"gate_3"});
db.accounts.insert({"userName":"gate_4","hashedPassword":"1","type":5,"nickName":"gate_4"});

//lock gate binding

db.ringConfig.insert({"dev_id":"ring_1","human_check":1,"lamp_switch":1,"warning_time":5,"cont_cap_num":1,"ring_tone":3,"ring_volumn":4});
db.ringConfig.insert({"dev_id":"ring_2","human_check":1,"lamp_switch":1,"warning_time":10,"cont_cap_num":2,"ring_tone":3,"ring_volumn":4});
db.ringConfig.insert({"dev_id":"ring_3","human_check":1,"lamp_switch":1,"warning_time":15,"cont_cap_num":3,"ring_tone":3,"ring_volumn":4});
db.ringConfig.insert({"dev_id":"ring_4","human_check":1,"lamp_switch":1,"warning_time":20,"cont_cap_num":4,"ring_tone":3,"ring_volumn":4});


db.accountDevice.insert({"userName":"userID_1a","devID":"router_1","relationShip":1,"nickName":"1"});
db.accountDevice.insert({"userName":"userID_1a","devID":"ring_1","relationShip":1,"nickName":"1"});
db.accountDevice.insert({"userName":"userID_2a","devID":"router_2","relationShip":1,"nickName":"1"});
db.accountDevice.insert({"userName":"userID_2a","devID":"ring_2","relationShip":1,"nickName":"1"});

//
db.accountDevice.insert({"userName":"userID_1b","devID":"ring_1","relationShip":2,"nickName":"1"});
db.accountDevice.insert({"userName":"userID_1c","devID":"ring_1","relationShip":2,"nickName":"2"});
db.accountDevice.insert({"userName":"userID_2b","devID":"ring_1","relationShip":2,"nickName":"3"});
db.accountDevice.insert({"userName":"userID_2c","devID":"ring_1","relationShip":2,"nickName":"4"});


//temp devs
db.accounts.insert({"userName":"router_temp","hashedPassword":"1","type":2,"nickName":"router_temp"});
db.accounts.insert({"userName":"ring_temp","hashedPassword":"1","type":3,"nickName":"ring_temp"});


//ring_1
db.ringImages.insert({"date_time": "2015-10-10 00:00:00","dev_id": "ring_1","images":[{"path": "1.jpg"}],"type":"WARN"});
db.ringImages.insert({"date_time": "2015-10-10 12:00:00","dev_id": "ring_1","images":[{"path": "2.jpg"},{"path": "3.jpg"}],"type":"WARN"});
db.ringImages.insert({"date_time": "2015-10-11 01:00:00","dev_id": "ring_1","images":[{"path": "4.jpg"},{"path": "5.jpg"},{"path": "6.jpg"}],"type":"WARN"});

//ring_2
db.ringImages.insert({"date_time": "2015-10-11 00:00:00","dev_id": "ring_2","images":[{"path": "1.jpg"}],"type":"WARN"});
db.ringImages.insert({"date_time": "2015-10-11 12:00:00","dev_id": "ring_2","images":[{"path": "2.jpg"},{"path": "3.jpg"}],"type":"WARN"});
db.ringImages.insert({"date_time": "2015-10-12 01:00:00","dev_id": "ring_2","images":[{"path": "4.jpg"},{"path": "5.jpg"},{"path": "6.jpg"}],"type":"WARN"});

//ring_3
db.ringImages.insert({"date_time": "2015-10-12 00:00:00","dev_id": "ring_3","images":[{"path": "1.jpg"}],"type":"WARN"});
db.ringImages.insert({"date_time": "2015-10-12 12:00:00","dev_id": "ring_3","images":[{"path": "2.jpg"},{"path": "3.jpg"}],"type":"WARN"});
db.ringImages.insert({"date_time": "2015-10-13 01:00:00","dev_id": "ring_3","images":[{"path": "4.jpg"},{"path": "5.jpg"},{"path": "6.jpg"}],"type":"WARN"});

//ring_4
db.ringImages.insert({"date_time": "2015-10-14 00:00:00","dev_id": "ring_4","images":[{"path": "1.jpg"}],"type":"WARN"});
db.ringImages.insert({"date_time": "2015-10-14 12:00:00","dev_id": "ring_4","images":[{"path": "2.jpg"},{"path": "3.jpg"}],"type":"WARN"});
db.ringImages.insert({"date_time": "2015-10-15 01:00:00","dev_id": "ring_4","images":[{"path": "4.jpg"},{"path": "5.jpg"},{"path": "6.jpg"}],"type":"WARN"});
