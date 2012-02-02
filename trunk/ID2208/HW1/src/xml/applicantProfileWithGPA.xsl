<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : applicantprofileTransform.xsl
    Created on : January 30, 2012, 11:50 PM
    Author     : Anand
    Description:
        Purpose of transformation follows.
        xmlns:xsd="http://xml.netbeans.org/schema/applicantProfileXmlSchema applicantProfileXmlSchema.xsd "
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:xalan="http://xml.apache.org/xalan"
                xmlns:ns1='http://xml.netbeans.org/schema/resume'
                xmlns:ns2='http://xml.netbeans.org/schema/transcriptSchema'>
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
                <xsl:for-each select="ns2:transcript">
                    <xsl:element name="qualification">
                        <xsl:element name="university">
                            <xsl:value-of select="ns2:university"/>
                        </xsl:element>
                        <xsl:element name="degree">
                            <xsl:value-of select="ns2:degree"/>
                        </xsl:element>
                        <xsl:element name="year">
                            <xsl:value-of select="ns2:year"/>
                        </xsl:element>
                        <xsl:element name="major">
                            <xsl:value-of select="ns2:major"/>
                        </xsl:element>
                        <xsl:element name="gpa">
                            <xsl:variable name="totalCredits" select="sum(ns2:courses/ns2:course/ns2:credits)"/>
                            <xsl:variable name="gradePoints">
                                <xsl:for-each select="ns2:courses/ns2:course">
                                    <iterate>
                                        <xsl:choose>
                                            <xsl:when test="ns2:grade = 'A'">
                                                <xsl:value-of select="ns2:credits * 5"/>
                                            </xsl:when>
                                            <xsl:when test="ns2:grade = 'B'">
                                                <xsl:value-of select="ns2:credits * 4"/>
                                            </xsl:when>
                                            <xsl:when test="ns2:grade = 'C'">
                                                <xsl:value-of select="ns2:credits * 3"/>
                                            </xsl:when>
                                            <xsl:when test="ns2:grade = 'D'">
                                                <xsl:value-of select="ns2:credits * 2"/>
                                            </xsl:when>
                                            <xsl:when test="ns2:grade = 'E'">
                                                <xsl:value-of select="ns2:credits * 1"/>
                                            </xsl:when>
                                            <xsl:when test="ns2:grade = 'F'">
                                                <xsl:value-of select="0"/>
                                            </xsl:when>
                                        </xsl:choose>
                                    </iterate>
                                </xsl:for-each>
                            </xsl:variable>
                            <xsl:value-of select="sum(xalan:nodeset($gradePoints)/iterate) div $totalCredits" />
                        </xsl:element>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
            <xsl:element name="professional_experience"></xsl:element>
            <xsl:element name="technical_skills"></xsl:element>
            <xsl:element name="certifications"></xsl:element>
            <xsl:element name="extracurricular_activities"></xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
