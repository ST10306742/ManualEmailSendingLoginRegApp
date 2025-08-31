# ManualEmailSendingLoginRegApp
This is a user login and register app that manual sends an otp via email to a user.


Videos used:
1st half (design andf logic):
https://www.youtube.com/watch?v=X-oGo5dch2E&t=1109s : Login Signup Using Email With OTP Part-1 (Designing Layouts) || Android Kotlin || Firebase

https://www.youtube.com/watch?v=Jdn1UBR54m4 : Login Signup Using Email With OTP Part-2 (Backend) || Android Kotlin || Firebase


2nd half:
https://www.youtube.com/watch?v=roruU4hVwXA : How to Send an Email from Android Application without opening Gmail App?


What happened?

The first 2 videos gave me the design and logic of whats currently in the app. This motivated the use of One-Time-Pin (OTP). However, when tested it crashed when it came to the email being sent. After asking ChatGPT, the issue was due to the outdated dependency being used (SendMail). It was calling an old version of the gmail mail servers, which is not supported anymore. SendMail library is trying to use TLSv1, Gmail requires TLSv1.2 or TLSv1.3. After consulting with GPT and searching the web for more resources, i have accross the 3rd video, which uses a sun mail dependency that basically uses JavaMail to send the email (hopefully its explained right).

I did have to make my own adjustments throughout this project so the guide i made or the videos included in this repo may not be as helpful. I did try to add as many comments as I could to explain the process.

The things that are currenly under development:

--> The forgot password has not been completed yet...still trying to figure out a logical way of implementing this feature.

--> Account Control : users who log can see everyone's information as it stands. Sessions will be used to aid this.

