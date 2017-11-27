#Use Cases

##Use Case: Time

###CHARACTERISTIC INFORMATION

**Goal in Context:** Give the user the option to display the time(s) on the mirror.

**Scope:** Display the current time and additional times for seperate timezones.

**Level:** Primary Task

**Preconditions:** None.  

**Success End Condition:** The time is displayed correctly and with the choice of timezone and time format (standard vs military). 

**Failed End Condition:** Time will not be displayed.

**Primary Actor:** The user must choose which time to display and where on the mirror to display it. A default location can be provided.

**Trigger:** The user can use a hand gesture, voice control, or the mobile app. 

###MAIN SUCCESS SCENARIO

Voice Recognition

1. User says "Display Time"

2. The time appears in the default position

Hand Gestures

1. User gives a gesture (to be determined)

2. The time appears in the default position

Mobile App

1. User opens the app and selects the time option

2. User chooses the timezone, format, and location of the time

3. The time appears in the specified position.


###RELATED INFORMATION (optional)

**Priority:** Critical

**Performance Target:** 10 Seconds

**Frequency:** The time should be displayed until the user asks it not to. 

###OPEN ISSUES (optional)

Is it possible to display more than one time?

What happens in sleep mode?

What happens in night mode?

###SCHEDULE

**Due Date:** None

##Use Case: Weather

###CHARACTERISTIC INFORMATION

**Goal in Context:** Give the user the option to display the weather on the mirror.

**Scope:** Display the weather as a temperature, description, and possibly and image.

**Level:** Primary Task

**Preconditions:** The app has access to a weather source.   

**Success End Condition:** The weather is displayed on the mirror accurately. 

**Failed End Condition:** The weather will be incorrect or won't appear. 

**Primary Actor:** The user must choose the weather option and select which city they want to display the weather for. This preference is saved for future use. 

**Trigger:** The user can use a hand gesture, voice control, or the mobile app. 

###MAIN SUCCESS SCENARIO

Voice Recognition

1. User says "Display Weather"

2. The weather appears in the default position

Hand Gestures

1. User gives a gesture (to be determined)

2. The weather appears in the default position

Mobile App

1. User opens the app and selects the weather option

2. User chooses the city that they want to display the weather for and where on the mirror to display it.

3. The weather appears in the specified position.


###RELATED INFORMATION (optional)

**Priority:** Critical

**Performance Target:** 10 Seconds

**Frequency:** The weather should be displayed until the user asks it not to. 

###OPEN ISSUES (optional)

Is it possible to display weather for more than one city? 

Can we incorporate road conditions?

What happens in sleep mode?

What happens in night mode?

###SCHEDULE

**Due Date:** None

##Use Case: Schedules

###CHARACTERISTIC INFORMATION

**Goal in Context:** Give the user the option to display their schedule on the mirror.

**Scope:** Display the users schedule. 

**Level:** Primary Task

**Preconditions:** The user has added items to their schedule on the mobile app.   

**Success End Condition:** The schedule is displayed such that the user can see what they have planned and when. 

**Failed End Condition:** Their schedule will not be displayed.

**Primary Actor:** The user must add items to their schedule via the mobile app and then choose to display it. 

**Trigger:** The user can use a hand gesture, voice control, or the mobile app to display the schedule.  

###MAIN SUCCESS SCENARIO

Voice Recognition

1. User says "Display Schedule"

2. Their schedule appears in the default position

Hand Gestures

1. User gives a gesture (to be determined)

2. Their schedule appears in the default position

Mobile App

1. The user adds items to their schedule

2. User opens the app and selects the schedule option 

3. The user decides where to display the schedule

4. Their schedule appears in the specified position


###RELATED INFORMATION (optional)

**Priority:** Critical

**Performance Target:** 10 Seconds

**Frequency:** The user's schedule should be displayed until they ask it not to. 

###OPEN ISSUES (optional)

What happens if their schedule is empty?

What happens in sleep mode?

What happens in night mode?

###SCHEDULE

**Due Date:** None

##Use Case: Reminders

###CHARACTERISTIC INFORMATION

**Goal in Context:** Give the user the option to display their reminders on the mirror.

**Scope:** Display the user's reminders.

**Level:** Primary Task

**Preconditions:** The user has added reminders via the mobile app.   

**Success End Condition:** The users reminders are displayed on the mirror.

**Failed End Condition:** The reminders will not be displayed.

**Primary Actor:** The user must choose to display their reminders and where on the mirror to display it. 

**Trigger:** The user can use a hand gesture, voice control, or the mobile app. 

###MAIN SUCCESS SCENARIO

Voice Recognition

1. User says "Display Reminders"

