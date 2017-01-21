<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns:cite="http://chs.harvard.edu/xmlns/cite"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- CITE CC -->

    <xsl:variable name="serviceUrl">@tripleserver@</xsl:variable>
    <xsl:variable name="getObjectUrl">/@ccapi@?request=GetObjectPlus&amp;stylesheet=cite_getobject&amp;urn=</xsl:variable>
    <xsl:variable name="imgThumbUrl">@imgapi@?request=GetBinaryImage&amp;w=100&amp;urn=</xsl:variable>
    <xsl:variable name="imgLinkUrl">@imgapi@?request=GetImagePlus&amp;stylesheet=cite_getimageplus&amp;urn=</xsl:variable>
    <xsl:variable name="imgICTUrl">@imgapi@/ict.html?urn=</xsl:variable>
    <xsl:variable name="ctsUrl">@ctsapi@?request=GetPassagePlus&amp;stylesheet=cts_passage&amp;urn=</xsl:variable>


    <xsl:variable name="getObjectPlusUrl">@ccapi@?request=GetObjectPlus&amp;stylesheet=cite_getobject&amp;urn=</xsl:variable>
    <xsl:variable name="getValidReffUrl">@ccapi@?request=GetValidReff&amp;safemode=on&amp;stylesheet=cite_getvalidreff&amp;urn=</xsl:variable>
    <xsl:variable name="getPagedValidReffUrl">@ccapi@?request=GetPagedValidReff&amp;safemode=on&amp;stylesheet=cite_getvalidreff&amp;offset=1&amp;limit=25&amp;urn=</xsl:variable>
    <xsl:variable name="getPagedUrl">@ccapi@?request=GetPaged&amp;safemode=on&amp;stylesheet=cite_paged&amp;urn=</xsl:variable>

    <xsl:variable name="projectLabel">@projectLabel@</xsl:variable>

    <xsl:variable name="thumbSize">@thumbsize@</xsl:variable>

    <xsl:variable name="homeUrl">@cchome@</xsl:variable>
    <xsl:variable name="formsUrl">@cchome@</xsl:variable>

</xsl:stylesheet>
