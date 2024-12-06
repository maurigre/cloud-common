# Contributing to Cloud Common 

First of all, thank you for taking the time to contribute! :tada:

Your time and help are appreciated.

Here are some guidelines to help you get started.

## Code of Conduct

Be kind and respectful to the members.\
Take time to educate others who are seeking help.

## Issue vs Discussion

Please [raise an issue](https://github.com/maurigre/cloud-common/issues) if:

* you find a bug;
* you have a feature request and can clearly describe your request.

Please [open a discussion](https://github.com/maurigre/cloud-common/discussions) if:

* you have a question;
* you're not sure how to achieve something with plugin;
* you have an idea but don't quite know how you would like it to work;
* you have achieved something cool with plugin and want to show it off;
* anything else!

## Filing a bug or feature

1. Before filing an issue, please check the existing issues (both open and closed) to see
   if a similar one has already been reported.
   If there is one already opened, feel free to comment on it.
2. If you believe you've found a bug, please provide detailed steps to reproduce.
3. If you'd like to see a feature or an enhancement please open an issue with a clear title
   and description of what the feature is and why it would be beneficial to the project and its users.

## Submitting changes

1. Ensure the new code works properly:

    * [build](README.md#build) plugin from updated sources and run tests (if any):

    ```
    mvn clean verify
    ```

    * [install](README.md#install) updated plugin;
    * run JMeter and check the sampler elements available and work as expected.

2. It might be useful to raise an issue or open a discussion before to create some code,
   it's help you to understand if the changes make sense for the plugin.

### Quick steps to contribute

1. Fork the project.
2. Clone forked project: `git clone https://github.com/_YOUR_USERNAME_/cloud-common && cd cloud-common`.
3. Create your feature branch: `git checkout -b feature/my-new-feature`.
4. Add changes and run: `mvn clean verify`.
5. Add them to staging: `git add .`
6. Commit your changes: `git commit -m 'Add some feature'`.
7. Push to the branch: `git push origin feature/my-new-feature`.
8. Create new pull request.
