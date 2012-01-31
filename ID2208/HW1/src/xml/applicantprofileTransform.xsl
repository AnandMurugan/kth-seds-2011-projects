<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : applicantprofileTransform.xsl
    Created on : January 30, 2012, 11:50 PM
    Author     : Anand
    Description:
        Purpose of transformation follows.
        xmlns:xsd="http://xml.netbeans.org/schema/applicantProfileXmlSchema applicantProfileXmlSchema.xsd "
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:ns1='http://xml.netbeans.org/schema/resume'>
    <xsl:template match="/">
        <xsl:element name="applicant_profile">
            <xsl:element name="name">
                <xsl:value-of select="ns1:resume/ns1:name"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
