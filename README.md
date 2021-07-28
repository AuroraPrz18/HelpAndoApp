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

* [X] User can create a new account.
    * [X] User should choose a type of user (volunteer or sponsor).
* [X] User can login.
* [X] User can logout.
* [X] The app should allow user persistence.
* [X] User can suggest tasks to the admin. V1 all of them will be automatically accepted.
* [X] User should find tasks to do, ordered by week, month or year depending on their difficulty or time required.
* [/] -> User should be able to find information about non-profit organizations. This information should be obtained by a global API.
* [X] -> User profile should display the dashboard of self-improvement based on its performance.
* [X] User profile should display the total points of the user, based on its performance.
* [X] User can post text information about the tasks that it had completed.
* [X] User can take a picture and post it inside the app.
* [X] User can post information about people that need help, this post will have a button to display information to contact the user to help her or him.
* [X] -> User can post information about places that need help, including the place's Location.
* [X] User can watch posts of other users.
* [X] Sponsors can find information about how to contact volunteers to know more about its altruistic projects.
* [X] The app integrates with at least one SDK or API
* [ ] The app uses at least one gesture (e.g. double tap to like, e.g. pinch to scale)
* [X] The app uses at least one animation (e.g. fade in/out, e.g. animating a view growing and shrinking)
* [ ] The app incorporates at least one external library to add visual polish

API used to obtain nonprofit organizations data: https://www.globalgiving.org/api/


**Optional Nice-to-have Stories**
* [ ] User can have insignias and ranking based on its performance.
* [ ] User should receive notifications about new ranking or improvement.
* [ ] User can change its profile picture
* [ ] Task can be ordered by category / amount of people that completed them / etc.
* [ ] User can see follow people.
* [ ] User can choose to see only post of the users that it follow.
* [ ] User can see a list of other user followers.
* [ ] User can see a list of their followers and following users.
* [ ] User can share publications outside the app to reach out to more people to help any specific project/person.
* [X] User can chose a photo from gallery and post it.
* [ ] User can define if its publications are in a private timeline or public timeline.
* [ ] -> User should be able to find information about places that need help. This information should be obtained by a backend server that is fill by the users.
* [ ] Suggested tasks are received by the admin.
* [ ] Admin can add tasks in the same mobile app.
* [ ] Admin can edit tasks in the same mobile app.
* [ ] Admin can accept and reject tasks in the same mobile app.
* [ ] User can have a dashboard that shows the days it completed some task (to see its progress)
* [ ] The App allows social networking. (Messaging)
* [ ] User can like publications.
* [ ] User can find publications by category.
* [ ] User can have preferred categories of tasks and subscribe to them.
* [ ] -> Connect the App to receive post of different sources, for example FB API,Twitter API, etc.
* [ ] Each month recognition to the volunteer and sponsor of the month.

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
    * Notifications Fragment (optional)
        * User should receive notifications about new ranking or improvement.
    * Profile Fragment
        * User can have and see its dashboard and points, based on its performance.

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
* SuggestedTasks Screen (optional)
   * Todo
   * EvaluateTask
* EvaluateTask Screen (optional)
   * SuggestedTasks

## Wireframes
<img src="https://github.com/AuroraPrz18/HelpAndoApp/blob/master/Wireframe.jpg" width=600>

## Schema

### Models
Model: User
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | userName      | String   | unique username for the user |
   | name          | String   | user's name |
   | profilePicture| File     | picture that will be display as the profile picture |
   | password      | String   | string to login |
   | type          | String   | define if the user is a volunteer or a sponsor |
   | isAdmin       | Boolean  | define if the user has admin privileges |

Model: Task
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the task (default field) |
   | name          | String   | unique name for the task |
   | description   | String   | description for the task |
   | points        | Number   | points that the user will get when he/she complete this task |
   | category      | String   | define the category of the task |
   | isApproved    | Boolean  | define if this task has been approved by an admin |

