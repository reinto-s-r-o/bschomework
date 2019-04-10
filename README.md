# BSC - Homework for Android developers

### Tech/framework used
This app is build on MVVM architecture.
External libraries used in this project:

 * RXJava (observing data)
 * Retrofit + okhttp3 (api)
 * Koin (dependency injection)

### Features
This project is a small app for managing notes. User can retreive his/her notes from api, create new notes, edit existing notes and delete them.

### Installation
Clone this project from git, open it via Android Studio and run it. App should install without any changes to this project.

### API Reference
All endpoints used in this project are documented in apiary - https://note10.docs.apiary.io/#

### Tests
This project contains a few tests:

 * test if viewmodel corectly observes incoming messages (send_message_live_data_success)
 * test if we succesfully receive data from api (get_notes_from_api_equal_success)
 * test if app handles error incoming from api (get_notes_from_api_equal_fail)

### Credits
This solution was created by Reinto s.r.o.