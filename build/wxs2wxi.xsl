<xsl:stylesheet version="1.0"
            xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
            xmlns:msxsl="urn:schemas-microsoft-com:xslt"
            exclude-result-prefixes="msxsl"
            xmlns:wix="http://schemas.microsoft.com/wix/2006/wi"
            >
			
    <!-- Turns a Wix wxs file into a wxi include file that can then be used in another master wxs file -->
			
    <xsl:output method="xml" indent="yes" />

    <xsl:strip-space elements="*"/>

    <xsl:template match='wix:Wix'>
	    <Include>
            <xsl:apply-templates select="*"/>
	    </Include>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>