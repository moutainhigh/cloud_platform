#!/usr/bin/python
import redis
import sys
r = redis.Redis(host='115.28.239.71',port=6379,db=0,password='hyringCloud@20151204')
#print(r.hgetall('USER:user_test1'))
print(r.hgetall(sys.argv[1]))
