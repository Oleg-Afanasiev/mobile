#!/bin/sh
nohup java -cp "./target/mobile-4.0/mobile-4.0/lib/*:."  com.academy.mobile.Application --spring.config.location=file:./target/mobile-4.0/mobile-4.0/cfg/application.properties
