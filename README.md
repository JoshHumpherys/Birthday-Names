# Birthday Names
Version 2.0

Released 08/14/2015

## Introduction
This application was created for Invasion Laser Tag to help our employees create images that say "Happy birthday" along with the birthday child's name. A window pops up with an option for color, a text box for the child's name, and a preview of what the final image will look like. By clicking "Create," an image and accompanying html file are created and automatically launched with Google Chrome. Fireworks are also displayed in the background, to add to the overall effect. The html files are then casted to remote monitors in the lobby via Google Chromecast.

## Configuration
This application was designed specifically for Invasion Laser Tag and is likely of no use elsewhere. Therefore, these configuration instructions are written specifically for setup on Invasion Laser Tag's computers. Requirements for setting up the application:

* Download `BirthdayNames.jar` and save it anywhere on the computer. The file may be renamed to `Birthday Names.jar` or anything else, as long as the filetype remains .jar.

* Create a shortcut on the desktop and change the icon. To do this, download img/cake-512.ico, right click on the application shortcut, click `Properties`, click `Change icon...`, click browse, and select the `.ico` image that was just downloaded.

* Download the `video` directory and place in a directory named `C:\chrome\`. Rename the desired background `.mp4` file to `fireworks.mp4`, as the application looks for the background at `C:\chrome\video\fireworks.mp4`. NOTE: If too many instances of chrome are open running the video, they may lag. If this happens, try using a lower resolution video file for the background, or run two from each of the two computers. There is also a `.webm` file that is much shorter if preferred.

* Copy all chrome files including `chrome.exe` and place them in a directory called `C:\chrome\application\`. Create 3 copies of chrome.exe and name the 4 executables `chrome1.exe`, `chrome2.exe`, `chrome3.exe`, and `chrome4.exe`. Then create shortcuts to all 4 files in the same directory and name them accordingly with `chrome1.lnk`, `chrome2.lnk`, etc. The application looks for shortcuts to the chrome executables in `C:\chrome\application\`. NOTE: If you have administrator permission, there is no need to copy the chrome files to the new directory. Simply create copies of the chrome.exe in it's original directory and rename as explained.

* Make sure the computer has JRE 7 or above installed.

* Install the Google Cast extension in each of the four user preference directories at `C:\chrome\tag1`, `C:\chrome\tag2`, `C:\chrome\tag3`, and `C:\chrome\tag4`. This can be done most easily by running the application and clicking "Create" using any of the colors. Install the Google Chromecast extension and then copy the corresponding user preference directory (i.e. `C:\chrome\tag1` for orange) to the other 3 directories.

## Usage
In order to cast a name, open the application, select a color, type the name, and click create. Wait for Google Chrome to open with the image and video playing in the background. Cast the `.html` file by clicking on the Google Cast extension and selecting the tag to cast it to. There is no need to kill the old chrome processes before creating a new one, as the application does that automatically. When clicking create, the text field is cleared and the next color is selected. The text field retains focus as long as the window is in focus, so there is no need to ever click the text field to gain focus. Additionally there are shortcut keys for increased speed. Use the `up` and `down` keys to select a color. Use `shift+enter` to create.

## Troubleshooting
The notes in the "Configuration" section should cover all of the setup required. However, if the application does not work as intended or crashes unexpectedly but all of the configuration requirements have been met, open up a terminal and run `java -jar BirthdayNames.jar` to run the application. All java errors will be printed to the terminal. If there is an issue with casting, create a file called `DO_NOT_USE_CHROMECASE_SDK_SCRIPT` in `C:\chrome\params`. This is a workaround to disable the chromecast sdk script without having to recompile all the code and reexport the project.

## Contact
This application was coded entirely by Joshua Humpherys in July and August of 2015. To report bugs, suggest new features, or ask questions on setup or use, feel free to contact him:

* email: [joshua.humpherys@gmail.com](mailto:joshua.humpherys@gmail.com).

* phone: (442)888-9000

## Updates
Updates from Version 1.0 to Version 2.0 include the following:

* Create accompanying `.html` file for `.png` for increased functionality (such as fireworks).

* Add fireworks to the background.

* Automatically launch with Google Chrome.

* Write files to `C:\chrome\` rather than to the same directory as the application.

* Bug fixes in UI.

* Remove no longer necessary options such as day and time slot.

* Kill old chrome process before launching a new one.

* Add keyboard shortcuts.

To check if this is the latest version of the application, visit the application's [GitHub repository](https://github.com/JoshHumpherys/Birthday-Names).