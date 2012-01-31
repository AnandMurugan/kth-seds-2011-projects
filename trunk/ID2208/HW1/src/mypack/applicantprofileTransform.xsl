<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : applicantprofileTransform.xsl
    Created on : January 30, 2012, 11:50 PM
    Author     : Anand
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:xsd="http://xml.netbeans.org/schema/applicantProfileXmlSchema applicantProfileXmlSchema.xsd ">
    <xsl:template match="/">
        <xsl:element name="xsd:name">
            <xsl:value-of select="name"/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
