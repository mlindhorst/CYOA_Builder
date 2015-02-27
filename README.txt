Melynda Lindhorst
u0855554

CS 4962 Final Project: CHOOSE YOUR OWN ADVENTURE application



Overview: 	An Android application that allows the user to load in a "script" to 					play their own choose your own adventure story.

Included in my submitted zip file:
		- My CYOA app
		- A sample game
			+ Images
			+ 3 script files (.xml)
		- My final design document

Instructions:
		1. Included in the top-most file level is a zip folder called "sample
		   game". Please unzip and load this folder into your phone's or 
		   emulator's storage.
		2. Open the application. The "continue" button will be unclickable
		   until an autosave occurs. For now, click the "new" button.
		3. You are now taken to the script loading screen. Please hit the
		   "search" button which will launch a file chooser. Navigate to the 
		   .xml script file. Click "next".
		4. Now you're taken to the create your character screen. Please enter
		   in any information. They all have defaulted values, so you can
		   feasibly just click "start" to continue on.
		5. You've arrived at the game! Read the story, if you are prompted
		   with a question, a spinner box will appear at the top to select
		   your answer. 
		6. When you reach the end, you will be taken back to the main screen.
		7. At this point, you can create a new game again or hit the
		   "continue" button to be taken to your last dialogue screen.

Notable Features:
		- The game autosaves. :D Exit in any way possible, and the
		  "continue" button will take you right back to where you previously
		  had stopped.
		- Anyone can create their own script file. I've included directions
		  below. 
		- A fun icon.
		- I've included three external libraries:
			+ GSON (saving and loading)
			+ Picasso (loading in images from file)
			+ aFileChooser (file dialog capabilities)
		- This took a lot more time than I had originally planned.

Creating a script:
		- In general, just follow the format of the sample game scripts.
		- All image declarations must come first. Their paths must be on
		  the phone.
		- All dialogue must come second, after image declarations. 
		- All dialogue must be encapsulated by page tags. 
		- Choices are saved in a Map, so all id's must be different.
		- There are two choice actions to choose from: 
			+ SAVE_VALUE: save the action_value attribute for later reference.
			+ JUMP_TO: jumps to the page id listed in action_value.
		- Blank scene or character attributes leave the current scene unchanged.
		- There are two speaker attribute reserved words:
			+ NARRATOR: dialogue speaker box will be blank.
			+ PC: player's name.
		- The retrieve_value attribute will retrieve a value from the choice table.
		- The jump_to attribute will jump to a page id.

Notes:
		A lot of my time went to the ScriptReaderActivity. I felt like I was basically
		making a text editor, having to parse so much XML. A lot of weird bugs would
		surface through the different phases of reading the file. Logic became very
		confusing at times.

		I also began implementing a load/save screen before I just decided to create
		an auto-save feature (which triggers in onPause()). I had the array adapter
		written and everything.

		In my design document, I discussed having an options menu for sound and things of
		that nature. But I decided not to include it because it would basically do 
		nothing that the phone can't already do itself autonomously from my application.