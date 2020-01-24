#!/bin/sh
nohup java -cp "./target/mobile-4.0/mobile-4.0/lib/*:."  com.academy.mobile.Application --spring.config.location=$1
