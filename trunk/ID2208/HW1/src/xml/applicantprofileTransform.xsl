<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:ns1='http://xml.netbeans.org/schema/applicantProfileXmlSchema'
                xmlns:cvNs='http://xml.netbeans.org/schema/resume'>
    <xsl:template match="/">
        <xsl:element name="ns1:applicant_profile">
            <xsl:element name="ns1:name">
                <xsl:value-of select="cvNs:resume/cvNs:name"/>
            </xsl:element>
            <xsl:element name="ns1:mail_id">
                <xsl:value-of select="cvNs:resume/cvNs:mail_id"/>
            </xsl:element>
            <xsl:element name="ns1:phone_number">
                <xsl:value-of select="cvNs:resume/cvNs:phone_number"/>
            </xsl:element>
            <xsl:element name="ns1:address">
                <xsl:element name="apt">
                    <xsl:value-of select="cvNs:resume/cvNs:address/cvNs:apt"/>
                </xsl:element>
                <xsl:element name="street">
                    <xsl:value-of select="cvNs:resume/cvNs:address/cvNs:street"/>
                </xsl:element>
                <xsl:element name="city">
                    <xsl:value-of select="cvNs:resume/cvNs:address/cvNs:city"/>
                </xsl:element>
                <xsl:element name="postalCode">
                    <xsl:value-of select="cvNs:resume/cvNs:address/cvNs:postalCode"/>
                </xsl:element>
                <xsl:element name="country">
                    <xsl:value-of select="cvNs:resume/cvNs:address/cvNs:country"/>
                </xsl:element>
            </xsl:element>
            <xsl:element name="ns1:summary">
                <xsl:value-of select="cvNs:resume/cvNs:summary"/>
            </xsl:element>
            <xsl:element name="ns1:education">
                <xsl:for-each select="cvNs:resume/cvNs:education/cvNs:qualification">
                    <xsl:element name="ns1:qualification">
                        <xsl:element name="ns1:university">
                            <xsl:value-of select="cvNs:university"/>
                        </xsl:element>
                        <xsl:element name="ns1:degree">
                            <xsl:value-of select="cvNs:degree"/>
                        </xsl:element>
                        <xsl:element name="ns1:year">
                            <xsl:value-of select="cvNs:year"/>
                        </xsl:element>
                        <xsl:element name="ns1:major">
                            <xsl:value-of select="cvNs:major"/>
                        </xsl:element>
                    </xsl:element>
                    <xsl:element name="ns1:gpa">4.0</xsl:element>
                </xsl:for-each>
            </xsl:element>
            <xsl:element name="ns1:professional_experience">
                <xsl:for-each select="cvNs:resume/cvNs:professional_experience/cvNs:company">
                    <xsl:element name="ns1:company">
                        <xsl:element name="ns1:company_profile">
                            <xsl:element name="ns1:name">
                                <xsl:value-of select="cvNs:company_profile/cvNs:name"/>
                            </xsl:element>
                            <xsl:element name="ns1:description">
                                <xsl:value-of select="cvNs:company_profile/cvNs:description"/>
                            </xsl:element>
                            <xsl:element name="ns1:email">
                                <xsl:value-of select="cvNs:company_profile/cvNs:email"/>
                            </xsl:element>
                            <xsl:element name="ns1:phone"/>
                            <xsl:element name="ns1:website"/>
                            <xsl:element name="ns1:businessType"/>
                        </xsl:element>
                        <xsl:element name="ns1:start_date">
                            <xsl:value-of select="cvNs:start_date"/>
                        </xsl:element>
                        <xsl:element name="ns1:end_date">
                            <xsl:value-of select="cvNs:end_date"/>
                        </xsl:element>
                        <xsl:element name="ns1:domain">
                            <xsl:value-of select="cvNs:domain"/>
                        </xsl:element>
                        <xsl:element name="ns1:positions">
                            <xsl:for-each select="cvNs:positions/cvNs:position">
                                <xsl:element name="ns1:position">
                                    <xsl:element name="ns1:positionTitle">
                                        <xsl:value-of select="cvNs:positionTitle"/>
                                    </xsl:element>
                                    <xsl:element name="ns1:entranceDate"/>
                                    <xsl:element name="ns1:exitDate"/>
                                    <xsl:element name="ns1:hoursPerWeek"/>
                                    <xsl:element name="ns1:location"/>
                                    <xsl:element name="ns1:exitReason"/>    
                                </xsl:element>
                            </xsl:for-each>
                        </xsl:element>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
            <xsl:element name="ns1:technical_skills">
                <xsl:for-each select="cvNs:resume/cvNs:technical_skills/cvNs:skill">
                    <xsl:element name="ns1:skill">
                        <xsl:element name="ns1:type">
                            <xsl:value-of select="cvNs:type"/>
                        </xsl:element>    
                        <xsl:element name="ns1:technology">
                            <xsl:value-of select="cvNs:technology"/>
                        </xsl:element>    
                    </xsl:element>    
                </xsl:for-each>
            </xsl:element>
            <xsl:element name="ns1:certifications">
                <xsl:for-each select="cvNs:resume/cvNs:certifications/cvNs:certification">
                    <xsl:element name="ns1:certification">
                        <xsl:element name="ns1:name">
                            <xsl:value-of select="cvNs:name"/>
                        </xsl:element>
                        <xsl:element name="ns1:organization">
                            <xsl:value-of select="cvNs:organization"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
            <xsl:element name="ns1:extracurricular_activities">
                <xsl:for-each select="cvNs:resume/cvNs:extracurricular_activities/cvNs:activity">
                    <xsl:element name="ns1:name">
                        <xsl:value-of select="cvNs:name"/>
                    </xsl:element>
                    <xsl:element name="ns1:achievement">
                        <xsl:value-of select="cvNs:achievement"/>
                    </xsl:element>
                </xsl:for-each>
            </xsl:element>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