Model: TaskCompleted
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the task (default field) |
   | user          | Pointer to User | user that completed this post |
   | task          | Pointer to Task | task completed by the user |
   | completedDate | DateTime | when this user completed this task (default field) |

Model: Post
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the post (default field) |
   | author        | Pointer to User | user that wrote this post |
   | image         | File     | image posted |
   | location      | String   | location posted |
   | contactInfo   | Pointer to Contact | info to contact and help |
   | text          | String   | text posted |
   | createdAt     | DateTime | date when post is created (default field) |

Model: Contact
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | name          | String   | name of the contact person to help |
   | telephone     | String   | telephone number where you can send message to help |
   | text          | String   | extra info about how to contact |


### Networking
* Login Screen (Activity)
    * (Read/GET) - To log in into a user account
   ```
    ParseUser.logInInBackground("<userName>", "<password>", (user, e) -> {
        if (user != null) {
            // Hooray! The user is logged in.
        } else {
            // Login failed. Look at the ParseException to see what happened.
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    ```
* Registration Screen (Activity)
    * (Create/POST) - To signing up a new user
    ```
    ParseUser user = new ParseUser();
    user.setUsername("my name");
    user.setPassword("my pass");
    user.setEmail("email@example.com");
    // Other fields can be set just like any other ParseObject,
    // using the "put" method, like this: user.put("attribute", "its value");
    // If this field does not exists, it will be automatically created
    user.signUpInBackground(e -> {
        if (e == null) {
              // Hooray! Let them use the app now.
        } else {
             // Sign up didn't succeed. Look at the ParseException
             // to figure out what went wrong
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    ```
* Main Activity (Activity)
    * HomeFeed Fragment
        * (Create/POST) - To create a new Post
          ```
          ParseObject entity = new ParseObject("Post");

          entity.put("author", ParseUser.getCurrentUser());
          entity.put("image", new ParseFile("resume.txt", "My string content".getBytes()));
          entity.put("location", "A string");
          entity.put("contactInfo", new ParseObject("Contact"));
          entity.put("text", "A string");

          // Saves the new object.
          // Notice that the SaveCallback is totally optional!
          entity.saveInBackground(e -> {
            if (e==null){
              //Save was done
            }else{
              //Something went wrong
              Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          });
          ```
         * (Create/POST) - To create a new Contact
         ```
      ParseObject entity = new ParseObject("Contact");

      entity.put("name", "A string");
      entity.put("telephone", "A string");
      entity.put("text", "A string");

      // Saves the new object.
      // Notice that the SaveCallback is totally optional!
      entity.saveInBackground(e -> {
        if (e==null){
          //Save was done
        }else{
          //Something went wrong
          Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });
         ```
        * (Read/GET) - To get a list of any object (it will be needed to retrieve all the posts)
        ```
          // Creates a new ParseQuery object to help us fetch MyCustomClass objects
          ParseQuery<ParseObject> query = ParseQuery.getQuery("MyCustomClass");

          // Fetches data synchronously
          try {
            List<ParseObject> results = query.find();
            for (ParseObject result : results) {
                System.out.println("Object found " + result.getObjectId());
            }
          } catch (ParseException e) {
            e.printStackTrace();
          }

          // Or use the the non-blocking method findInBackground method with a FindCallback
         query.findInBackground((results, e) -> {
            // do some stuff with results
            if (e == null) {
              for (ParseObject result : results) {
                Log.d("Object found",result.getObjectId());
              }
            } else {
                Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          });
        ```
        * (Read/GET) - To retrieve a Contact object
        ```
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Contact");

          // The query will search for a ParseObject, given its objectId.
          // When the query finishes running, it will invoke the GetCallback
          // with either the object, or the exception thrown
          query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
                if (e == null) {
                //Object was successfully retrieved
              } else {
                // something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
              }
          });
        ```
    * Todo Activity
        * Todo Fragment
            * (Read/GET) - To get a list of any object (it will be needed to retrieve an array of tasks)
                ```
                // Creates a new ParseQuery object to help us fetch MyCustomClass objects
                  ParseQuery<ParseObject> query = ParseQuery.getQuery("MyCustomClass");

                  // Fetches data synchronously
                  try {
                    List<ParseObject> results = query.find();
                    for (ParseObject result : results) {
                        System.out.println("Object found " + result.getObjectId());
                    }
                  } catch (ParseException e) {
                    e.printStackTrace();
                  }

                  // Or use the the non-blocking method findInBackground method with a FindCallback
                 query.findInBackground((results, e) -> {
                    // do some stuff with results
                    if (e == null) {
                      for (ParseObject result : results) {
                        Log.d("Object found",result.getObjectId());
                      }
                    } else {
                        Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                  });
                ```
            * (Write/POST) - To create a new object of the TaskCompleted class, when the user completes a task
                ```
                ParseObject entity = new ParseObject("TaskCompleted");

              entity.put("task", new ParseObject("Task"));
              entity.put("user", ParseUser.getCurrentUser());

              // Saves the new object.
              // Notice that the SaveCallback is totally optional!
              entity.saveInBackground(e -> {
                if (e==null){
                  //Save was done
                }else{
                  //Something went wrong
                  Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
              });
                ```
            * (Read/GET) - To retrieve the current user
               ```
              ParseUser currentUser = ParseUser.getCurrentUser();
              if (currentUser != null) {
                // do stuff with the user
              } else {
                // show the signup or login screen
              }
                ```
        * AddTask Fragment
            * (Write/POST) - To create a new Task
                ```
                ParseObject entity = new ParseObject("Task");

              entity.put("name", "A string");
              entity.put("description", "A string");
              entity.put("points", 1);
              entity.put("category", "A string");
              entity.put("isApproved", true);

              // Saves the new object.
              // Notice that the SaveCallback is totally optional!
              entity.saveInBackground(e -> {
                if (e==null){
                  //Save was done
                }else{
                  //Something went wrong
                  Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
              });
                ```
    * Profile Fragment
         * (Read/GET) - To retrieve the current user
            ```
              ParseUser currentUser = ParseUser.getCurrentUser();
              if (currentUser != null) {
                // do stuff with the user
              } else {
                // show the signup or login screen
              }
            ```
         * (Read/GET) - To get a list of any object (it will be needed to retrieve all the completed tasks for this user)
            ```
            // Creates a new ParseQuery object to help us fetch MyCustomClass objects
              ParseQuery<ParseObject> query = ParseQuery.getQuery("MyCustomClass");

              // Fetches data synchronously
              try {
                List<ParseObject> results = query.find();
                for (ParseObject result : results) {
                    System.out.println("Object found " + result.getObjectId());
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }

              // Or use the the non-blocking method findInBackground method with a FindCallback
             query.findInBackground((results, e) -> {
                // do some stuff with results
                if (e == null) {
                  for (ParseObject result : results) {
                    Log.d("Object found",result.getObjectId());
                  }
                } else {
                    Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
              });
            ```
    To find more code: https://parse-dashboard.back4app.com/

 ## 📃 Libraries Used
   * Android Codepath AsyncHttpClient: https://github.com/codepath/CPAsyncHttpClient
   * Secrets Gradle Plugin for Android: https://github.com/google/secrets-gradle-plugin
   * Image Picker for Android: https://github.com/Dhaval2404/ImagePicker
   * CircleImageView: https://github.com/hdodenhof/CircleImageView
   * ChartProgressBar-Android: https://github.com/hadiidbouk/ChartProgressBar-Android
   * Lottie for Android: https://github.com/airbnb/lottie-android
 ## 📃 More Resources Used
   * Parse XML data: https://developer.android.com/training/basics/network-ops/xml#parse

