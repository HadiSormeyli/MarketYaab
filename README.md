# MarketYaab - Dropshipping App

Market Yab is a comprehensive dropshipping app designed to streamline the shopping experience for both customers and sellers.

Features:
- Customer Panel:
  - View product categories easily.
  - Save favorite locations to see nearby product listings.
  - Check the distance to selected stores and navigate to them.
  - Reserve products and track the remaining delivery time.
  - View stores on a map and explore store details.
  - Update profile information.

- Seller Panel:
  - Add and edit product listings.
  - Manage product reservations.
  - Mark the store as a trusted vendor.
  - Scan customer order codes and receive payments.
  - Update seller profile information.


<br/>
📧 Email: mohammadhadisormeyli@gmail.com<br/>
Feel free to reach out to me via email or connect with me. I'm always open to collaboration and networking opportunities!

# MVI pattern
This pattern was specified by André Medeiros (Staltz) for a JavaScript framework he has written called cycle.js. From a theoretical (and mathematical) point of view we could describe Model-View-Intent as follows:
<p align="center">
<img src="https://github.com/Kotlin-Android-Open-Source/MVI-Coroutines-Flow/blob/master/MVI_diagram.png" />
</p>

* intent(): This function takes the input from the user (i.e. UI events, like click events) and translate it to “something” that will be passed as parameter to model() function. This could be a simple string to set a value of the model to or more complex data structure like an Object. We could say we have the intention to change the model with an intent.
* model(): The model() function takes the output from intent() as input to manipulate the Model. The output of this function is a new Model (state changed). So it should not update an already existing Model. We want immutability! We don’t change an already existing Model object instance. We create a new Model according to the changes described by the intent. Please note, that the model() function is the only piece of your code that is allowed to create a new Model object. Then this new immutable Model is the output of this function. Basically, the model() function calls our apps business logic (could be an Interactor, Usecase, Repository … whatever pattern / terminology you use in your app) and delivers a new Model object as result.
* view(): This method takes the model returned from model() function and gives it as input to the view() function. Then the View simply displays this Model somehow. view() is basically the same as view.render(model).

# Coroutine + Flow
* Play MVI with Kotlin Coroutines Flow.
* Clean Architecture, Functional programming.
* Using Koin for DI.

# Authentication
<br/><br/>
<p float="left">
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_2024-09-06_15-29-06.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_1_2024-09-06_15-37-45.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_2_2024-09-06_15-37-45.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_2_2024-09-06_15-51-09.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_3_2024-09-06_15-58-41.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_1_2024-09-06_15-58-41.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_3_2024-09-06_15-51-09.jpg" width="150" />
</p>
<br/><br/>

# Customer
<br/><br/>
<p float="left">
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_1_2024-09-06_18-40-53.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_2_2024-09-06_18-40-53.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_6_2024-09-06_18-40-53.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_2024-09-06_18-42-18.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_4_2024-09-06_18-40-53.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_5_2024-09-06_18-40-53.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_3_2024-09-06_18-40-53.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_3_2024-09-06_19-12-08.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_2_2024-09-06_19-12-08.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_1_2024-09-06_19-12-08.jpg" width="150" />
</p>
<br/><br/>


# Store
<br/><br/>
 <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_5_2024-09-06_19-21-47.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_2_2024-09-06_19-21-47.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_3_2024-09-06_19-21-47.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_2024-09-06_18-42-18.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_1_2024-09-06_19-21-47.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_4_2024-09-06_19-21-47.jpg" width="150" />
  <img src="https://github.com/HadiSormeyli/MarketYaab/blob/main/images/photo_7_2024-09-06_19-21-47.jpg" width="150" />
<br/><br/>
