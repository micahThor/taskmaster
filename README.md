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

![Home Page of TaskMaster App](https://github.com/micahThor/taskmaster/blob/master/screenshots/Home%20page%20lab%2028.png)


