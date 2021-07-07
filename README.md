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
V1. Encourage people to do things that can help to build a better world. It is possible giving you tasks to do every week, month, or year. Moreover, you can find information about causes that you can help to build an environment of comfort and empathy.
V2. Encourage people to do things that can help to build a better world. It is possible giving you tasks to do every week, month, or year. Moreover, you can find information about causes that you can help and find people to connect with to build an environment of comfort and empathy. 


### App Evaluation
- **Category:** Social
- **Mobile:** The mobile experience will be the first launch. Hence, all the functionalities are added to the mobile version. Moreover, it is crucial to have an app to make it easy to use and to be able to send notifications to remind the user to don't lose their tasks. In the same way, the project will be ready to add new features in the future in a faster way, integrating functionalities like camera, location, real-time, etc.
- **Story:** Allows users to be empathetic with the situations that are happening around their environment. Beginning with small assignments the user can create a habit of help people easily and build a better world step by step.
- **Market:** Anyone who cares about improving itself, its life, and other people's lives could enjoy this app. Moreover, it can be used as a guide for altruistic groups to find their next target to help.
- **Habit:** Helping others can become a habit that makes you feel better, so the app can be open any time, and the tasks can be completed throughout the day many times by the user. Features like the dashboard of self-improvement, ranking, and insignias encourage the user to continue using it.
- **Scope:** V1 begins like a powerful to-do app, integrating APIs to get information about locations/organizations/people that need attention. V2 would be a social network to bind sponsors with volunteers to encourage users to continue building a better world. 


## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**
* User can create a new account, login and logout.
* The app should allow user persistence.
* User should find tasks to do, ordered by week, month or year depending on their difficulty or time required.
* User can have preferred categories of tasks and subscribe to them. 
* User should be able to find information about non-profit organizations, or places that need help,. This information should be obtained by a global API. 
* Users can add its own tasks or suggest taks for all the users. 
* Suggested tasks are received by the admin.  Admin can add, edit, accept and reject them. These actions should happen in the same mobile app for V1 and V2.
* User can have and see its insignias or ranking, based on its performance.
* User can have a dashboard that shows the days it completed some task (to see its progress)
    
**Optional Nice-to-have Stories**
* The App allows social networking.
* User can post information about the tasks that it had completed.
* User can post information about places/people that need help.
* User can have a public section where we find ways to contact them or send money to sponsor altruistic projects.
* User can define if its publications are in a private timeline or public timeline.
* User can define its profile as a sponsorship profile or as a volunteer profile.
    * Sponsorship profiles can receive information about volunteer users that are active more frequently, and contact them to give them sponsorship.
* User can share publications outside the app to reach out to more people to help any specific project/person.
* User can see follow people and see its publications in the app.
* User can like publications.
* User can find publications by category.
* User can see a list of their followers and following users.
* User can see a list of other user followers.

### 2. Screen Archetypes

* Login Screen (Activity)
       * User can login
* Registration Screen (Activity)
       * User can create a new account
* Main Activity (Activity)
    * HomeFeed Fragment (Optional)
        * User can see follow people and see its publications in the app. 
        * User can like publications.
        * User can find publications by category.
    * Todo Fragment
        * User should find tasks to do (next task at the top and highlighted), ordered by week, month or year depending on their difficulty or time required.
        * Users can add its own tasks or suggest taks for all the users. 
    * Organizations Fragment
        * User should be able to find information about non-profit organizations or places that need help.
    * Profile Fragment
        * User can have and see its insignias or ranking, based on its performance.
        * User can have a dashboard that shows the days it complete some task (to see its progress)    
* Categories Activity
        * User can have prefered categories of tasks and suscribe to them.
* AddTask Activity
        * Users can add its own tasks or suggest taks for all the users. 
* SuggestedTasks Activity
        * Suggested tasks are received by the admin.
* EvaluateTask Activity
        *  Admin can add, edit, accept and reject them. 
* Follow Activity
    * Followers Fragment
    * Followings Fragment
        * User can see a list of their followers and following users.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* HomeFeed
* Todo
* Find Organizations
* User Profile

* See Followers
* See Followings

**Flow Navigation** (Screen to Screen)

* Login Screen
   => Registration
   => Home feed
* Registration Screen
   => Home feed
* Main Activity
   => HomeFeed
   => Todo
   => Organizations
   => Profile 
* HomeFeed Screen
* Todo Screen
   => AddTask
   => SuggestedTasks (Just admin account)
* Organizations Screen
   => ContactIt (Optional)
* Profile Screen
   => Categories
   => Follow
* Categories Activity
   => Profile Screen
* AddTask Screen
   => Todo
* SuggestedTasks Sceen
   => Todo
   => EvaluateTask
* EvaluateTask Screen
   => SuggestedTasks
* Follow Activity
   => Profile

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

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
