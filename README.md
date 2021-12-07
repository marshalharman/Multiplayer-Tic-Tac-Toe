# Tic Tac Toe

This is a starter code for the Tic Tac Toe multiplayer game app assignment.

It uses Android Navigation Component, with a single activity and three fragments:

- The DashboardFragment is the home screen. If a user is not logged in, it should navigate to the
LoginFragment. (See the TODO comment in code.)

- The floating button in the dashboard creates a dialog that asks which type of game to create and
passes that information to the GameFragment (using SafeArgs).

- The GameFragment UI has a 3x3 grid of buttons. They are initialized in the starter code.
Appropriate listeners and game play logic needs to be provided.

- Pressing the back button in the GameFragment opens a dialog that confirms if the user wants to
forfeit the game. (See the FIXME comment in code.)

- A "log out" action bar menu is shown on both the dashboard and the game fragments. Clicking it
should log the user out and show the LoginFragment. This click is handled in the MainActivity.



## a) DETAILS
Project Name: Journal App

Name: Harman Marshal Singh

BITS ID: 2018B4A70843G

Email: f20180843@goa.bits-pilani.ac.in


## b) APP DESCRIPTION
### Tic Tac Toe
A single/multiplayer Tic Tac Toe game. A player can chose from a list of open games to be played another player or can create their own 2 player game. 
### Bugs:
  * Register/SignIn button takes some time to authenticate, hence user might press the button multiples times as no information of loading/authenticating is shown.  

## c) TASKS DESCRIPTION
### Task1: Implementing Sign In:
  * Firebase authentication is used.
  * The user has to enter an email of valid format( xxxx @ xxxx.xxxx ) and password length should be greater than 6.
  * One pressing the register or signIn button, createAccount() function is called. If the email is already registered then the signIn() function is called.
  * It takes one or two seconds to authenticate, depending upon the internet connectivity of the device. 
  * If the authentication is successful, then the wins,losses and email are stored in a sharedPreference file for sharing data amongst fragment and maintaining a cache.
  
### Task2: Single Player Game:
  * A list of available cells is maintained. Every time a cell's value is updated, the boolean value for the availabilty is set to false.
  * The listeners 
  
