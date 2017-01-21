## `sparlqcc`


### Configuration

Configure your CITE Collections service by setting values in the `ccconf.gradle` file (for integration in the `cs2` servlet), or the `soloconf.gradle` file (for stand-alone use: see the following section).

### Building for stand-alone use

To run or build `sparqlcc` as a stand-alone CITE Collection service (rather than integrating it into the full `cs2` servlet), set the `solo` property when running gretty tasks.  You can set this on the command line with the `-P` option, e.g..,

    gretty -Psolo=t war

When building a standalone version, gradle uses the configuration settings in `soloconf.gradle`, rather than in `ccconf.gradle`.
