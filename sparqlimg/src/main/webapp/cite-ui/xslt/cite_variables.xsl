<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns:cite="http://chs.harvard.edu/xmlns/cite"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <!-- CITE IMAGE -->
   
    <xsl:variable name="serviceUrl">http://localhost:8080/sparqlimg/</xsl:variable>
    <xsl:variable name="ccUrl">http://localhost:8080/sparqlcc/</xsl:variable>
    
    <xsl:variable name="getPagedUrl">api?request=GetValidReff&amp;safemode=on&amp;stylesheet=cite_getvalidreff&amp;offset=1&amp;limit=20&amp;urn=</xsl:variable>
   <xsl:variable name="projectLabel">CITE Image Extension</xsl:variable>
    
    <xsl:variable name="ictUrl">ict.html?urn=</xsl:variable>
    
    <xsl:variable name="thumbSize">300</xsl:variable>
    <xsl:variable name="bigImage">1000</xsl:variable>
   
    <xsl:variable name="homeUrl">home</xsl:variable>
    <xsl:variable name="formsUrl">home</xsl:variable>
    
</xsl:stylesheet>
