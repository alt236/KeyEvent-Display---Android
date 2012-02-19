KeyEvent Display - Android
========================

Once, I was playing with a number of Chinese tablets and trying different ROMs on them as they are technically the same hardware [HSG X5A variants](http://www.huashiguang.com/MID.html)).

I've had some problems getting the hard buttons to work though, so I wrote this application to detect key events and print them out.

* KeyEvents:  The KeyEvents as Android understands them (KeyUp, KeyDown, KeyLongPress, KeyMultiple)

* LogCat: Any relevant messages in logcat. Its filtered based on keywords declared in `arrays.xml`

* Kernel: Any relevant messages in the kernel log. Its filtered based on keywords declared in `arrays.xml`. Kernel log parsing needs root.

The three checkboxes at the top control what information will be displayed.

* This is a personal debug tool, but I hope it will be of some use to someone else.

* No Ads.

Notes
-----------
* While the application is running, the only "hard" keys which should work is "Home" and power. All others will produce their keycodes.
    
* The SU request is to read the kernel log, so I can check if any keyevents are thrown by the kernel.
    
* The location of keylayout files in Android is `/system/usr/keylayout`.
    
* Kernel log parsing needs root
  
* Both logcat and kernel log monitoring will only display lines containing words from two arrays in `arrays.xml`
    
* Currently the filters are:

1. Logcat:
    1. HwGPIOE->GPDA
    1. keycode
    1. keycharacter
1. Kernel:
    1. HwGPIOE->GPDA
    1.  keycode
    1. keycharacter

Permission Explanation
--------------
* `READ_LOGS`:  Used to access the Logcat log.
* `WRITE_EXTERNAL_STORAGE`: Used to write the exported data to the SD card.

Changelog
--------------
* v0.0.1: First public release.
* v0.0.2: Improved stability, added Exit button.
* v0.0.3: Code updates. 

Links
-------
* Market link:  [https://market.android.com/details?id=aws.apps.keyeventdisplay](https://market.android.com/details?id=aws.apps.keyeventdisplay)
* Webpage: [http://aschillings.co.uk/html/keyevent_display.html](http://aschillings.co.uk/html/keyevent_display.html)
* Github: [https://github.com/alt236/KeyEvent-Display---Android](https://github.com/alt236/KeyEvent-Display---Android)

Credits
-------

Author: Alexandros Schillings.

All logos are the property of their respective owners

The code in this project is licensed under the [Apache Software License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).

Copyright (c) 2011 Alexandros Schillings.
