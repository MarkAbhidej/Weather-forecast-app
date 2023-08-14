# Weather forecast app description

This app there are 2 screen

At home screen display temperature and humidity in your location

If your first install, You should allow permission to access your location in order to display correct data

You can search forecast by using city name of your interesting in search bar at home screen

You can see temperature and humidity of whole day by click bottom navigation on the screen

## Getting Started
This application is simple, you can clone it or download it and you can use it right away.
For help getting started, view our [Android develop guide](https://developer.android.com/docs) , which offer guidance for development

# This app implement by using Clean architecture:
1. **data**: Contain all the data accessing
2. **useCase**: Implement business logic of app
3. **viewModel**: Implement view logic of app
4. **presentation(ui)** View class along with corresponding viewModel

using dependency injection with Koin

manage tasks with asynchronous by using Coroutines

screen transitions use navigation graphs

# There are Unit test of:
1. repository
2. useCase
3. viewModel
You can see or test in path: src/main/test

If you need to develop new features, You can reuse repository in this project
and create the new useCase for implement business logic
and inject to viewModel for manage ui display