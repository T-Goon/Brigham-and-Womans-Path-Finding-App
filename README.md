# Brigham and Women's Path Finding App

A JavaFx application for Brigham and Women's Hospital that allows for path finding around the hospital and submitting service requests. The app also includes many other special features and enhancements.

A very detailed description of all of the app's features can be found in "User Manual.docx".

# Table of Contents

- [Overview](#overview)
  * [Home Screen](#home-screen)
  * [Login and Authentification](#login-and-authentication)
    + [Authentification Levels](#authentication-levels)
      + [Admin](#admin) 
      + [Staff](#staff)
      + [Patient](#patient)
      + [Guest](#guest)
    + [Login](#login)
    + [Register](#register)
  * [Chat Bot](#chat-bot)
  * [COVID Survey](#covid-survey)
    + [Arcade Games](#arcade-games)
      + [Snake](#snake)
      + [Pac-Man](#pac-man)
      + [Breakout](#breakout)
  * [Path Finding](#path-finding)
    + [Google Maps](#google-maps)
    + [Map Editor](#map-editor)
  * [Text-to-Speech](#text-to-speech)
  * [On-Screen-Keyboard](#on-screen-keyboard)
  * [Service Requests](#service-requests)
  * [The Database](#the-database)
- [Development Tools](#development-tools)
- [Cloning this Reo](#cloning-this-repo)
- [Building a jar](#building-a-jar)

# Overview

A brief overview of all of the app's features.

## Home Screen

The first menu the user sees when opening the app.

![image](https://user-images.githubusercontent.com/32044950/120939104-4eb10800-c6e4-11eb-816a-b08a3e237c55.png)

## Login and Authentication

### Authentication Levels

#### Admin
The system admin for the app. These users are able to do anything the other lower authentication levels are able to do and are the only users allowed to edit the account information for other users, edit the hospital map, change the path finding algorithm, and switch the app over to using the remote database. These users are also the only ones allowed to view all service requests globally.

#### Staff
These accounts represent general hospital staff. They have the permissions to do anything the Guest and Patient authentication levels can. These users can also submit service requests and view any submitted service requests that have been assigned to them.

#### Patient
These users represent any patient or visitor of the hospital. These users are allowed to use the hospital path finding, access Google Maps for directions to the hospital, and edit account specific settings like password and text-to-speech enabling. This authentification level is the only one that is able to send COVID surveys into the system.

The only service request these users are allowed to submit is the Emergency service request form.

#### Guest
This authentication level is for users that have not yet logged into an account. They have all of the permissions of the Patient authentification level. There is only one Guest user per session of the app and any account specific infomation related to the Guest user is wiped upon restarting the app.

The Guest user doesn't count has having an account and cannot be edited by admins.

### Login

This is the login menu for users. Face ID on this menu will be toggled on by default and the app will auto-fill your username if your face is detected in the database. Toggling off face ID will clear both the username and password fields.

![image](https://user-images.githubusercontent.com/32044950/120939721-4efed280-c6e7-11eb-98d1-1a6b8532d441.png)

### Register

This is the register menu for new users. Accounts made here all have Patient authentification level by default. Here users must register their face and fill in all text fields in the menu to create the account. Enabling text-to-speech is optional.

An email will be sent when registration is complete.

![image](https://user-images.githubusercontent.com/32044950/120939713-40b0b680-c6e7-11eb-9d4e-e4728cce6980.png)

## Chat Bot

There is a chat bot message window in the bottom right of every screen. You can hold a general conversation with the bot and the messages will be read out to you if text-to-speech is enabled for the account.

![image](https://user-images.githubusercontent.com/32044950/120940465-6f309080-c6eb-11eb-83c5-2f60d07c71e8.png)

The bot can also take you to certian menus and tell you some troubleshooting information if you need it.

![image](https://user-images.githubusercontent.com/32044950/120940522-af900e80-c6eb-11eb-825f-e7d3b511908d.png)

## COVID Survey

Users of authentification level Patient or Guest must fill out a COVID screening survey before using the path finding feature of the application. 

![image](https://user-images.githubusercontent.com/32044950/120940615-29c09300-c6ec-11eb-99d4-eb2fe59508c5.png)

Each survey must be approved by an admin or staff account before the user is able to use the hospital pathfinding. Different entrances of the hospital will be auto-filled by path finding depending on if the user is marked as "SAFE" or "DANGEROUS".

![image](https://user-images.githubusercontent.com/32044950/120940784-f8949280-c6ec-11eb-8168-c31646de2706.png)

The user is then screened by some hospital staff at an entrance and can be marked as accepted or denied access into the hospital.

![image](https://user-images.githubusercontent.com/32044950/120940869-6a6cdc00-c6ed-11eb-96f4-2a6fde8a3636.png)

### Arcade Games

While waiting for the COVID survey to be approved a user can opt to play a few video games.

![image](https://user-images.githubusercontent.com/32044950/120940961-039bf280-c6ee-11eb-823c-c31f0f6989de.png)

![image](https://user-images.githubusercontent.com/32044950/120940925-c9caec00-c6ed-11eb-8e00-002f381f91a2.png)

#### Snake

![image](https://user-images.githubusercontent.com/32044950/120941054-8cb32980-c6ee-11eb-87bc-ca7b8c81a4ea.png)

#### Pac-Man

![image](https://user-images.githubusercontent.com/32044950/120941010-655c5c80-c6ee-11eb-9098-06c026e0cd38.png)

#### Breakout

![image](https://user-images.githubusercontent.com/32044950/120941017-70af8800-c6ee-11eb-8607-a817d9e718f8.png)

## Path Finding

WIP

### Google Maps

WIP

### Map Editor

WIP

## Text-to-Speech

WIP

## On-Screen-Keyboard

WIP

## Service Requests

WIP

## The Database

WIP

# Development Tools
- Java 1.8
- IntelliJ 2020.3.3
- Scene Builder 8.5.0

# Cloning this Repo

There are 2 things you must do after cloning this project to get it to run properly:

1. Account for the Email Feature

Create a directory `src/main/resources/edu/wpi/cs3733/D21/teamB/account`

Within that directory place an `account.txt` file.

The first line of the file is a Gmail email address that the email service will use to send emails.

The second line of the file is the password to that email account.

2. Decompressing the Facenet Model

There is a file `src/main/resources/edu/wpi/cs3733/D21/teamB/faces/pytorch_models/facenet/facenet.zip`.

Extract `facenet.pt` and place it into the same directory as `facenet.zip`.

# Building a jar
To assemble a runnable jar file for the project, run the "shadowjar" gradle task under the "shadow" folder. Gradle will automatically download all dependencies needed to compile your jar file, which will be stored in the build/libs folder.
