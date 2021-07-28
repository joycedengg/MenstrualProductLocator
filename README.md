# MenstrualProductLocator

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app would allow college students to quickly find nearby menstrual products, whether they're in bathrooms on campus or donated by students who happen to have one in their backpack.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Health
- **Mobile:** Mobile is extremely important since all users will need the fast convenience of logging in on their phone to quickly find a product.
- **Story:** Creates a community of people on campus who menstruate. Allows them to support each other by donating products to each other during their times of need. Rewards people for logging free products in bathrooms on the map, or for donating a product to someone.
- **Market:** Any college students on campus who menstruate.
- **Habit:** Students always log on whenever they need a product in a public area but do not have one
- **Scope:** Begin with focusing on implementing a map where users can log that they need a menstrual product in a certain building, or log free products in a certain bathroom. Later on, expand into providing resources about menstrual health and products

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

Your app has multiple views
    * Chat/Notification, Map, and Profile screens
    
Your app interacts with a database (e.g. Parse)
    * Use Parse to create users and store their current locations using ParseGeoPoint
    
You can log in/log out of your app as a user
    * Within login screen
    
You can sign up with a new user profile 
    * Within login screen
    
Your app integrates with at least one SDK (e.g. Google Maps SDK, Facebook SDK) or API (that you didn’t learn about in CodePath) 
    * Google Maps SKD
    
Your app uses at least one gesture (e.g. double tap to like, e.g. pinch to scale) 
    * Swiping between fragments
    
Your app uses at least one animation (e.g. fade in/out, e.g. animating a view growing and shrinking)
    * Fragments sliding in
    
Your app incorporates at least external library to add visual polish
    * MotionLayout
    
Your app provides opportunities for you to overcome difficult/ambiguous technical problems
    * 

-------------

* User can log in with college ID/email
* User can create a new account
* User can see the map of nearby products offered in public bathrooms
* User can see the map of nearby students who need a product
* User can log their location on the map to signal they need a product
* User can locate the most nearby products

**Optional Nice-to-have Stories**
* User is notified when they get close to another user who needs a product
* User can accept another user's request for a product
* User can chat with other users in an app-wide chat
* User can find nearest supply based on their location
* User can text the other user to commmunicate where to drop off the product

### 2. Screen Archetypes

* Login Screen
   * User can log in with college ID/email
   * User can create a new account
* Map Screen
   * User can see the map of nearby products offered in public bathrooms
   * User can see the map of nearby students who need a product
   * User can log their location on the map to signal they need a product
   * User can accept another user's request for a product
* Chat Screen
    * User can text the other user to commmunicate where to drop off the product
* Profile Screen
    * User can earn exp points for logging free products
    * User can see their exp level
### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Map
* Profile

**Flow Navigation** (Screen to Screen)

* Login screen
   * Map screen
* Registration screen
    * Map screen
* Chat screen
    * Map screen
* Log request for a product screen
    * Map screen
    * Chat screen
* Log free public products screen
    * Map screen

## Wireframes
[Add picture of your hand sketched wireframes in this section]
![](https://i.imgur.com/0bGn8lN.png)

<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 

### Models
| Property               | Type                                 | Description                                 |
| ---------------------- | ------------------------------------ | ------------------------------------------- |
| user                   | Pointer to User                      | app user                                    |
| userLocation           | ParseGeoPoint Pointer to User        | user's specific location                    |
| freeProduct            | ParseObject                          | free product station                        |
| freeProductLocation    | ParseGeoPoint Pointer to freeProduct | free product location                       |
| buildingLocation       | String                               | user's described location                   |
| productRequest         | ParseObject                          | a user's request for a product              |
| productRequestLocation | ParseGeoPoint Pointer to freeProduct | location for a user's request for a product |
| password               | Pointer to User, String              | user's password                             |
| profilePic             | Pointer to User, File                | user's profile picture                      |
| request                | ParseObject                          | user's request for a product                |
| completed              | Pointer to Request, Boolean          | whether request was completed or not        |


### Networking
* Profile Screen
    * (Read/GET) Query logged in user object
    * (Update/PUT) Update user profile image
* Map Screen
    * (Read/GET) Query location of the user
    * (Read/GET) Query location of the user's request for a product
    * (Read/GET) Query location of free product station
    * (Create/POST) Add location for a free product station
    * (Create/POST) Add location for a user's product request
    * (Delete) Remove user's completed request for a product

- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
