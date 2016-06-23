# POEditor CLI

Command Line Interface for the POEditor translation platform.

## Usage

Create a file `poeditor.properties` in the root directory of your project, with your API key and the POEditor project ID:

    apiKey=YOUR_API_KEY
    projectId=YOUR_PROJECT_ID

Then run:

    poeditor-cli [push | pull]

The push command traverses your directories looking for your translation files, and then uploads them together with the reference language translations. By default, `poeditor-cli` looks for `Bundle.properties` files. You can specify a different pattern by setting the property `inputFile` in your `poeditor.properties`.

The pull command donwloads the translations for all the languages, and by default creates a file `Bundle_LANG.properties` wherever a source file is found, one for each language.
