<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns:cite="http://chs.harvard.edu/xmlns/cite"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- CITE IMAGE -->

    <xsl:variable name="serviceUrl">@imgapi@</xsl:variable>
    <xsl:variable name="ccUrl">@ccapi@</xsl:variable>

    <xsl:variable name="getPagedUrl">@imgapi@?request=GetValidReff&amp;safemode=on&amp;stylesheet=cite_getvalidreff&amp;offset=1&amp;limit=20&amp;urn=</xsl:variable>
   <xsl:variable name="projectLabel">CITE Image Extension</xsl:variable>

    <xsl:variable name="ictUrl">@icturl@</xsl:variable>

    <xsl:variable name="thumbSize">@thumbsize@</xsl:variable>
    <xsl:variable name="bigImage">@bigimage@</xsl:variable>

    <xsl:variable name="homeUrl">@imghome@</xsl:variable>
    <xsl:variable name="formsUrl">@imghome@</xsl:variable>

</xsl:stylesheet>
