# Fat lining


[![Build Status](https://travis-ci.org/xabgesagtx/fat-lining.svg?branch=master)](https://travis-ci.org/xabgesagtx/fat-lining)

This app helps to keep track of your weight

## Features

* graphic visualisation of weight changes
* set your own goal to achieve
* user management

## Build

* run `./gradlew build`
* the resulting file is a spring boot executable in `build/libs/fat-lining.jar`
* the name of the admin user is `admin`

## Configuration

Configuration can be done via `application.yml` that has to be put next to `fat-lining.jar`

### Example configuration for mysql usage
```
config:
  adminPassword: admin
  adminEmail: test@example.com
spring:
  datasource:
    url: "jdbc:mysql://localhost/fatlining"
    username: fatlining
    password: fatlining
    driver-class-name: com.mysql.jdbc.Driver
```

### Configuration parameters

* config.adminPassword: set the password for the admin user
* config.adminEmail: set the email address for the admin user
* spring.datasource.url: the address of the database
* spring.datasource.username: name of the database user
* spring.datasource.password: password of the database user
