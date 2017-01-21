<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:cts="http://chs.harvard.edu/xmlns/cts" xmlns:ti="http://chs.harvard.edu/xmlns/cts" xmlns:dc="http://purl.org/dc/elements/1.1" xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output omit-xml-declaration="yes" method="html" encoding="UTF-8"/>
    <xsl:include href="cts_header.xsl"/>

    <xsl:variable name="homeUrl">@ctshome@</xsl:variable>
    <xsl:variable name="formsUrl">@ctshome@</xsl:variable>


    <xsl:template match="/">

        <html>
            <head>


                <link href="cts-ui/css/cts-core.css" rel="stylesheet" title="CSS for CTS" type="text/css"/>
                <link href="cts-ui/css/cts.css" rel="stylesheet"/>
                <xsl:choose>
                    <xsl:when
                        test="/cts:CTSError">
                        <title>Error</title>
                    </xsl:when>
                    <xsl:otherwise>
                        <title>Opening of Text
                        </title>
                    </xsl:otherwise>
                </xsl:choose>
            </head>
            <body>


                <header>
                    <xsl:call-template name="header"/>
                </header>

                <nav>
                    <p>
                        <xsl:element name="a">
                            <xsl:attribute name="href"><xsl:value-of select="$homeUrl"/></xsl:attribute>
                            CTS home
                        </xsl:element>

                        <!--<xsl:element name="a">
							<xsl:attribute name="href"><xsl:value-of select="$formsUrl"/></xsl:attribute>
							Look up material by URN
						</xsl:element>-->


                    </p>

                </nav>

                <article>
                    <xsl:choose>
                        <xsl:when test="/cts:CTSError">
                            <xsl:apply-templates select="cts:CTSError"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <p>The first citation of <xsl:value-of select="//cts:requestUrn"/> is:</p>

                            <h1>
                                <xsl:element name="a">
                                    <xsl:attribute name="href">@ctsapi@?request=GetPassagePlus&amp;stylesheet=cts_passage&amp;urn=<xsl:value-of
                                        select="normalize-space(//cts:reply/cts:urn)"/></xsl:attribute>
                                    <xsl:value-of select="//cts:reply/cts:urn"/>
                                </xsl:element>
                            </h1>


                        </xsl:otherwise>
                    </xsl:choose>


                </article>

                <footer>
                    <xsl:call-template name="footer"/>
                </footer>

            </body>
        </html>



    </xsl:template>

</xsl:stylesheet>
