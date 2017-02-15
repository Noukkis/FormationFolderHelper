<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:param name="eleveName" />
    <xsl:template match="/">
        <xsl:for-each select="Eleves/Eleve[@name = $eleveName]">            
            <h1 style="text-align: center;"><span style="font-size:24px;"><u>Rapport de dossier de formation</u></span></h1>
            
            <h3><span style="font-size:18px;">Eleve : <xsl:value-of select="@name"/></span></h3>
            
            <h3><span style="font-size:18px;">Complet a <xsl:value-of select="round(100 * count(Categorie/Keyword[@present='true']) div count(Categorie/Keyword))"/>%</span></h3>
            
            <hr />
            <xsl:for-each select="Categorie">
                <p><strong><xsl:value-of select="@name"/> :</strong></p>
                <ul>
                    <xsl:for-each select="Keyword">
                        <li>
                            <span>
                                <xsl:choose>
                                    <xsl:when test="@present='true'">
                                        <xsl:attribute name="style">color:#008000;</xsl:attribute>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:attribute name="style">color:#FF0000;</xsl:attribute>
                                    </xsl:otherwise>
                                </xsl:choose>
                                <xsl:variable name="testOk">
                                    <xsl:choose>
                                        <xsl:when test="@present='true'">Ok</xsl:when>
                                        <xsl:otherwise>Ko</xsl:otherwise>
                                    </xsl:choose>
                                </xsl:variable>
                                <xsl:value-of select="concat(@name,' : ',$testOk)"/>
                            </span>
                        </li>
                    </xsl:for-each>
                </ul>        
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
