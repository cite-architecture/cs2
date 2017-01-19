<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns:cite="http://chs.harvard.edu/xmlns/cite"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- CITE CC -->

    <xsl:variable name="serviceUrl">http://localhost:8080/hmt-cc/</xsl:variable>
    <xsl:variable name="getObjectUrl">http://localhost:8080/hmt-cc/api?request=GetObjectPlus&amp;stylesheet=cite_getobject&amp;urn=</xsl:variable>
    <xsl:variable name="imgThumbUrl">http://localhost:8080/hmt-img/api?request=GetBinaryImage&amp;w=100&amp;urn=</xsl:variable>
    <xsl:variable name="imgLinkUrl">http://localhost:8080/hmt-img/api?request=GetImagePlus&amp;stylesheet=cite_getimageplus&amp;urn=</xsl:variable>
    <xsl:variable name="imgICTUrl">http://localhost:8080/hmt-img/ict.html?urn=</xsl:variable>
    <xsl:variable name="ctsUrl">http://localhost:8080/hmt-cts/api?request=GetPassagePlus&amp;stylesheet=cts_passage&amp;urn=</xsl:variable>


    <xsl:variable name="getObjectPlusUrl">api?request=GetObjectPlus&amp;stylesheet=cite_getobject&amp;urn=</xsl:variable>
    <xsl:variable name="getValidReffUrl">api?request=GetValidReff&amp;safemode=on&amp;stylesheet=cite_getvalidreff&amp;urn=</xsl:variable>
    <xsl:variable name="getPagedValidReffUrl">api?request=GetPagedValidReff&amp;safemode=on&amp;stylesheet=cite_getvalidreff&amp;offset=1&amp;limit=25&amp;urn=</xsl:variable>
    <xsl:variable name="getPagedUrl">api?request=GetPaged&amp;safemode=on&amp;stylesheet=cite_paged&amp;urn=</xsl:variable>

    <xsl:variable name="projectLabel">CITE Collection Service</xsl:variable>

    <xsl:variable name="thumbSize">300</xsl:variable>

    <xsl:variable name="homeUrl">../hmt-digital</xsl:variable>
    <xsl:variable name="formsUrl">home</xsl:variable>

</xsl:stylesheet>
