<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:ns="http://www.coffee.com" >
    <xsl:template match="/">
        <xsl:element name="ns:Coffee">         	      	
        	<xsl:element name="ns:CoffeePrice">                
                <xsl:value-of select="/priceList/coffee/name"/>
        	</xsl:element>
        	<xsl:element name="ns:CoffeeProducer">                
                <xsl:value-of select="/priceList/coffee/producer"/>
        	</xsl:element>        	
        </xsl:element>                                   
    </xsl:template>
</xsl:stylesheet>
