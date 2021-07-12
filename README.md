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
- **Habit:** Helping others can become a habit that makes you feel better, so the app can be open any time, and many tasks can be completed throughout the day by the user. Features like the dashboard of self-improvement and ranking  would encourage the user to continue using it.
- **Scope:** Begins like a powerful to-do app, integrating APIs to get information about non-profit organizations that the user can help. Moreover, you can find locations/people that need attention. V1 allow a kind of social network to bind sponsors with volunteers to encourage users to continue building a better world, where the user can post information about the activities that it completed and ask for more help.


## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**
* User can create a new account.
    * User should chose a type of user (volunteer, sponsor, volunteer & sponsor).
* User can login.
* User can logout. 
* The app should allow user persistence.
* User can suggest tasks to the admin. V1 all of them will be automatically acepted.
* User should find tasks to do, ordered by week, month or year depending on their difficulty or time required.
* -> User should be able to find information about non-profit organizations. This information should be obtained by a global API.
* -> User profile should display the dashboard of self-improvement and ranking of the user, based on its performance.
* User can post text information about the tasks that it had completed.
* User can take a picture and post it inside the app.
* User can post information about people that need help, this post will have a button to display information to contact the user to help her or him.
* -> User can post information about places that need help, including the place's Location.
* User can watch posts of other users.
* Sponsors can find information about how to contact volunteers to know more about its altruistic projects.
* User should receive ntifications about new ranking or improvement.


**Optional Nice-to-have Stories**
* Task can be ordered by category / amount of people that completed them / etc.
* User can have insignias based on its performance.
* User can see follow people.
* User can choose to see only post of the users that it follow.
* User can see a list of other user followers.
* User can see a list of their followers and following users.
* User can share publications outside the app to reach out to more people to help any specific project/person.
* User can chose a photo from galery and post it.
* User can define if its publications are in a private timeline or public timeline.
* -> User should be able to find information about places that need help. This information should be obtained by a backend server that is fill for the users.
* Suggested tasks are received by the admin.  
* Admin can add tasks in the same mobile app.
* Admin can edit tasks in the same mobile app.
* Admin can accept and reject tasks in the same mobile app.
* User can have a dashboard that shows the days it completed some task (to see its progress)
* The App allows social networking. (Messaging)
* User can like publications.
* User can find publications by category.
* User can have preferred categories of tasks and subscribe to them. 
* -> Conect the App to receive post of different sources, for example FB API,Twitter API, etc.
* Each month recognition to the volunteer and sponsor of the month.

### 2. Screen Archetypes

* Login Screen (Activity)
        * User can login
        * The app should allow user persistence.
* Registration Screen (Activity)
        * User can create a new account
* Main Activity (Activity)
        * User can logout.
    * HomeFeed Fragment 
        * User can post text information about the tasks that it had completed.
        * User can take a picture and post it inside the app.
        * User can post information about people that need help, this post will have a button to display information to contact the user to help her or him.
        * User can post information about places that need help, including the place's Location.
        * User can watch posts of other users.
    * Todo Activity
        * Todo Fragment
            * User should find tasks to do (next task at the top and highlighted).
        * AddTask Fragment
            * User can suggest tasks to the admin. V1 all of them will be automatically acepted.
        * SuggestedTasks Activity (Optional Story)
                * Suggested tasks are received by the admin.
            * Evaluate Task Fragment (Optional story)
                * Admin can add tasks in the same mobile app.
                * Admin can edit tasks in the same mobile app.
                * Admin can accept and reject tasks in the same mobile app.
    * Nonprofit Organizations Fragment
        * User should be able to find information about non-profit organizations
    * Notifications Fragment
        * User should receive ntifications about new ranking or improvement.
    * Profile Fragment
        * User can have and see its dashboard and ranking, based on its performance. 

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* HomeFeed
* Todo
* Find Organizations
* Notifications
* User Profile

* Todo
* Add a Task
* Notifications2 (just for admins, optional)

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
   * Notifications2 (just for admins, optional)
* Organizations Screen
* Profile Screen
* Add Task
   * Todo
* SuggestedTasks Sceen (optional)
   * Todo
   * EvaluateTask
* EvaluateTask Screen (optional)
   * SuggestedTasks

## Wireframes
<img src="https://github.com/AuroraPrz18/HelpAndoApp/blob/master/Wireframe.jpg" width=600>
