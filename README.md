# PortfoliosChart

"This is a simple android application that can store, read in and display data in a user-friendly manner."

#### Library support show Chart (Line Chart)
Thanks to https://github.com/PhilJay/MPAndroidChart.

#### Screenshots:
Can take a look here to have a overall stuff in project: https://github.com/lannablue89/PortfoliosChart/tree/master/screenshots

## Requirements and Delivery

### Things was done:

- App read the provided JSON data at startup.
- App can render portfolios on the same chart (line chart), with time as the x-axis and amount as y-axis. 
- The Chart is scaled to fit data at begin with different colours for each portfolio. It's also scalable and zoomable by user.
- The Chart show report daily at begin, after that user can press device menu to show options that can display data report in daily/monthly/quarterly format. 
- There can also report a total portfolio amount that is the sum of all portfolios for each day.
- Unit Test: there is some test for filterring data by day/month/quarter, missing test case for function report total portfolio amount that is the sum of all portfolios for each day. There almost is unit test, not UI test. (Refer to class PcPortfolioTest)

### Things need to improve:
 
 - UI: now it look so basic, not nice looking and user-friendly design from I've focused on main function first, then think can improve later. (Note that the ui for menu is really bad now, can improve later)
 - Unit Test: missing much test case now.
 - Move data into Firebase: later with more research first :D

### Other Notes:

- I assump that amount if 0 or null is unavailable, is ignored when show on screen. 


## Config Android Studio:
There is no any special settings for Android Studio then you should be able to run this project with Android Studio after clone the source. 
- My Android Studio version: 2.3.1

## Creator:
Lan Tran
