# # # Pixel Dungeon Of Teller
Greetings and this is another mod of SPD.I have to note that this project have a rather long way to what is called 'formal version' so it might be a bad choice for a playing fun during a long time.
# Highlights:
* Overrided damage system(not in total use yet)
* A large increase amount of weapons(still not enough and many needs to be replaced)
* Self-mod able magic book replaces wand(in work and have a long way to go)
* Stealth,AI change and assassin equipment to make use of that(not finished yet and need a lot of polishing)  
* NOT REALIZED SYSTEM,like overrided rings and artficates,rework upgrade,imbue and weightstone imerged into a highly combined and controlable one
* A lot of bugs,and more bugs(yeah that's the most important content)
# Notice:
Remember that this dungeon have a long way to go.
Not recommended for playing but I really need some testing and bug reports so...here it is.
Myself may lack talent in balancing and drawing(coding also,might be)and I just cannot refuse new and also useful update in SPD and other dungeons(like 2.5D and muliti-save slots)So this one would need a much longer time to really reach a impressive version,much longer than you can expected,seriously.
I only have the time for Chinese text,after the so-called-formal version is out I would be able to concentrate on other language support.I would be more than honored if get help on translation,but still have to mention it's not at high properties,not yet.
# Contact:
I'm in lack of almost everything,from pictures to data and design,even time to code.So if you want to take part in its creation or just report a bug(also long delay before update might be)can contact me by e-mail,in English or Chinese.

# Shattered Pixel Dungeon

A Roguelike RPG, with randomly generated levels, items, enemies, and traps!
Based on the source code of Pixel Dungeon, by Watabou.

Look below for compilation instructions.

Shattered Pixel Dungeon on Google Play:
https://play.google.com/store/apps/details?id=pixeldungeonofteller

On Amazon:
https://www.amazon.com/Shattered-Pixel-Dungeon/dp/B00OH2C21M

On F-Droid (Which compiles directly from this source code):
https://f-droid.org/repository/browse/?fdid=pixeldungeonofteller

Official website and blog:
http://www.shatteredpixel.com

Source code of original Pixel Dungeon (adapted to use newer build tools):
https://github.com/00-Evan/pixel-dungeon-gradle

# Compiling Shattered Pixel Dungeon

To compile Shattered Pixel Dungeon you will need:
- A computer which meets the [system requirements for Android Studio](https://developer.android.com/studio#Requirements)
- (optional) a GitHub account to fork this repository, if you wish to use version control
- (optional) an android phone to test your build of Shattered Pixel Dungeon

#### 1. Installing programs

Download and install the latest version of [Android Studio](https://developer.android.com/studio). This is the development environment which android apps use, it includes all the tools needed to get started with building android apps.

It is optional, but strongly recommended, to use version control to manage your copy of the Shattered Pixel Dungeon codebase. Version control is software which helps you manage changes to code. To use version control you will need to download and install [Git](https://git-scm.com/downloads). You are welcome to use a separate graphical git client or git CLI if you prefer, but this guide will use Android Studio's built-in git tools.

#### 2. Setting up your copy of the code

If you are using version control, fork this repository using the 'fork' button at the top-right of this web page, so that you have your own copy of the code on GitHub.

If you do not wish to use version control, press the 'clone or download' button and then 'Download ZIP'. Unzip the downloaded zip to any directory on your computer you like.

#### 3. Opening the code in Android Studio

Open Android Studio, you will be greeted with a splash page with a few options.

If you are using version control, you must first tell Android Studio where your installation of Git is located:
- Select 'Configure' then 'Settings'
- From the settings window, select 'Version Control' then 'Git'
- Point 'Path to Git executable:' to 'bin/git.exe', which will be located where you installed git.
- Hit the 'test' button to make sure git works, then press 'Okay' to return to the splash page.

After that, you will want to select 'check out project from version control' then 'git'. Log in to GitHub through the button (use username instead of tokens), and select your forked repository from the list of URLs. Import to whatever directory on your computer you like. Accept the default options android studio suggests when opening the project. If you would like more information about working with Git and commiting changes you make back to version control, [consult this guide](https://code.tutsplus.com/tutorials/working-with-git-in-android-studio--cms-30514) (skip to chapter 4).

If you are not using version control, select 'Import project (Gradle, Eclipse ADT, etc.)' and select the folder you unzipped the code into. Accept the default options android studio suggests when opening the project.

#### 4. Running the code

Once the code is open in Android Studio, running it will require either a physical android device or an android emulator. Using a physical device is recommended as the Android Emulator is less convenient to work with and has additional system requirements. Note that when you first open and run the code Android Studio may take some time, as it needs to set up the project and download various android build tools.

The Android studio website has [a guide which covers the specifics of running a project you have already set up.](https://developer.android.com/studio/run)

This guide includes a [section on physical android devices...](https://developer.android.com/studio/run/device.html)

... and [a section on emulated android devices.](https://developer.android.com/studio/run/emulator)

#### 5. Generating an installable APK

An APK (Android PacKage) is a file used to distribute Android applications. The Android studio website has [a guide which covers building your app into an APK.](https://developer.android.com/studio/run#reference) Note that the option you will likely want to use is 'Generate Signed Bundle / APK'.

Note that APKs must be signed with a signing key. If you are making a small personal modification to Shattered Pixel Dungeon then your signing key is not important, but if you intend to distribute your modification to other people and want them to be able to receive updates, then your signing key is critical. The Android studio website has [a guide on signing keys.](https://developer.android.com/studio/publish/app-signing.html#opt-out)

Additionally, note that by default Shattered Pixel Dungeon uses R8 on release builds. R8 is a code optimizer which decreases the size of the APK and improves performance, but also makes error reports more difficult to read. You can disable R8 by setting minifyEnabled to false in 'core/build.grade'. If you wish to keep R8 enabled, you can learn more about it [here.](https://developer.android.com/studio/build/shrink-code)

#### 6. Distributing Your APK

The Android Studio website includes [a guide for ways to distribute your app.](https://developer.android.com/studio/publish)

Note that by distributing your modification of Shattered Pixel Dungeon, you are bound by the terms of the GPLv3 license, which requires that you make any modifications you have made open-source. If you followed this guide and are using version control, that is already set up for you as your forked repository is publicly hosted on GitHub.

If you intend to make your version of the game available on Google Play **PLEASE CONTACT ME AT THE FOLLOWING EMAIL ADDRESS:** Evan@ShatteredPixel.com . There are various aspects of Google's Developer Policies that go beyond the scope of a simple 'how to compile' guide. If you do not take necessary precautions before attempting to publish on Google Play, your version of the game will almost certainly be taken down for impersonating Shattered Pixel Dungeon or Pixel Dungeon.
