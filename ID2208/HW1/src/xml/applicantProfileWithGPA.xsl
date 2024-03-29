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
        <xsl:variable name="personalNumber" select="ns1:resume/ns1:personalNumber"/>
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
                <xsl:for-each select="ns2:transcript/ns2:transcriptRecord">
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
            <xsl:element name="professional_experience">
                <xsl:for-each select="ns1:resume/ns1:professional_experience/ns1:company">
                    <xsl:element name="company">
                        <xsl:element name="company_profile">
                            <xsl:element name="name">
                                <xsl:value-of select="ns1:company_profile/ns1:name"/>
                            </xsl:element>
                            <xsl:element name="description">
                                <xsl:value-of select="ns1:company_profile/ns1:description"/>
                            </xsl:element>
                            <xsl:element name="email">
                                <xsl:value-of select="ns1:company_profile/ns1:email"/>
                            </xsl:element>
                            <xsl:element name="phone">
                                <xsl:value-of select="ns1:company_profile/ns1:phone"/>
                            </xsl:element>
                            <xsl:element name="website">
                                <xsl:value-of select="ns1:company_profile/ns1:website"/>
                            </xsl:element>
                            <xsl:element name="businessType">
                                <xsl:value-of select="ns1:company_profile/ns1:businessType"/>
                            </xsl:element>
                        </xsl:element>
                        <xsl:element name="start_date">
                            <xsl:value-of select="ns1:start_date"/>
                        </xsl:element>
                        <xsl:element name="end_date">
                            <xsl:value-of select="ns1:end_date"/>
                        </xsl:element>
                        <xsl:element name="domain">
                            <xsl:value-of select="ns1:domain"/>
                        </xsl:element>
                        <xsl:element name="positions">
                            <xsl:for-each select="ns1:positions/ns1:position">
                                <xsl:element name="position">
                                    <xsl:element name="positionTitle">
                                        <xsl:value-of select="ns1:positionTitle"/>
                                    </xsl:element>
                                    <xsl:element name="entranceDate"/>
                                    <xsl:element name="exitDate"/>
                                    <xsl:element name="hoursPerWeek"/>
                                    <xsl:element name="location"/>
                                    <xsl:element name="exitReason"/>    
                                </xsl:element>
                            </xsl:for-each>
                        </xsl:element>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
            <xsl:element name="technical_skills">
                <xsl:for-each select="ns1:resume/ns1:technical_skills/ns1:skill">
                    <xsl:element name="skill">
                        <xsl:element name="type">
                            <xsl:value-of select="ns1:type"/>
                        </xsl:element>    
                        <xsl:element name="technology">
                            <xsl:value-of select="ns1:technology"/>
                        </xsl:element>    
                    </xsl:element>    
                </xsl:for-each>
            </xsl:element>
            <xsl:element name="certifications">
                <xsl:for-each select="ns1:resume/ns1:certifications/ns1:certification">
                    <xsl:element name="certification">
                        <xsl:element name="name">
                            <xsl:value-of select="ns1:name"/>
                        </xsl:element>
                        <xsl:element name="organization">
                            <xsl:value-of select="ns1:organization"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
            <xsl:element name="extracurricular_activities">
                <xsl:for-each select="ns1:resume/ns1:extracurricular_activities/ns1:activity">
                    <xsl:element name="name">
                        <xsl:value-of select="ns1:name"/>
                    </xsl:element>
                    <xsl:element name="achievement">
                        <xsl:value-of select="ns1:achievement"/>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
