Original App Design Project 
===

# HelpAndo App

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Encourage people to do things that can help to build a better world. It is possible giving you tasks to do every week, month, or year. Moreover, you can find information about causes that you can help and find people to connect with to build an environment of comfort and empathy. 


### App Evaluation
- **Category:** Social
- **Mobile:** The mobile experience will be the first launch. Hence, all the functionalities are added to the mobile version. Moreover, it is crucial to have an app to make it easy to use and to be able to send notifications to remind the user to don't lose their tasks. In the same way, the project integrates functionalities like camera, location, etc.
- **Story:** Allows users to be empathetic with the situations that are happening around their environment. Beginning with small assignments the user can create a habit of help people easily and build a better world step by step.
- **Market:** Anyone who cares about improving itself, its life, and other people's lives could enjoy this app. Moreover, it can be used as a guide for altruistic groups to find their next target to help and share their outcomes.
- **Habit:** Helping others can become a habit that makes you feel better, so the app can be open any time, and the tasks can be completed throughout the day many times by the user. Features like the dashboard of self-improvement, ranking, and insignias encourage the user to continue using it.
- **Scope:** Begins like a powerful to-do app, integrating APIs to get information about non-profit organizations that the user can help. Moreover, you can find locations/people that need attention. V1 allow a kind of social network to bind sponsors with volunteers to encourage users to continue building a better world, where the user can post information about the activities that it completed and ask for more help.


## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**
* User can create a new account, login and logout.
* User should be chose a type of user (volunteer, sponsor, volunteer & sponsor)
* The app should allow user persistence.
* User should find tasks to do, ordered by week, month or year depending on their difficulty or time required.
* -> User should be able to find information about non-profit organizations, or places that need help. This information should be obtained by a global API. (ACTON REQUIRED: Seek API needed)
* Users can suggest tasks to the admin. 
* Suggested tasks are received by the admin.  Admin can add, edit, accept and reject them. These actions should happen in the same mobile app for V1.
* User can have and see its insignias or ranking, based on its performance.
* -> User can post information about the tasks that it had completed and photos (photos can come from galery).
* -> User can post information about places/people that need help. (It can include Location, with Google Maps API)
* Sponsors can contact volunteers to know more about its altruistic projects and probably give money to them.
* User can define if its publications are in a private timeline or public timeline.
* User can share publications outside the app to reach out to more people to help any specific project/person.
* User can see follow people and see its publications in the app.
    
**Optional Nice-to-have Stories**
* User can have a dashboard that shows the days it completed some task (to see its progress)
* The App allows social networking. (Messaging)
* User can like publications.
* User can find publications by category.
* User can see a list of their followers and following users.
* User can have preferred categories of tasks and subscribe to them. 
* User can see a list of other user followers.
* -> Conect the App to receive post of different sources, for example FB API,Twitter API, etc.
* Each month recognition to the volunteer and sponsor of the month

### 2. Screen Archetypes

* Login Screen (Activity)
       * User can login
* Registration Screen (Activity)
       * User can create a new account
* Main Activity (Activity)
    * HomeFeed Fragment (Optional)
        * User can see follow people and see its publications in the app. 
        * User can like publications. (optional)
        * User can find publications by category.(optional)
    * Todo Fragment
        * User should find tasks to do (next task at the top and highlighted), ordered by week, month or year depending on their difficulty or time required.
        * Users can suggest taks for all the users. 
    * Nonprofit Organizations Fragment
        * User should be able to find information about non-profit organizations or places that need help.
    * Notifications Fragment
    * Profile Fragment
        * User can have and see its insignias or ranking, based on its performance.
        * User can have a dashboard that shows the days it complete some task (to see its progress)(optional) 
* SuggestedTasks Activity
        * Suggested tasks are received by the admin.
* EvaluateTask Activity
        *  Admin can add, edit, accept and reject them. 
* Follow Activity
    * Followers Fragment (optional)
    * Followings Fragment (optional)
        * User can see a list of their followers and following users.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* HomeFeed
* Todo
* Find Organizations
* Notifications
* User Profile

* Todo
* Suggest a Task
* Notifications2 (just for admins)

* See Followers
* See Followings

**Flow Navigation** (Screen to Screen)

* Login Screen
   * Registration
   * MainActivity
* Registration Screen
   * Login
* Main Activity
   * HomeFeed
   * Todo
   * Organizations
   * Notifications
   * Profile 
* HomeFeed Screen
* Todo Screen
   * Todo
   * SuggestTasks
   * Notifications2 (just for admins)
* Organizations Screen
   * ContactIt (Optional)
* Profile Screen
* Suggest Task
   * Todo
* SuggestedTasks Sceen
   * Todo
   * EvaluateTask
* EvaluateTask Screen
   * SuggestedTasks

## Wireframes
Hand sketched wireframe only with main views
<img src="https://github.com/AuroraPrz18/HelpAndoApp/blob/master/Wireframe.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
