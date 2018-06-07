# Baking App
Project 3 for Udacity Android Developer Nanodegree

## Overview
App allows a user to select a recipe and see video-guided steps for how to complete it.
Data is fetched from an unpolished JSON file that might be missing visual media data
or have it misplaced.

- App uses Retrofit with Gson for networking and data serialization. 
- Utilizes Broadcast receivers for determining internet connectivity and impementing 
  a widget and a Service for populating the widget with data.
- Fragments are used in order to create a responsive design that supports both handset
  and tablet master-detail flow.
- Exoplayer handles the video playback
- Picasso is used for image loading
- RecyclerView used for displaying lists
- User can pin recipe to widget. This acton saves recipe in SharedPreferences and
  notifies the AppWidgetManager of data set changes, which triggers widget update
 
### Projects requirements:
- Plan and build app from scratch
- Use MediaPlayer/Exoplayer to display videos
- Handle error cases in Android
- Add a widget to the app experience
- Leverage a third-party library in your app
- Use Fragments to create a responsive design that works on phones and tablets

