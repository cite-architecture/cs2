<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns:cite="http://chs.harvard.edu/xmlns/cite"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   
    <xsl:variable name="serviceUrl">http://localhost:8080/sparqlcc/</xsl:variable>
    <xsl:variable name="getObjectPlusUrl">api?request=GetObjectPlus&amp;stylesheet=cite_getobject&amp;urn=</xsl:variable>
    <xsl:variable name="getValidReffUrl">api?request=GetValidReff&amp;safemode=on&amp;stylesheet=cite_getvalidreff&amp;urn=</xsl:variable>
    <xsl:variable name="getPagedValidReffUrl">api?request=GetPagedValidReff&amp;safemode=on&amp;stylesheet=cite_getvalidreff&amp;urn=</xsl:variable>
    <xsl:variable name="getPagedUrl">api?request=GetPaged&amp;safemode=on&amp;stylesheet=cite_paged&amp;urn=</xsl:variable>
    
   
    <xsl:variable name="homeUrl">home</xsl:variable>
    <xsl:variable name="formsUrl">home</xsl:variable>
    
</xsl:stylesheet>
