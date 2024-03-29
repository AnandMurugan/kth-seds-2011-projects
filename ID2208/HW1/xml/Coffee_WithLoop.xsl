<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:ns="http://www.coffee.com" >
    <xsl:template match="/">
        <xsl:element name="ns:Coffee">     
        	<xsl:for-each select="/priceList/coffee">    	      	
        		<xsl:element name="ns:CoffeePrice">                
                	<xsl:value-of select="name"/>
	        	</xsl:element>
    	    	<xsl:element name="ns:CoffeeProducer">                
        	        <xsl:value-of select="producer"/>
        		</xsl:element>
        	</xsl:for-each>        	
        </xsl:element>                                   
    </xsl:template>
</xsl:stylesheet>
