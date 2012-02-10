<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:ns1='http://xml.netbeans.org/schema/jobSchema'
                xmlns:ns2="http://xml.netbeans.org/schema/commonSchema">
    <xsl:template match="/">
        <xsl:param name="criteria"/>
        <xsl:element name="jobs">
            <xsl:for-each select="ns1:jobs/ns1:job">
                <xsl:variable name="advJob" select="ns1:category" type="String"/>
                <xsl:if test="$advJob = $criteria">
                    <xsl:element name="job">
                        <xsl:element name="id">
                            <xsl:value-of select="ns1:id"/>
                        </xsl:element>
                        <xsl:element name="type">
                            <xsl:value-of select="ns1:type"/>
                        </xsl:element>
                        <xsl:element name="description">
                            <xsl:value-of select="ns1:description"/>
                        </xsl:element>
                        <xsl:element name="id">
                            <xsl:value-of select="ns1:id"/>
                        </xsl:element>
                        <xsl:element name="category">
                            <xsl:value-of select="ns1:category"/>
                        </xsl:element>
                        <xsl:element name="location">
                            <xsl:value-of select="ns1:location"/>
                        </xsl:element>
                        <xsl:element name="deadline_for_application">
                            <xsl:value-of select="ns1:deadline_for_application"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:if>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>
