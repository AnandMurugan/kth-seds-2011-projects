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
            <xsl:element name="address">
                <xsl:element name="doorno">
                    <xsl:value-of select="ns1:resume/ns1:address/ns1:doorno"/>
                </xsl:element>
                <xsl:element name="street">
                    <xsl:value-of select="ns1:resume/ns1:address/ns1:street"/>
                </xsl:element>
                <xsl:element name="county">
                    <xsl:value-of select="ns1:resume/ns1:address/ns1:county"/>
                </xsl:element>
                <xsl:element name="city">
                    <xsl:value-of select="ns1:resume/ns1:address/ns1:city"/>
                </xsl:element>
            </xsl:element>
            <xsl:element name="summary">
                <xsl:value-of select="ns1:resume/ns1:summary"/>
            </xsl:element>
            <xsl:element name="education">
                <xsl:for-each select="ns1:resume/ns1:education/ns1:qualification">
                    <xsl:element name="qualification">
                        <xsl:element name="university">
                            <xsl:value-of select="ns1:university"/>
                        </xsl:element>
                        <xsl:element name="degree">
                            <xsl:value-of select="ns1:degree"/>
                        </xsl:element>
                        <xsl:element name="year">
                            <xsl:value-of select="ns1:year"/>
                        </xsl:element>
                        <xsl:element name="major">
                            <xsl:value-of select="ns1:major"/>
                        </xsl:element>
                    </xsl:element>
                    <xsl:element name="gpa">0.0</xsl:element
                </xsl:for-each>
            </xsl:element>
            <xsl:element name="professional_experience"></xsl:element>
            <xsl:element name="technical_skills"></xsl:element>
            <xsl:element name="certifications"></xsl:element>
            <xsl:element name="extracurricular_activities"></xsl:element>
            <xsl:element name="motivation_letter"></xsl:element>
            <xsl:element name="preferred_workLocation"></xsl:element>
            <xsl:element name="job_type_applied"></xsl:element>
            <xsl:element name="reference"></xsl:element>
            <xsl:element name="work_permit">
                <xsl:element name="type"/>
                <xsl:element name="expiry_date"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
