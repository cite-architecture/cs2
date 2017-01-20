# `customcts`

In this directory, you can run or build a servlet with your own customized Canonical Text Service.

Add any content you like to the `src/main/webapp` directory, and when you use any of the gretty plugin's tasks, the contents of the `sparqlcts` servlet will be folded in with your custom content.  Your custom content has priority, so if you want to redefine something in `sparqlcts`'s standard installation, you can do so.

gretty tasks include:

- `gradle appRun`: runs your servlet
- `gradle war`: builds a `.war` file you can add to your own servlet container
