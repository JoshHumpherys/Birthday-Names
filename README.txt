BIRTHDAY NAMES
--------------

Version 2.0 --- 08/12/2015


INTRODUCTION
------------

This application was created for Invasion Laser Tag to help our employees
create images that say "Happy birthday" along with the birthday child's name. A
window pops up with an option for color, a text box for the child's name, and a
preview of what the final image will look like. By clicking "Create," an image
and accompanying html file are created and automatically launched with Google
Chrome. Fireworks are also displayed in the background, to add to the overall
effect. The html files are then casted to remote monitors in the lobby via
Google Chromecast.


CONFIGURATION
-------------

This application was designed specifically for Invasion Laser Tag and is likely
of no use elsewhere. Requirements for setting up the application:

* Download `BirthdayNames.jar` and preferably place it on the desktop. The file
may be renamed to `Birthday Names.jar` or anything else, as long as the
filetype remains .jar.

* Download the `video` directory and place in a directory named `C:\chrome\`.
Rename the desired background `.mp4` file to `fireworks.mp4`, as the
application looks for the background at `C:\chrome\video\fireworks.mp4`. NOTE:
If too many instances of chrome are open running the video, they may lag. If
this happens, try using a lower resolution video file for the background, or
run two from each of the two computers.

* Create a shortcut to `chrome.exe` (`chrome.lnk`) and place it in
`C:\chrome\application\`. The application looks for Google Chrome at
`C:\chrome\application\chrome.lnk`.

* Make sure the computer has JRE 7 or above installed.

* Install the Google Chromecast extension in each of the four user preference
directories at `C:\chrome\tag1`, `C:\chrome\tag2`, `C:\chrome\tag3`, and
`C:\chrome\tag4`. This can be done most easily by running the application and
clicking "Create" using any of the colors. Install the Google Chromecast
extension and then copy the corresponding user preference directory (i.e.
`C:\chrome\tag1` for orange) to the other 3 directories.

* Resize the Google Chrome window to the preferred size, preferably so that the
bottom of the window is right at the bottom of the image and video (not leaving
extra space at the bottom or blocking part of the video or image). Resizing one
window and then copying the corresponding user preference directory to the
other 3 directories should save the window size preference for all. NOTE: Once
the window is the preferred size, don't change it again, as the new window size
preference will automatically be saved.


TROUBLESHOOTING
---------------

The notes in the "Configuration" section should cover all of the setup
required. However, if the application does not work as intended or crashes
unexpectedly but all of the configuration requirements have been met, open up a
terminal and run `java -jar BirthdayNames.jar` to run the application. All java
errors will be printed to the terminal.


CONTACT
-------

This application was coded entirely by Joshua Humpherys in July and August of
2015. To report bugs, suggest new features, or ask questions on setup or use,
feel free to contact him:

* email: [joshua.humpherys@gmail.com](mailto:joshua.humpherys@gmail.com).

* phone: (442)888-9000


UPDATES
-------

Updates from Version 1.0 to Version 2.0 include the following:

* Create accompanying `.html` file for `.png` for increased functionality (such
as fireworks).

* Add fireworks to the background.

* Automatically launch with Google Chrome.

* Write files to `C:\chrome\` rather than to the same directory as the
application.

* Bug fixes in UI.

* Remove no longer necessary options such as day and time slot.

To check if this is the latest version of the application, visit the
application's GitHub repository.
https://github.com/JoshHumpherys/Birthday-Names