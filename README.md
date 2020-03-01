# Task Master Mobile App
---
# Lab 26 - 11 FEB 2020

## Description
TaskMaster application is an Andriod mobile product that allows users to create and add tasks to be completed.

## Change log
Created Activities:

    - MainActivity:  User lands on this activity.  Activity contains buttons to navigate to the add and all activities.  
    
    - AddTaskActivity:  User is able to add a task.  
    
    - AllTasksActivity:  User is able to view their open tasks

## Screenshots
![Home Page of TaskMaster App](https://github.com/micahThor/taskmaster/blob/master/screenshots/Home%20page%20lab%2026.png)

---

# Lab 27 - 12 FEB 2020

## Description
TaskMaster application is an Andriod mobile product that allows users to create and add tasks to be completed.

## Change log
Created Activities:

    - SettingsActivity:  User has ability to save a user name to shared preferences
    
    - TaskDetailsActivity: User can navigate to a task from home page and view details about task.

![Home Page of TaskMaster App](https://github.com/micahThor/taskmaster/blob/master/screenshots/Home%20page%20lab%2027.png)


---

# Lab 28 - 13 FEB 2020

## Description
Refactor your homepage to use a RecyclerView for displaying Task data. This should have hardcoded Task data for now.

Some steps you will likely want to take to accomplish this:

Create a ViewAdapter class that displays data from a list of Tasks.
In your MainActivity, create at least three hardcoded Task instances and use those to populate your RecyclerView/ViewAdapter.
Ensure that you can tap on any one of the Tasks in the RecyclerView, and it will appropriately launch the detail page with the correct Task title displayed.

## Change log
    - Created Task class.
    - Created fragment for Task class.
    - Created adapters to handle fragments

![Home Page of TaskMaster App](https://github.com/micahThor/taskmaster/blob/master/screenshots/Home%20page%20lab%2028.png)


---

# Lab 32 - 19 FEB 2020

## Description
-Task Model and Room 
        Following the directions provided in the Android documentation, set up Room in your application, and modify your Task class to be an Entity.  

- Add Task Form 
        Modify your Add Task form to save the data entered in as a Task in your local database.  

- Homepage
        Refactor your homepage’s RecyclerView to display all Task entities in your database.  

- Detail Page
        Ensure that the description and status of a tapped task are also displayed on the detail page, in addition to the title. (Note that you can accomplish this by passing along the entire Task entity, or by passing along only its ID in the intent.)

## Change log
    - Created Database to store Task objects.

![Home Page of TaskMaster App](https://github.com/micahThor/taskmaster/blob/master/screenshots/Home%20page%20lab%2032.png)


---

# Lab 33 - 20 FEB 2020

## Description
- Today, your app will add a new activity for all tasks with a Recycler View showing all tasks. These tasks must be clickable. When clicked on, trigger a Toast that displays details about the task.

## Change log
    - Added functionality to the all tasks activity.  Now all the saved tasks in the database appear on the screen.  Every activity is tappable.  When tapped, a toast appears with details about the task.

![All Tasks Page of TaskMaster App](https://github.com/micahThor/taskmaster/blob/master/screenshots/Home%20page%20lab%2033.png)


---
# Lab 26 - 11 FEB 2020

## Description
Integrate an AWS Amplify and create a Dynamo Database to replace the local storage solution.

## Change log
- Integrated the Amplify dependency.  This allows me to sync my AWS account with my Android application

## Screenshots
![](https://github.com/micahThor/taskmaster/blob/master/screenshots/Home%20page%20lab%2034.png)
![](https://github.com/micahThor/taskmaster/blob/master/screenshots/proof%20lab%2034.png)

---

# Lab 36 - 25 FEB 2020

## Description
Integrate Cognito dependency to allow user logins

## Change log
Integrated Cognito dependency.  Users can log in/out.  Also usernames

![Home Page of TaskMaster App](https://github.com/micahThor/taskmaster/blob/master/screenshots/lab36%20sign%20in.png)
![Home Page of TaskMaster App](https://github.com/micahThor/taskmaster/blob/master/screenshots/lab36%20home%20with%20login.png)

---

# Lab 37 - 38 - 27 FEB 2020

## Description
Integrate S3 dependency to allow users to save photos of their Tasks to AWS S3 storage

## Change log
Integrated Amplify Storage to allow users to save to AWS S3

Screenshots to come
![](https://github.com/micahThor/taskmaster/blob/master/screenshots/s3%201.png)
![](https://github.com/micahThor/taskmaster/blob/master/screenshots/s3%202.png)
![](https://github.com/micahThor/taskmaster/blob/master/screenshots/s3%203.png)
![](https://github.com/micahThor/taskmaster/blob/master/screenshots/s3%204.png)
