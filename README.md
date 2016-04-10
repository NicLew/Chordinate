Chordinate
==========

### Description

Chordinate is an Android application that aids music composers by accepting a melody played on an in-app keyboard and algorithmically composing chords to accompany that melody. Our app may also be used to record and save musical ideas via the microphone. The goal of this project is to develop a useful learning tool for amateur composers and a convenient, portable tool for experienced composers. Chordinate is implemented in Java using the Android SDK and is currently being tested on a Samsung Galaxy S5 smartphone. Chordinate’s algorithm takes your melody and finds appropriate chords and progressions based on a key, a major or minor scale, and the basic rules of “Bach chorale” music composition. We have successfully created an application that transforms your melody into a cohesive composition. Our hope is that Chordinate will inspire new musical ideas and facilitate those ideas by recording and building upon them whenever and wherever inspiration strikes.

### How to Build and Run the Code

1. Download and install Android Studio (http://developer.android.com/sdk/index.html)

2. Fork the Chordinate repository to your personal GitHub account.

3. Clone your personal copy of Chordinate to your computer, replacing USERNAME with your GitHub user name:

    `git clone git@github.com:USERNAME/Chordinate.git`

4. Import the project into Android Studio ([tutorial](https://www.jetbrains.com/help/idea/2016.1/importing-project-from-gradle-model.html?origin=old_help)).

5. Connect an Android device to your computer through USB ([more info](http://developer.android.com/tools/device.html)). NOTE: The emulator may crash with this project since it does not support sound input.

6. Build and run the project from Android Studio ([more info](http://developer.android.com/tools/building/building-studio.html)).

### How to Contribute

We would be happy to have you contribute to our project!

##### Bug Reporting

If you wish to file a bug report, please open a GitHub Issue marked as **bug**. Here are the things you should include:
* A clear description of the bug you found
* Screenshots where applicable and possible
* Clear instructions on how to replicate the bug, if known
* Where in the code you think the bug may be originating
* If the bug causes the app to crash, show logs

If you wish to fix a bug, choose from the list of GitHub Issues marked as bugs, assign yourself, fix it, and follow instructions for making a pull request.

##### Adding a Feature

If you wish to add a new feature to the app, these are the steps you should take:

1. Choose from the list of additional features we have listed or imagine your own!

2. If you choose from our list of features, look up the **enhancement** tag in GitHub Issues. Assign yourself that issue to show that it is claimed. If you still have questions, contact us at chordinate.help@gmail.com.

4. If your feature adds any new media (such as new images or sounds), make sure they are under compatible licenses.

List of additional features we would like:
* Interpreting pitch through the microphone to allow for easier input of the melody
* Implementing rhythm
* Adding more scale and chord types to the composition algorithm
* MIDI file output
* PDF sheet music output
* Support for all Android devices

##### Making a Pull Request

Once you have made changes (by either fixing a bug or adding a feature), create a pull request. For your pull request to be merged, follow these guidelines:
* A pull request from outside contributor must have at least two positive reviews.
* A pull request must be merged by one of the project leads.
* Please follow good Java coding standards (https://google.github.io/styleguide/javaguide.html). Your pull request will not be merged without proper documentation. 
* Please test your changes throughly.

If you follow all of these guidelines, you will be a successful contributor to our project!

### Contact Information

If you have any questions, email us at chordinate.help@gmail.com or comment on the relevant issue or pull request.

### Original Authors

[Nicole Lewey](https://www.linkedin.com/in/nicolelewey) (NicLew) and [Jacob Lundgren](https://www.linkedin.com/in/jacob-lundgren-22771a86) (TwoSeconds) are computer science majors from Pacific University and developed this project throughout their senior year.

### System Information

* Developed in Windows 7
* Developed in Android Studio 1.4 Preview 3
* Tested on the Samsung Galaxy S5

### Documentation

All documentation is within the code.

### License Information

This software is under [The MIT License (MIT)](https://opensource.org/licenses/MIT).

All images and sounds used in this software are licensed under the [Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)](http://creativecommons.org/licenses/by-nc/4.0/).

![alt text](https://github.com/NicLew/Chordinate/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)
