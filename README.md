# The CITE servlet (second generation)

A servlet running all CITE services.  Note that the January, 2017, release uses `cite2` URNs for all objects in CITE Collections.

## Notes on building and running


### The `cs2` servlet

The `cs2` subproject builds a single servlet offering the full suite of CITE services.  Use gretty tasks like `gradle appRun` and `gradle war` to run or build this servlet.  If you want to use non-default values for any of the servlet settings, you can define values in a configuration file.  Duplicate the file `cs2/conf.gradle`, and edit or add any values you like in the `cs2Tokens` map.  Include a `custom` property giving the full path to your custom configuration file when you use any gretty task, e.g.,

    gradle -Pcustom=/Path/to/custom/config/file.gradle cs2:appRun


### Individual services

You may also build any of the separate services independently.  To use gretty tasks with default settings for `sparqlcts`, `sparqlcc` or `sparqlimg`, include a `solo` property with a non-null value, e.g.,

    gradle -Psolo=y sparqlcts:runApp

Alternatively, as with the `cs2` subproject, you can instead set the full path to a custom configuration file with the `custom` property.


### Adding custom content

In addition to customizing settings, you can run or build a servlet with your customized content in the `customcs2` subproject.  The `customcts2` subproject relies on a configuration file that has the same `cs2Tokens` map as `cs2`, but also includes a `sourceDirectory` property

Like this:

    gradle -Pcustom=/Path/to/conf/file.gradle installSource
