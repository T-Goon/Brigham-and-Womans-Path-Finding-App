# Brigham and Women's Path Finding App

A JavaFx application for Brigham and Women's Hospital that allows for path finding around the hospital and submitting service requests. The app also includes many other special features and enhancements.

A very detailed description of all of the app's features can be found in "User Manual.docx".

Download the Jar: https://github.com/T-Goon/Brigham-and-Womans-Path-Finding-App/releases/tag/1.1

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
  * [Text-to-Speach](#text-to-speech)
  * [On-Screen-Keyboard](#on-screen-keyboard)
  * [Service Requests](#service-requests)
  * [The Database](#the-database)
- [Development Tools](#development-tools)
- [Cloning this Repo](#cloning-this-repo)
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

There are 3 built in accounts created for development purposes that can be used:
- Admin
  - Username: admin
  - Password: admin
- Staff
  - Username: staff
  - Password: staff
- Patient
  - Username: patient
  - Password: patient

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

In the path finding menu users are able to find directions to anywhere in the hospital regardless of floor. Users are able to put intermediate stops into their path and the direction of the path is animated by a moving blue ball. There are also options such as Limited Mobility Pathing that avoids stairs, finding the closest restroom, entrance, etc., favoriting locations, and selecting locations from a sorted category dropdown list.

![image](https://user-images.githubusercontent.com/32044950/121082256-d4e45180-c7ab-11eb-8dcd-1bc9fafcb638.png)

There is also an option to view textual directions which can be emailed to the currently logged in user. The textual directions can be stepped through and if text-to-speech is enabled for the user, the directions will be read out loud.

![image](https://user-images.githubusercontent.com/32044950/121084502-b59af380-c7ae-11eb-9303-55333c53ae5c.png)

### Google Maps

For path finding to the hospital there is Google Maps integration.

![image](https://user-images.githubusercontent.com/32044950/121084746-06aae780-c7af-11eb-90b0-ce14c0a0a6cb.png)

### Map Editor

Editing the map of the hospital is an Admin account only feature and allows for the editing of the different nodes and edges of the hospital map. The Admin can also change the path finding algorithm they system is using if needed. Nodes on the map can be added, updated, and deleted at will. Also, the color of the nodes can be changed. A node's location can be changed by dragging it around the map and a group of nodes can be aligned by holding CTRL and clicking on any number of nodes. New nodes can also be placed within an edge, bisecting that edge. Lastly, different maps can be loaded and saved using .csv files.

![image](https://user-images.githubusercontent.com/32044950/121085027-5a1d3580-c7af-11eb-8091-bc67e2889e18.png)

![image](https://user-images.githubusercontent.com/32044950/121085882-71a8ee00-c7b0-11eb-93c1-7e00bfe97a00.png)

![image](https://user-images.githubusercontent.com/32044950/121085814-5342f280-c7b0-11eb-9e17-ae8382115c8d.png)

## Text-to-Speech

Within a settings menu there is an option to turn on the text-to-speech setting for the account. When text-to-speech is on users using TAB to nagivate through the app will be able to hear the element that is currently selected being spoken out to them.

![image](https://user-images.githubusercontent.com/32044950/121086625-630f0680-c7b1-11eb-8e8c-63d5a8287fb4.png)

## On-Screen-Keyboard

Within a settings menu there is an option to turn on the on-screen-keyboard. When toggled on a draggable keyboard will appear on screen and users will be able to click on keys to type into text fields.

![image](https://user-images.githubusercontent.com/32044950/121086661-6efac880-c7b1-11eb-82bb-f76bd3a5701c.png)

## Service Requests

A variety of service requests can be sent by Admin and Staff accounts. These service requests show up in a database after submission and can be assigned to Staff accounts to complete by Admin accounts.

![image](https://user-images.githubusercontent.com/32044950/121086915-c7ca6100-c7b1-11eb-8b38-374166b2aaf9.png)

![image](https://user-images.githubusercontent.com/32044950/121086955-d1ec5f80-c7b1-11eb-993e-145694fe4081.png)

![image](https://user-images.githubusercontent.com/32044950/121087037-ec263d80-c7b1-11eb-8539-bc40ef7ab2fd.png)

## The Database

The app has both embedded and remote database support. Admin accounts can switch the app over to the remote database in the settings menu.

![image](https://user-images.githubusercontent.com/32044950/121087280-3b6c6e00-c7b2-11eb-9cbc-19d31dc8ff9f.png)

# Development Tools
- Java 1.8
- IntelliJ 2020.3.3
- Scene Builder 8.5.0

Any code dependencies are handled by Gradle.

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
