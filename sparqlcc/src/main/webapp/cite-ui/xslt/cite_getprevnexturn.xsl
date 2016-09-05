<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cite="http://chs.harvard.edu/xmlns/cite" 
    version="1.0">
    <xsl:include href="cite_header.xsl"/>
    <xsl:include href="cite_variables.xsl"/>
    <xsl:include href="cite_templates.xsl"/>
    
    <xsl:output method="html" omit-xml-declaration="yes"/>
    
    
    <xsl:template match="/">
        <html>
            <head>
                <xsl:choose>
                    <xsl:when
                        test="/cite:CITEError">
                        <title>Error</title>
                    </xsl:when>
                    <xsl:otherwise>
                        <title>CITE Request: <xsl:value-of select="//cite:request/cite:request"/> [<xsl:value-of select="//cite:resolvedUrn"/>]</title>
                    </xsl:otherwise>
                </xsl:choose>
                <meta charset="utf-8"></meta>
                <link rel="stylesheet" href="cite-ui/css/cite-core.css"></link>
            </head>
            <body class="no-logo">
                <header>
                    <xsl:call-template name="cite_header"/>
                </header>
                <xsl:call-template name="cite_nav"/>
                <article>
                    <xsl:choose>
                        <xsl:when test="cite:CITEError">
                            <xsl:call-template name="cite_error"/>
                        </xsl:when>
                        
                        <xsl:otherwise>
                            <div class="reply cite_replyData">
                                <xsl:if test="//cite:prevUrn">
                                <p>Previous Object URN: 
                                    <xsl:call-template name="cite_getObjectLink">
                                        <xsl:with-param name="urn"><xsl:value-of select="//cite:reply/cite:prevUrn"/></xsl:with-param>
                                    </xsl:call-template>
                                     </p>
                                </xsl:if>
                                <xsl:if test="//cite:nextUrn">
                                <p>Next Object Urn: 
                                    <xsl:call-template name="cite_getObjectLink">
                                        <xsl:with-param name="urn"><xsl:value-of select="//cite:reply/cite:nextUrn"/></xsl:with-param>
                                    </xsl:call-template>
                                 </p>
                                </xsl:if>
                                <xsl:if test="//cite:firstUrn">
                                    <p>First Object Urn: 
                                        <xsl:call-template name="cite_getObjectLink">
                                            <xsl:with-param name="urn"><xsl:value-of select="//cite:reply/cite:firstUrn"/></xsl:with-param>
                                        </xsl:call-template>
                                    </p>
                                </xsl:if>
                                <xsl:if test="//cite:lastUrn">
                                    <p>Last Object Urn: 
                                        <xsl:call-template name="cite_getObjectLink">
                                            <xsl:with-param name="urn"><xsl:value-of select="//cite:reply/cite:lastUrn"/></xsl:with-param>
                                        </xsl:call-template>
                                    </p>
                                </xsl:if>
                            </div>
                            <div class="cite_serviceLinks">
                                <p>
                                    <xsl:call-template name="cite_getValidReffLink"/>
                                </p>
                            </div>
                            <xsl:call-template name="cite_requestData"/>
                        </xsl:otherwise>
                    </xsl:choose>
                    
                    
                </article>
                <footer>
                    <xsl:call-template name="cite_footer"/>
                </footer>
            </body>
        </html>
    </xsl:template>
    
    
    
</xsl:stylesheet>
