#!/usr/bin/python
import redis
r = redis.Redis(host='115.28.239.71',port=6379,db=0,password='hyringCloud@20151204')
print(r.keys('*'))
