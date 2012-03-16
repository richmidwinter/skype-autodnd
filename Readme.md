Skype AutoDND
=============

This is a utility jar which automatically moves your Skype status to Do Not Disturb when you are in a call and moves it back to the previous status at the end of a call.

It seems pretty reliable to move to Do Not Disturb but less reliable when moving back. Still, some of the legwork is removed.

### Build the jar

    mvn assembly:single

This creates a single jar file including dependencies.

### Run the jar

With Skype running, execute:

    java -d32 -jar skype-autodnd-0.0.1-SNAPSHOT-jar-with-dependencies.jar
