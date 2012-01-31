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
            <xsl:element name="mail_id">
                <xsl:value-of select="ns1:resume/ns1:mail_id"/>
            </xsl:element>
            <xsl:element name="phone_number">
                <xsl:value-of select="ns1:resume/ns1:phone_number"/>
            </xsl:element>
            <xsl:element name="Address">
                <xsl:element name="doorno">
                    <xsl:value-of select="ns1:resume/ns1:Address/ns1:doorno"/>
                </xsl:element>
                <xsl:element name="street">
                    <xsl:value-of select="ns1:resume/ns1:Address/ns1:street"/>
                </xsl:element>
                <xsl:element name="county">
                    <xsl:value-of select="ns1:resume/ns1:Address/ns1:county"/>
                </xsl:element>
                <xsl:element name="city">
                    <xsl:value-of select="ns1:resume/ns1:Address/ns1:city"/>
                </xsl:element>
            </xsl:element>
            <xsl:element name="summary">
                <xsl:value-of select="ns1:resume/ns1:summary"/>
            </xsl:element>
            <xsl:element name="education"></xsl:element>
            <xsl:element name="professional_experience"></xsl:element>
            <xsl:element name="technical_skills"></xsl:element>
            <xsl:element name="certifications"></xsl:element>
            <xsl:element name="Extracurricular_activities"></xsl:element>
            <xsl:element name="motivation_letter"></xsl:element>
            <xsl:element name="Preferred_WorkLocation"></xsl:element>
            <xsl:element name="Job_type_applied"></xsl:element>
            <xsl:element name="Reference"></xsl:element>
            <xsl:element name="Work_Permit"></xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
