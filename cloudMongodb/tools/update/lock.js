var db = connect('hycloudRing');


db.accounts.insert({"userName":"123456","hashedPassword":"0123456789012345678901234567890123456789","type":4,"nickName":"lock_1"});
db.accounts.insert({"userName":"111111","hashedPassword":"0123456789012345678901234567890123456789","type":4,"nickName":"lock_2"});
db.accounts.insert({"userName":"111112","hashedPassword":"0123456789012345678901234567890123456789","type":4,"nickName":"lock_3"});