2. The reminders appear in the default position

Hand Gestures

1. User gives a gesture (to be determined)

2. The reminders appear in the default position

Mobile App

1. The user adds reminders via the mobile app.

2. User opens the app and selects the reminders

3. User chooses where to place the reminders on the mirror.

4. The reminders appear in the specified position.


###RELATED INFORMATION (optional)

**Priority:** Critical

**Performance Target:** 10 Seconds

**Frequency:** The reminders should be displayed until the user asks it not to. 

###OPEN ISSUES (optional)

What happens if the user doesn't have any reminders?

What happens in sleep mode?

What happens in night mode?

###SCHEDULE

**Due Date:** None

##Use Case: Lists

###CHARACTERISTIC INFORMATION

**Goal in Context:** Give the user the option to display their list on the mirror.

**Scope:** Display the users list. 

**Level:** Primary Task

**Preconditions:** The user has created a list to display.  

**Success End Condition:** The user's list is displayed in the correct location.

**Failed End Condition:** The list will not be displayed. 

**Primary Actor:** The user must choose to display the list and where to display it on the mirror. 

**Trigger:** The user can use a hand gesture, voice control, or the mobile app. 

###MAIN SUCCESS SCENARIO

Voice Recognition

1. User says "Display List"

2. The list appears in the default position

Hand Gestures

1. User gives a gesture (to be determined)

2. The list appears in the default position

Mobile App

1. User creates a list. 

2. User opens the app and selects the list option

3. User chooses the position of the list on the screen.

4. The list appears in the specified position.


###RELATED INFORMATION (optional)

**Priority:** Critical

**Performance Target:** 10 Seconds

**Frequency:** The list should be displayed until the user asks it not to. 

###OPEN ISSUES (optional)

Is it possible to display more than one list?

What happens in sleep mode?

What happens in night mode?

###SCHEDULE

**Due Date:** None


##Use Case: Pictures

###CHARACTERISTIC INFORMATION

**Goal in Context:** Give the user the option to display thier pictures on the mirror.

**Scope:** Display the users pictures.

**Level:** Secondary Task

**Preconditions:** The user has pictures to display.   

**Success End Condition:** The users pictures are displayed as a slideshow in the desired location.

**Failed End Condition:** The pictures will not be displayed. 

**Primary Actor:** The user must choose which pictures to display and where. 

**Trigger:** The user can use a hand gesture, voice control, or the mobile app. 

###MAIN SUCCESS SCENARIO

Voice Recognition

1. User says "Display Pictures"

2. The pictures appear in the default position

Hand Gestures

1. User gives a gesture (to be determined)

2. The pictures appear in the default position

Mobile App

1. User opens the app and selects the pictures option

2. The user chooses which pictures to include and where to put the slideshow on the mirror

3. The pictures appear in the specified position

###RELATED INFORMATION (optional)

**Priority:** Semi-Critical

**Performance Target:** 10 Seconds

**Frequency:** The pictures should be displayed until the user asks it not to. 

###OPEN ISSUES (optional)

Is it possible to display more than one picture slideshow?

What happens in sleep mode?

What happens in night mode?

###SCHEDULE

**Due Date:** None

##Use Case: Music and Videos

###CHARACTERISTIC INFORMATION

**Goal in Context:** Give the user the option to play music from the mirror and potentially display a video.

**Scope:** Display the video or a music icon that allows the user to play music. 

**Level:** Secondary Task

**Preconditions:** The user has music to listen to.  

**Success End Condition:** The music is played.  

**Failed End Condition:** The music is not played

**Primary Actor:** Which music to play and where on the mirror to display the video/icon.

**Trigger:** The user can use a hand gesture, voice control, or the mobile app. 

###MAIN SUCCESS SCENARIO

Voice Recognition

1. User says "Play Music"

2. The music video/icon appears in the default position.

3. The user can go to the next song by saying "next", pause by saying "pause", stop by saying "stop", or adjust the volume by saying "volume up" or "volume down"

Hand Gestures

1. User gives a gesture (to be determined)

2. The music video/icon appears in the default position.

Mobile App

1. User opens the app and selects the music option

2. User chooses which music to play.

3. The music video/icon is displayed in the specified location and the music is played.

4. The user can go to the next song, pause, play, stop, and adjust the volume using the app.


###RELATED INFORMATION (optional)

**Priority:** Non-Critical

**Performance Target:** 10 Seconds

**Frequency:** The music should be played until the user stops it.  

###OPEN ISSUES (optional)

Is video even possible?

What happens in sleep mode?

What happens in night mode?

How do we implement an auto-shutoff? 

###SCHEDULE

**Due Date:** None
